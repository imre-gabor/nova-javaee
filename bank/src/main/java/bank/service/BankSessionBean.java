package bank.service;

import static bank.repository.ClientSpecifications.addressContains;
import static bank.repository.ClientSpecifications.isClientIdEqual;
import static bank.repository.ClientSpecifications.nameStartsWith;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import bank.dao.AccountDao;
import bank.dao.ClientDao;
import bank.dao.HistoryDao;
import bank.model.Account;
import bank.model.BankException;
import bank.model.Client;
import bank.model.History;
import bank.model.History.Status;
import bank.model.QClient;
import bank.repository.ClientRepository;
import bank.repository.RepositoryFactory;

/**
 * Session Bean implementation class BankSessionBean
 */
@Stateless
@Interceptors(LoggerInterceptor.class)
public class BankSessionBean implements BankSessionBeanLocal {
	
	@EJB
	ClientDao clientDao;
	
//	@Inject
	ClientRepository clientRepository;
	
	@EJB
	AccountDao accountDao;
	
	@EJB
	HistoryDao historyDao;
	
	@Resource
	SessionContext ctx;
	
	@Resource
	TimerService timerService;
	
	@PersistenceContext
	EntityManager em;
	
	@PostConstruct
	public void init() {
		this.clientRepository = RepositoryFactory.createRepository(em, ClientRepository.class);
	}

    @Override
	public void createClient(Client client) {
//    	clientDao.create(client);
    	clientRepository.save(client);
    }
    
    @Override
	public void createAccountForClient(Account account, int clientId) throws BankException {
    	Optional<Client> optionalClient = clientDao.findById(clientId);
//    	Optional<Client> optionalClient = clientRepository.findById(clientId);
    	if(!optionalClient.isPresent())
    		throw new BankException("Client with given id does not exist");

    	optionalClient.get().addAccount(account);
    	account.setCreatedate(new Date());
    	accountDao.create(account);
    }
    
    
    @Override
	public void scheduleTransfer(int fromAccountId, int toAccountId, double amount) {
    	timerService.createSingleActionTimer(5000, new TimerConfig(new TransferData(fromAccountId, toAccountId, amount), false));
    }
    
    @Timeout
    public void executeScheduledTransfer(Timer timer) {
    	TransferData transferData = (TransferData) timer.getInfo();
    	try {
			transfer(transferData.getFromAccountId(), transferData.getToAccountId(), transferData.getAmount());
		} catch (BankException e) {
			System.out.println("Scheduled transfer could not be executed.");
			e.printStackTrace();
		}
    }
    
    @Override
	public void transfer(int fromAccountId, int toAccountId, double amount) throws BankException {
    	
    	History history = null;
    	Status status = null;
    	BankSessionBeanLocal self = ctx.getBusinessObject(BankSessionBeanLocal.class);
    	try {
    		//cél: akkor is le legyen naplózva a history táblába ez az esemény, ha végül a transfer sikertelen, és rollbackel
			history = self
    			.logHistory(String.format("Trying to transfer from account %d to account %d amount %f", fromAccountId, toAccountId, amount), amount);
    		
	    	Optional<Account> fromAccount = accountDao.findById(fromAccountId);
	    	Optional<Account> toAccount = accountDao.findById(toAccountId);
	    	if(!fromAccount.isPresent() || !toAccount.isPresent())
	    		throw new BankException("Non-existing account.");
	    	
	    	toAccount.get().increase(amount);
	    	fromAccount.get().decrease(amount);
	    	status = Status.SUCCESS;
    	} catch (Exception e) {
    		status = Status.FAILURE;
    		ctx.setRollbackOnly();
    		throw new BankException(e);
    	} finally {
    		self.updateHistoryStatus(history, status);
    	}
    }
    
    /*
     * csak akkor működik, más middleware szolgáltatásokhoz hasonlóan, ha a generált wrapperen keresztül hívjuk
     * ha lokálisan ebből az EJB-ből, akkor nem!
     */
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) 
    public History logHistory(String message, Double amount) {
    	History history = new History(message);
    	history.setAmount(amount);
    	history.setStatus(Status.PENDING);
		return historyDao.create(history);
    }
    
    
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) 
    public void updateHistoryStatus(History history, Status status) {
    	history.setStatus(status);
    	historyDao.update(history);
    }
    
//    @Schedule(minute = "*/1", hour = "*")
    public void logTopTransfers() {
    	historyDao.findTop5Today().forEach(System.out::println);
    }
    
    
    @Override
    @Transactional
//    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Client> searchClients(Client example){
		//    	return clientDao.findByExample(example);
//    	return clientDao.findByExampleWithPaging(example, 2, 0);
//    	return clientDao.findByExampleWithHibernate(example);
//    	List<Client> clients = findClientsWithSpringDataExampleWithPaging(example);
//    	List<Client> clients = findClientsWithSpringDataSpecificationWithPaging(example);
    	List<Client> clients = findClientsWithQueryDslWithPaging(example);
		List<Client> clientsWithAccounts = clientRepository.findByClientidIn(clients.stream().map(Client::getClientid).collect(Collectors.toList()));
		clientsWithAccounts.forEach(c -> {
			if(c.getAccounts() != null)
				c.getAccounts().size();
		});
		
		return clientsWithAccounts;
    	
    }

	

	private List<Client> findClientsWithSpringDataExampleWithPaging(Client example) {
		Example<Client> ex = Example
    			.of(example, ExampleMatcher.matching()
    					.withIgnoreNullValues()
    					.withIgnoreCase()
    					.withStringMatcher(StringMatcher.STARTING)
    					.withTransformer("clientid", id -> ((Integer)id.get()) == 0 ? Optional.empty() : Optional.of(id))
    					.withMatcher("address", matcher -> matcher.contains()));
    	
    	Page<Client> pageOfClient = clientRepository.findAll(ex, createPageRequest());
    	List<Client> clients = pageOfClient.getContent();
		return clients;
	}
    
	private List<Client> findClientsWithSpringDataSpecificationWithPaging(Client example) {
		int clientid = example.getClientid();
		String address = example.getAddress();
		String name = example.getName();
		
		Specification<Client> spec = Specification.where(null);
		
		if(clientid > 0) {
			spec = spec.and(isClientIdEqual(clientid));
		}
		
		if(StringUtils.isNotEmpty(address)) {
			spec = spec.and(addressContains(address));
		}
		
		if(StringUtils.isNotEmpty(name)) {
			spec = spec.and(nameStartsWith(name));
		}
		return clientRepository
				.findAll(spec, createPageRequest())
				.getContent();
		
	}

	private PageRequest createPageRequest() {
		return PageRequest.of(0, 2, Sort.by(Order.desc("name"), Order.asc("clientid")));
	}
	
	private List<Client> findClientsWithQueryDslWithPaging(Client example) {
		
		int clientid = example.getClientid();
		String address = example.getAddress();
		String name = example.getName();
		
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		
		QClient qClient = QClient.client;
		
		if(clientid > 0) {
			booleanBuilder.and(qClient.clientid.eq(clientid));
		}
		
		if(StringUtils.isNotEmpty(address)) {
			booleanBuilder.and(qClient.address.containsIgnoreCase(address));
		}
		
		if(StringUtils.isNotEmpty(name)) {
			booleanBuilder.and(qClient.name.startsWithIgnoreCase(name));
		}
		
		Predicate predicate = booleanBuilder.getValue();
		
		return predicate != null ? clientRepository.findAll(predicate, createPageRequest()).getContent() 
				: clientRepository.findAll(createPageRequest()).getContent();
				
	}

}
