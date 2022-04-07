package hu.bme.aait;

import java.util.List;

import hu.bme.aait.Orders.Status;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class OrderHandlerBean {

	public void createNewOrder(Orders orderData) {

		// TODO: persist orderData with PENDING status

		// TODO: send JMS msg to ViccAir
	}

	public List<Orders> getOrders() {
		// TODO: return a list of all orders
		return null;
	}

	public void updateOrderStatus(int orderId, Status status) {
		// TODO: update status of the specified order
	}
	
	public void cancelFlight(String flightId) {
		// TODO: update status to cancelled for all affected orders
	}
}
