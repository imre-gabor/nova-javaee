package bank.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

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

	public List<Client> findByExample(Client example) {
		int clientid = example.getClientid();
		String address = example.getAddress();
		String name = example.getName();
		
		CriteriaBuilder cb = getCriteriaBuilder();
		CriteriaQuery<Client> cq = cb.createQuery(Client.class);
		Root<Client> root = cq.from(Client.class);
		cq.select(root);
		
		List<Predicate> predicates = new ArrayList<>();
		if(clientid > 0) {
			predicates.add(cb.equal(root.get("clientid"), clientid));
		}
		
		if(StringUtils.isNotEmpty(address)) {
			predicates.add(cb.like(root.get("address"), "%" + address + "%"));
		}
		
		if(StringUtils.isNotEmpty(name)) {
			predicates.add(cb.like(root.get("name"), name + "%"));
		}

		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		
		return em.createQuery(cq).getResultList();
	}

}
