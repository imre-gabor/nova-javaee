package bank.web;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bank.model.Account;
import bank.service.BankSessionBeanLocal;

/**
 * Servlet implementation class CreateClientServlet
 */
@WebServlet("/CreateClientServlet")
public class CreateAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB
	BankSessionBeanLocal bank;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int clientId = Integer.parseInt(request.getParameter("clientId"));
		double balance = Double.parseDouble(request.getParameter("balance"));
		
		try {
			Account account = new Account();
			account.setBalance(balance);
			bank.createAccountForClient(account, clientId);
			request.setAttribute("resultOfAccountCreation", "Account successfully created with id "  + account.getAccountid());
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("resultOfAccountCreation", "Account not created: "  + e.getMessage());
		}
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}
