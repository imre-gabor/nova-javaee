package bank.web;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import bank.model.Client;
import bank.repository.ClientRepository;
import bank.service.BankSessionBeanLocal;

/**
 * Servlet implementation class CreateClientServlet
 */
@WebServlet("/SearchClientServlet")
public class SearchClientServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB
	BankSessionBeanLocal bank;
	
//	@EJB
//	ClientDao clientDao;
	
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String name = request.getParameter("name");
		name = StringUtils.stripToNull(name);
		String address = request.getParameter("address");
		address = StringUtils.stripToNull(address);
		String clientIdString = request.getParameter("clientid");
		int clientid = StringUtils.isEmpty(clientIdString) ? 0 : Integer.parseInt(clientIdString);
		
		try {
			Client example = new Client(name, address);
			example.setClientid(clientid);
			List<Client> result = bank.searchClients(example);
			
			result.forEach( c-> c.getAccounts().forEach(System.out::println));
			
			/*1. verzió: default fetch, semmit nem állítunk 
			 * --> by default lazy fetch, de itt már lecsatolt állapotúak a client példányok 
			 * --> LazyInitException
			 */
			
			/*2. verzió: fetch=EAGER a @OneToMany paramétere --> LazyInit megoldva
			 * De! 1 SELECT a kliensek megtalálására + N SELECT minden klienshez az accountok betöltése
			 */
			
			/*3. verzió: @Fetch(FetchMode.JOIN): ha bent maradt a fetch=EAGER, továbbra is 1+N select
			 * de ha levesszük a fetch=EAGER-t, akkor is, mert custom query-ből keresünk, nem findById-val
			 * Custom query esetén ignorálja, a query-be kell ilyenkor left join fetch-et írni
			 */
			
			/*
			 * 4. verzió: @Fetch(FetchMode.JOIN), findById esetén
			 */
//			clientDao.findById(1).get().getAccounts().forEach(System.out::println);
				
			/*5. verzió: @Fetch(FetchMode.SUBSELECT), és nincs fetch=EAGER --> LazyInit
			 */

			/*6. verzió: @Fetch(FetchMode.SUBSELECT), és van fetch=EAGER --> 1 select + 1 select, amelyben subselectben szerepel az eredeti select 
			 */
			
			/*
			 * 7. verzió: left join fetch a queryben --> 1 SELECT, benne join
			 */

			/*
			 * 8. verzió: entity graph hintként átadva, fetchgraph-ként vagy loadgraphként 1 select, benne --> join
			 * fetchgraph/loadgraph különbség akkor látszana, ha más kapcsolat is lenne, am nincs a graph-ban
			 */
			
			/*
			 * 9. verzió: entity graph (vagy fetch join a queryben) + lapozás (query.setMaxResults)ű
			 * warning: lapozás csak in-memory, valójában az összes eredmény betöltődik a DB-ből
			 */
			
			/*
			 * 10. verzió: 9. verzió javítása, a findByExampleWithPaging-ben: 
			 * 1 select a kliensek megtalálására, lapozással
			 * + 1 select az accountok fetchelésére, de csak az előzőben megtalált kliensekhez
			 */
			
			request.setAttribute("clients", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}
