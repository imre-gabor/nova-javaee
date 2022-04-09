package bank.repository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

//1. próbálkozás
//@Stateless
//public class EntityManagerProducer {
//
//	@PersistenceContext
//	private EntityManager em;
//	
//	
//	@Produces
//	@RequestScoped
//	public EntityManager getEntityManager() {
//		return em;
//	}
//}

//2. próbálkozás
//@ApplicationScoped
//class EntityManagerFactoryProducer {
//
//	  @Produces
//	  @ApplicationScoped
//	  public EntityManagerFactory createEntityManagerFactory() {
//	    return Persistence.createEntityManagerFactory("bank");
//	  }
//
//	  public void close(@Disposes EntityManagerFactory entityManagerFactory) {
//	    entityManagerFactory.close();
//	  }
//
//	  @Produces
//	  @RequestScoped
//	  public EntityManager createEntityManager(EntityManagerFactory entityManagerFactory) {
//	    return entityManagerFactory.createEntityManager();
//	  }
//
//	  public void close(@Disposes EntityManager entityManager) {
//	    entityManager.close();
//	  }
//	}

//3. próbálkozás
//@ApplicationScoped
//public class EntityManagerProducer
//{
//    @PersistenceUnit
//    private EntityManagerFactory entityManagerFactory;
//
//    @Produces
//    @Default
//    @RequestScoped
//    public EntityManager create()
//    {
//        return this.entityManagerFactory.createEntityManager();
//    }
//
//    public void dispose(@Disposes @Default EntityManager entityManager)
//    {
//        if (entityManager.isOpen())
//        {
//            entityManager.close();
//        }
//    }
//}