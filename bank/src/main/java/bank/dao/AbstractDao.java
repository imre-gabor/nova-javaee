package bank.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

public abstract class AbstractDao<T, ID> {

	private Class<T> entityClass;
	private Class<ID> idClass;

	public AbstractDao(Class<T> entityClass, Class<ID> idClass) {
		super();
		this.entityClass = entityClass;
		this.idClass = idClass;
	}

	public abstract EntityManager em();
	
	public T create(T entity) {
		em().persist(entity);
		return entity;
	}
	
	public T update(T entity) {
		return em().merge(entity);
	}
	
	public Optional<T> findById(ID id){
		return Optional.ofNullable(em().find(entityClass, id));
	}
	
	public void delete(T entity) {
		em().remove(em().merge(entity));
	}
	
	public void deleteById(ID id) {
		CriteriaBuilder cb = em().getCriteriaBuilder();
		CriteriaDelete<T> criteriaDelete = cb.createCriteriaDelete(entityClass);
		Root<T> root = criteriaDelete.from(entityClass);
		
		EntityType<T> entity = em().getMetamodel().entity(entityClass);
		SingularAttribute<? super T, ID> idAttribute = entity.getId(idClass);
		criteriaDelete.where(cb.equal(root.get(idAttribute) , id));
	}
	
	
	public List<T> findAll(){
		CriteriaBuilder cb = em().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(entityClass);
		
		return em().createQuery(cq).getResultList();
	}
}
