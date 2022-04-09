package bank.repository;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class RepositoryFactory {

	public static <T> T createRepository(EntityManager em, Class<T> repositoryType) {
		RepositoryFactorySupport factory = new JpaRepositoryFactory(em);
	    return factory.getRepository(repositoryType);
	}
}
