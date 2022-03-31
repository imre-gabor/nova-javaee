package bank.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import bank.model.History;

@Stateless
public class HistoryDao extends AbstractDao<History, Long> {

	@PersistenceContext
	private EntityManager em;

	public HistoryDao() {
		super(History.class, Long.class);
	}
	
	@Override
	public EntityManager em() {
		return em;
	}

}
