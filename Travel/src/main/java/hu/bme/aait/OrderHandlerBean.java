package hu.bme.aait;

import java.util.List;

import hu.bme.aait.Orders.Status;
import hu.bme.aait.vicc_air.TicketOrderBean;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class OrderHandlerBean {

	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private JMSContext ctx;

	@Resource(mappedName = "java:global/jms/ViccAirRequest")
	private Queue viccairRequestQ;
	
	
	public void createNewOrder(Orders orderData) {

		// TODO: persist orderData with PENDING status
		orderData.setStatus(Status.PENDING);
		em.persist(orderData);
		
		// TODO: send JMS msg to ViccAir
		
		TicketOrderBean ticket = new TicketOrderBean(
				orderData.getOrderId(), 
				orderData.getCustomername(), 
				orderData.getFlightId(), 
				orderData.getDepart(), 
				orderData.getSeats());
		
		ctx.createProducer().send(viccairRequestQ, ticket);
	}

	
	public List<Orders> getOrders() {
		return em.createQuery("SELECT o FROM Orders o", Orders.class).getResultList();
	}

	public void updateOrderStatus(int orderId, Status status) {
		em.find(Orders.class, orderId).setStatus(status);
	}
	
	public void cancelFlight(String flightId) {
		// TODO: update status to cancelled for all affected orders
	}
}
