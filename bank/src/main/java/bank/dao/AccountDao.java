package bank.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import bank.model.Account;

@Stateless
public class AccountDao extends AbstractDao<Account, Integer> {

	@PersistenceContext
	private EntityManager em;

	public AccountDao() {
		super(Account.class, Integer.class);
	}
	
	@Override
	public EntityManager em() {
		return em;
	}

}
