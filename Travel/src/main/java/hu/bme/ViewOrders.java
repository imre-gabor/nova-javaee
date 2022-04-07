package hu.bme;

import hu.bme.aait.OrderHandlerBean;
import hu.bme.aait.Orders;

import java.io.*;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ViewOrders")
public class ViewOrders extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	private OrderHandlerBean orderHandlerBean;

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		List<Orders> orders = orderHandlerBean.getOrders();
		request.setAttribute("ordersList", orders);
		request.getRequestDispatcher("orders.jsp").forward(request, response);

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