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
import javax.jms.TextMessage;

import hu.bme.aait.vicc_air.TicketOrderBean;
import hu.bme.aait.vicc_air.TicketOrderBean.Status;

@MessageDriven(mappedName = "java:global/jms/ViccAirDFlights", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:global/jms/ViccAirDFlights") })
public class DeletedFlightsHandlerBean implements MessageListener {

	@EJB
	private OrderHandlerBean orderBean;

	@Override
	public void onMessage(Message message) {

		System.out.println("Got deleted flights message:" + message.toString());

		if (message instanceof TextMessage) {
			try {
				String flightId = message.getBody(String.class);
				
				orderBean.cancelFlight(flightId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
