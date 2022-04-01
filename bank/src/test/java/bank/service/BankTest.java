package bank.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Optional;

import javax.ejb.EJB;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import bank.dao.AccountDao;
import bank.dao.ClientDao;
import bank.model.Account;
import bank.model.BankException;
import bank.model.Client;

@RunWith(MockitoJUnitRunner.class)
public class BankTest {

	@InjectMocks
	BankSessionBeanLocal bank;
	
	@Mock
	ClientDao clientDao;
	
	@Mock
	AccountDao accountDao;
	
	
	//@Test(expected = BankException.class) // --> nem tudjuk ellenőrizni a kivétel részleteit
	@Test
	public void testThatAccountForNonExistingClientCannotBeCreated() throws Exception {
		
		//ARRANGE
		int clientId = 100;
		when(clientDao.findById(100)).thenReturn(Optional.empty());
		
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
		int clientid = 1;
		client.setClientid(clientid);
		client.setAccounts(new ArrayList<Account>());
		when(clientDao.findById(clientid)).thenReturn(Optional.of(client));
		
		int accountid = 2;
		Account account = new Account();
		double balance = 100.0;
		account.setBalance(balance);
		account.setAccountid(accountid);
		
		//ACT
		bank.createAccountForClient(account, clientid);
		
		//ASSERT
		assertEquals(balance, account.getBalance(), 0.000001);
		assertEquals(clientid, account.getClient().getClientid());
		assertEquals(accountid, client.getAccounts().get(0).getAccountid());
		//dátumot is ellenőrizni lehetne
		
		verify(accountDao, times(1)).create(account);
		
	}
}
