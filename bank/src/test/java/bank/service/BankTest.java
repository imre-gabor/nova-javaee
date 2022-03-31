package bank.service;

import static org.junit.Assert.*;

import java.util.Optional;

import javax.ejb.EJB;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import bank.dao.AccountDao;
import bank.dao.ClientDao;
import bank.model.Account;

@RunWith(MockitoJUnitRunner.class)
public class BankTest {

	@InjectMocks
	BankSessionBean bank;
	
	@Mock
	ClientDao clientDao;
	
	@Mock
	AccountDao accountDao;
	
//	@Before
//	public void init() {
//		MockitoAnnotations.initMocks(bank);
//	}
//	
	
	//@Test(expected = BankException.class) // --> nem tudjuk ellenőrizni a kivétel részleteit
	@Test
	public void testThatAccountForNonExistingClientCannotBeCreated() throws Exception {
		
		//ARRANGE
		int clientId = 100;
		Mockito.when(clientDao.findById(100)).thenReturn(Optional.empty());
		
		//ACT
		try {
			bank.createAccountForClient(new Account(), clientId);
			fail("Expected exception not thrown.");
		} catch (Exception e) {
			if(! (e instanceof BankException) ) {
				e.printStackTrace();
				fail("Unexpected exception " + e.getMessage());
			} else {
				assertEquals("Client with given id does not exist", e.getMessage());
			}
		}
		
		//ASSERT
		
	}
}
