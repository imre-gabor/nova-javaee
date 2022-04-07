package hu.bme.aait.vicc_air;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DeleteFlight")
public class DeleteFlight extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private JMSContext ctx;

	@Resource(mappedName = "java:global/jms/ViccAirDFlights")
	private Topic destination;

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter();) {
			String flight = request.getParameter("flight");
			if ((flight != null) && (flight.length() != 0)) {
				ctx.createProducer().send(destination, flight);
				out.println("Notification about the cancellation of flight "
						+ flight + " has been sent to our partners.");
			} else {
				out.println("No flight specified.");
			}
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
}
