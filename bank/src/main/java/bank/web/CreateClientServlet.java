package bank.web;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bank.model.Client;
import bank.service.BankSessionBeanLocal;

/**
 * Servlet implementation class CreateClientServlet
 */
@WebServlet("/CreateClientServlet")
public class CreateClientServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB
	BankSessionBeanLocal bank;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		
		try {
			Client client = new Client(name, address);
			bank.createClient(client);
			request.setAttribute("resultOfClientCreation", "Client successfully created with id "  + client.getClientid());
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("resultOfClientCreation", "Client not created: "  + e.getMessage());
		}
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}
