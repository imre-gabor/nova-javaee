package hu.bme;

import hu.bme.aait.OrderHandlerBean;
import hu.bme.aait.Orders;
import hu.bme.aait.Orders.Status;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/NewOrder")
public class NewOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	private OrderHandlerBean orderHandlerBean;

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		try (PrintWriter out = response.getWriter();) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy.mm.dd");
			Orders dataBean = new Orders();
			dataBean.setArrive(df.parse(request.getParameter("arrive")));
			dataBean.setCustomername(request.getParameter("customername"));
			dataBean.setDepart(df.parse(request.getParameter("depart")));
			dataBean.setFlightId(request.getParameter("flightId"));
			dataBean.setSeats(Integer.valueOf(request.getParameter("seat")));
			dataBean.setStatus(Status.PENDING);
			orderHandlerBean.createNewOrder(dataBean);
			request.getRequestDispatcher("index.jsp")
					.forward(request, response);
		} catch (ParseException e) {
			e.printStackTrace();
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