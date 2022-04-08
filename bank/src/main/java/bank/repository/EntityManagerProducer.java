package bank.repository;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EntityManagerProducer {

	@PersistenceContext
	private EntityManager em;
	
	
	@Produces
	@RequestScoped
	public EntityManager getEntityManager() {
		return em;
	}
}
