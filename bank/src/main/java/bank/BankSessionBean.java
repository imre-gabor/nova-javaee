package bank;

import javax.ejb.Stateless;

/**
 * Session Bean implementation class BankSessionBean
 */
@Stateless
public class BankSessionBean implements BankSessionBeanLocal {

    public BankSessionBean() {
    }
    
    
    @Override
	public void createClient(Client client) {
    	
    }
    
    @Override
	public void createAccountForClient(Account account, int clientId) throws BankException {
    	
    }
    
    @Override
	public void transfer(int fromAccountId, int toAccountId, double amount) throws BankException {
    	
    }

}
