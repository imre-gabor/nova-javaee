package bank.dao;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.google.common.collect.ImmutableMap;

import bank.model.History;

@Stateless
public class HistoryDao extends AbstractDao<History, Long> {

	@PersistenceContext
	private EntityManager em;

	public HistoryDao() {
		super(History.class, Long.class);
	}
	
	@Override
	public EntityManager em() {
		return em;
	}
	
	public List<History> findByTsBetween(OffsetDateTime from, OffsetDateTime to){
//		return em().createNamedQuery("History.findByTsBetween", History.class)
//				.setParameter("from", from)
//				.setParameter("to", to)
//				.getResultList();
		return findWithNamedQuery("History.findByTsBetween", ImmutableMap.of("from", from, "to", to));
		
	}
	
	public List<History> findTop5Today(){
		
		OffsetDateTime from = OffsetDateTime.now().truncatedTo(ChronoUnit.DAYS);
		OffsetDateTime to = from.plusDays(1);
		return findWithNamedQuery("History.findByTsBetween", 0, 5, ImmutableMap.of("from", from, "to", to));
	}

}
