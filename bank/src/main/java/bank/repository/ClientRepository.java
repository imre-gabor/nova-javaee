package bank.repository;

import javax.enterprise.context.Dependent;

import org.springframework.data.jpa.repository.JpaRepository;

import bank.model.Client;

@Dependent
public interface ClientRepository extends JpaRepository<Client, Integer>{

}
