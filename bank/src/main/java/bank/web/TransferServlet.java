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
@WebServlet("/TransferServlet")
public class TransferServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB
	BankSessionBeanLocal bank;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int fromAccountId = Integer.parseInt(request.getParameter("from"));
		int toAccountId = Integer.parseInt(request.getParameter("to"));
		double amount = Double.parseDouble(request.getParameter("amount"));
		
//		try {
//			
//			bank.transfer(fromAccountId, toAccountId, amount);
//			request.setAttribute("resultOfTransfer", "Transfer successful");
//		} catch (Exception e) {
//			e.printStackTrace();
//			request.setAttribute("resultOfTransfer", "Error during transfer: "  + e.getMessage());
//		}
		
		
		try {
			
			bank.scheduleTransfer(fromAccountId, toAccountId, amount);
			request.setAttribute("resultOfTransfer", "Transfer scheduled successfully");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("resultOfTransfer", "Error at scheduling transfer: "  + e.getMessage());
		}
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}
