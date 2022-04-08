package bank.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;

import bank.model.Client;
import bank.model.Client_;

@Stateless
public class ClientDao extends AbstractDao<Client, Integer> {

	@PersistenceContext
	private EntityManager em;

	public ClientDao() {
		super(Client.class, Integer.class);
	}
	
	@Override
	public EntityManager em() {
		return em;
	}

	public List<Client> findByExample(Client example) {
		TypedQuery<Client> query = createQueryBasedOnCriteria(example);
		
//		query.setMaxResults(2); --> nem hatékony a fetch-csel együtt
		applyAccountsEntityGraph(query);
		return query.getResultList();
	}

	private TypedQuery<Client> createQueryBasedOnCriteria(Client example) {
		int clientid = example.getClientid();
		String address = example.getAddress();
		String name = example.getName();
		
		CriteriaBuilder cb = getCriteriaBuilder();
		CriteriaQuery<Client> cq = cb.createQuery(Client.class);
		Root<Client> root = cq.from(Client.class);
		//root.fetch(Client_.accounts, JoinType.LEFT);
		
		cq.select(root);
		
		List<Predicate> predicates = new ArrayList<>();
		if(clientid > 0) {
			predicates.add(cb.equal(root.get(Client_.clientid), clientid));
		}
		
		if(StringUtils.isNotEmpty(address)) {
			predicates.add(cb.like(root.get(Client_.address), "%" + address + "%"));
		}
		
		if(StringUtils.isNotEmpty(name)) {
			predicates.add(cb.like(root.get(Client_.name), name + "%"));
		}

		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		
		TypedQuery<Client> query = em.createQuery(cq);
		
		return query;
	}

	private void applyAccountsEntityGraph(TypedQuery<Client> query) {
		EntityGraph<?> eg = em.getEntityGraph("Client.EGWithAccounts");
		query.setHint("javax.persistence.loadgraph", eg);
	}
	
	public List<Client> findByExampleWithPaging(Client example, int maxResults, int firstResult) {
		TypedQuery<Client> query = createQueryBasedOnCriteria(example);
		query.setMaxResults(2);
		List<Client> clientsWithNoAccounts = query.getResultList();
		return fetchSelectedClientsWithAccounts(clientsWithNoAccounts);
	}

	private List<Client> fetchSelectedClientsWithAccounts(List<Client> clientsWithNoAccounts) {
		TypedQuery<Client> query = em.createNamedQuery("Client.findByIdIn", Client.class)
				.setParameter("ids", clientsWithNoAccounts.stream().map(Client::getClientid).collect(Collectors.toList()));
		
		applyAccountsEntityGraph(query);
		return query
				.getResultList();
	}

	
	public List<Client> findByExampleWithHibernate(Client example) {
		Example ex = Example
			.create(example)
			.enableLike(MatchMode.START)
			.excludeZeroes();
		
		Session session = em.unwrap(Session.class);

		return session.createCriteria(Client.class)
				.add(ex)
				.list();
		
	}

}
