package bank.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import bank.dao.AccountDao;
import bank.model.Account;
import bank.model.BankException;
import bank.model.Client;
import bank.repository.ClientRepository;

//másik megoldás DB takarításra
//@Transactional(value=TransactionMode.ROLLBACK)
@RunWith(Arquillian.class)
public class BankIT {

	@EJB
	BankSessionBeanLocal bank;
	
	@EJB
	AccountDao accountDao;
	
//	@EJB
//	ClientDao clientDao;
	
	@Inject
	ClientRepository clientRepository;
	
	@Deployment
	public static WebArchive createDeployment() {
		File[] dependencies = Maven.resolver().loadPomFromFile("pom.xml")
			.resolve("org.assertj:assertj-core")
			.withTransitivity()
			.asFile();
		
		return ShrinkWrap.create(WebArchive.class)
				.addPackages(false, "bank.model", "bank.dao", "bank.service")
				.addAsResource("META-INF/persistence.xml")
				.addAsLibraries(dependencies);
	}
	
	@Before
	public void init() {
		accountDao.deleteAll();
//		clientDao.deleteAll();
		clientRepository.deleteAll();
	}
	
	@Test
	public void testThatAccountForNonExistingClientCannotBeCreated() throws Exception {
		
		//ARRANGE
		int clientId = 100;
		
		//ACT
		try {
			bank.createAccountForClient(new Account(), clientId);
			//ASSERT
			fail("Expected exception not thrown.");
		} catch (Exception e) {
			//ASSERT
			if(! (e instanceof BankException) ) {
				e.printStackTrace();
				fail("Unexpected exception " + e.getMessage());
			} else {
				assertEquals("Client with given id does not exist", e.getMessage());
			}
		}
	}
	
	
	@Test
	public void testThatAccountForExistingClientIsCreated() throws Exception {
		//ARRANGE
		Client client = new Client();
		client.setAccounts(new ArrayList<Account>());
//		client = clientDao.create(client);
		client = clientRepository.save(client);
		int clientid = client.getClientid();
		
		Account account = new Account();
		double balance = 100.0;
		account.setBalance(balance);
		
		//ACT
		bank.createAccountForClient(account, clientid);
		
		//ASSERT
		Account accountInDb = accountDao.findAll().get(0);
		
//		assertEquals(balance, accountInDb.getBalance(), 0.000001);
//		assertEquals(clientid, accountInDb.getClient().getClientid());
		//dátumot is ellenőrizni lehetne
		
		//AssertJ használatával
		assertThat(accountInDb.getBalance()).isEqualTo(balance);
		assertThat(accountInDb.getCreatedate()).isInSameDayAs(Instant.now());
		Client clientInDb = accountInDb.getClient();
		assertThat(clientInDb)
			.usingRecursiveComparison()
			.ignoringFields("accounts")
			.isEqualTo(client);
		
	}
	
}
