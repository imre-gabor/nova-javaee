package bank.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import javax.ejb.EJB;

import org.assertj.core.data.Offset;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.ImmutableMap;

import bank.dao.AccountDao;
import bank.model.Account;

@RunWith(Arquillian.class)
public class CurrencyConverterIT {

	@EJB
	AccountDao accountDao;
	
	
	@Deployment
	public static WebArchive createDeployment() {
		File[] dependencies = Maven.resolver().loadPomFromFile("pom.xml")
			.resolve("org.assertj:assertj-core", "com.google.guava:guava")
			.withTransitivity()
			.asFile();
		
		return ShrinkWrap.create(WebArchive.class)
				.addPackages(false, "bank.model", "bank.dao")
				.addAsResource("META-INF/persistence.xml")
				.addAsLibraries(dependencies);
	}
	
	@Before
	public void init() {
		accountDao.deleteAll();
	}
	
	
	
	@Test
	public void testThatBalanceIsUnchangedAfterSaveAndRead() throws Exception {
		double balance = 100.0;
		Account account = createNewAccount(balance);
		
		Account accountFromDb = accountDao.findById(account.getAccountid()).get();
		assertThat(accountFromDb.getBalance()).isCloseTo(balance, Offset.offset(0.00001));
	}

	private Account createNewAccount(double balance) {
		Account account = new Account();
		account.setBalance(balance);
		accountDao.create(account);
		return account;
	}
	
	@Test
	public void testThatBalanceInDbIsConverted() throws Exception {
		double balance = 100.0;
		Account account = createNewAccount(balance);
		List<Double> balances = accountDao.findWithNativeQuery(Double.class, "SELECT a.balance FROM Account a WHERE a.accountid = :id" , ImmutableMap.of("id", account.getAccountid()));
		assertThat(balances.get(0)).isCloseTo(100.0 / 370.0, Offset.offset(0.00000001));
	}
	
	
	@Test
	public void testThatJpqlQueryParamOnBalanceIsConverted() throws Exception {
		double balance = 100.0;
		Account account = createNewAccount(balance);
		
		List<Account> found = accountDao.findWithJpql("SELECT a FROM Account a WHERE a.balance > :limit", ImmutableMap.of("limit", 80.0));
		assertThat(found.get(0))
			.usingRecursiveComparison()
			.isEqualTo(account);
	}
	
	@Test
	public void testThatNativeQueryParamOnBalanceIsNotConverted() throws Exception {
		double balance = 100.0;
		Account account = createNewAccount(balance);
		
		List<Integer> found = accountDao.findWithNativeQuery(
				Integer.class, 
				"SELECT accountid FROM Account WHERE balance > :limit", 
				ImmutableMap.of("limit", 80.0));
				
		assertThat(found).isEmpty();
		
		
		found = accountDao.findWithNativeQuery(
				Integer.class, 
				"SELECT accountid FROM Account WHERE balance > :limit", 
				ImmutableMap.of("limit", 100.0/370.0 - 0.0001));
		
		assertThat(found).hasSize(1);
		assertThat(found.get(0)).isEqualTo(account.getAccountid());
	}
}
