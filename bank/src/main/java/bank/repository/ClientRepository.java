package bank.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.cdi.Eager;

import bank.model.Client;

//@Eager
//@ApplicationScoped
//@Dependent
public interface ClientRepository extends JpaRepository<Client, Integer>,
										JpaSpecificationExecutor<Client>,
										QuerydslPredicateExecutor<Client>{

	@QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
	@EntityGraph(/*value = "Client.EGWithAccounts"*/ attributePaths = "accounts")
	public List<Client> findByClientidIn(List<Integer> clientids);
}
