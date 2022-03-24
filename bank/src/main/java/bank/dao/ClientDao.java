package bank.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import bank.model.Client;

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

}
