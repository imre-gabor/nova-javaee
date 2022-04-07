package hu.bme.aait;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;

import hu.bme.aait.vicc_air.TicketOrderBean;
import hu.bme.aait.vicc_air.TicketOrderBean.Status;

@MessageDriven(mappedName = "java:global/jms/ViccAirResponse", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:global/jms/ViccAirResponse") })
public class ViccAirResponseHandlerBean implements MessageListener {

	@EJB
	private OrderHandlerBean orderBean;

	@Override
	public void onMessage(Message message) {

		System.out.println("Got response:" + message.toString());

		if (message instanceof ObjectMessage) {
			try {
				TicketOrderBean ticketOrderBean = ((ObjectMessage)message).getBody(TicketOrderBean.class);
				
				orderBean.updateOrderStatus(ticketOrderBean.getOrderId(), convertStatus(ticketOrderBean.getStatus()));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	private hu.bme.aait.Orders.Status convertStatus(Status status) {
		return hu.bme.aait.Orders.Status.valueOf(status.name());
	}
}
