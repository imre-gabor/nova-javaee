package bank.repository;

import java.util.List;

import javax.enterprise.context.Dependent;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import bank.model.Client;

@Dependent
public interface ClientRepository extends JpaRepository<Client, Integer>,
										JpaSpecificationExecutor<Client>,
										QuerydslPredicateExecutor<Client>{

	@EntityGraph(/*value = "Client.EGWithAccounts"*/ attributePaths = "accounts")
	public List<Client> findByClientidIn(List<Integer> clientids);
}
