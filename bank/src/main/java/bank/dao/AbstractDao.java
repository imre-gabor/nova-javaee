package bank.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
		CriteriaBuilder cb = getCriteriaBuilder();
		CriteriaDelete<T> criteriaDelete = cb.createCriteriaDelete(entityClass);
		Root<T> root = criteriaDelete.from(entityClass);
		
		EntityType<T> entity = em().getMetamodel().entity(entityClass);
		SingularAttribute<? super T, ID> idAttribute = entity.getId(idClass);
		criteriaDelete.where(cb.equal(root.get(idAttribute) , id));
		
		em().createQuery(criteriaDelete).executeUpdate();
	}
	
	
	public List<T> findAll(){
		CriteriaBuilder cb = getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(entityClass);
		cq.from(entityClass);
		
		return em().createQuery(cq).getResultList();
	}

	public CriteriaBuilder getCriteriaBuilder() {
		CriteriaBuilder cb = em().getCriteriaBuilder();
		return cb;
	}

	public void deleteAll() {
		CriteriaBuilder cb = getCriteriaBuilder();
		CriteriaDelete<T> criteriaDelete = cb.createCriteriaDelete(entityClass);
		criteriaDelete.from(entityClass);
		em().createQuery(criteriaDelete).executeUpdate();
	}
	
	public List<T> findWithNamedQuery(String queryName, Integer firstResult, Integer maxResults, Map<String, Object> parameters){
		
		TypedQuery<T> query = em().createNamedQuery(queryName, entityClass);
		query.setFirstResult(firstResult == null ? 0 : firstResult);
		if(maxResults != null)
			query.setMaxResults(maxResults);
		for (Map.Entry<String, Object> par : parameters.entrySet()) {
			query.setParameter(par.getKey(), par.getValue());
		}
		return query.getResultList();
	}
	
	
	public List<T> findWithJpql(String jpql, Integer firstResult, Integer maxResults, Map<String, Object> parameters){
		
		TypedQuery<T> query = em().createQuery(jpql, entityClass);
		query.setFirstResult(firstResult == null ? 0 : firstResult);
		if(maxResults != null)
			query.setMaxResults(maxResults);
		for (Map.Entry<String, Object> par : parameters.entrySet()) {
			query.setParameter(par.getKey(), par.getValue());
		}
		return query.getResultList();
	}
	
	public List<T> findWithJpql(String jpql, Map<String, Object> parameters){
		return findWithJpql(jpql, null, null, parameters);
	}
	
	public List<T> findWithNamedQuery(String queryName, Map<String, Object> parameters){
		return findWithNamedQuery(queryName, null, null, parameters);
	}
	
	public <S> List<S> findWithNativeQuery(Class<S> resultClass, String sql, Map<String, Object> parameters){
		
		Query query = em().createNativeQuery(sql);
		for (Map.Entry<String, Object> par : parameters.entrySet()) {
			query.setParameter(par.getKey(), par.getValue());
		}
		return query.getResultList();
	}
}
