package bank.repository;

import org.springframework.data.jpa.domain.Specification;

import bank.model.Client;
import bank.model.Client_;

public class ClientSpecifications {

	public static Specification<Client> isClientIdEqual(int clientid) {
		return (root, cq, cb) -> cb.equal(root.get(Client_.clientid), clientid);
	}
	
	public static Specification<Client> nameStartsWith(String name) {
		return (root, cq, cb) -> cb.like(root.get(Client_.name), name + "%");
	}

	public static Specification<Client> addressContains(String address) {
		return (root, cq, cb) -> cb.like(root.get(Client_.address), "%" + address + "%");
	}
}
