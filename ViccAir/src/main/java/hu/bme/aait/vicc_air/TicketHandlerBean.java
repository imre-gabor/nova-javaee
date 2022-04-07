package hu.bme.aait.vicc_air;

import hu.bme.aait.vicc_air.TicketOrderBean.Status;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;

@MessageDriven(mappedName = "java:global/jms/ViccAirRequest", activationConfig = {
		// @ActivationConfigProperty(propertyName = "acknowledgeMode",
		// propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:global/jms/ViccAirRequest") })
public class TicketHandlerBean implements MessageListener {

	@Inject
	private JMSContext ctx;

	@Resource(mappedName = "java:global/jms/ViccAirResponse")
	private Queue destination;

	@Override
	public void onMessage(Message message) {

		System.out.println("Got request:" + message.toString());
		if (message instanceof ObjectMessage) {
			try {
				ObjectMessage msg = (ObjectMessage) message;
				TicketOrderBean bean = (TicketOrderBean) msg.getObject();
				Thread.sleep(20000);

				Status status = bean.getSeats() > 10 ? Status.DECLINED
						: Status.CONFIRMED;
				bean.setStatus(status);

				ctx.createProducer().send(destination, bean);

			} catch (JMSException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
