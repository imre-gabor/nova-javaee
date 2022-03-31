package bank.model;

import java.io.Serializable;
import javax.persistence.*;

import bank.service.BankException;

import java.util.Date;


/**
 * The persistent class for the ACCOUNT database table.
 * 
 */
@Entity
@NamedQuery(name="Account.findAll", query="SELECT a FROM Account a")
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int accountid;

	private double balance;

	@Temporal(TemporalType.DATE)
	private Date createdate;

	//bi-directional many-to-one association to Client
	@ManyToOne
	@JoinColumn(name="CLIENTID")
	private Client client;

	public Account() {
	}

	public int getAccountid() {
		return this.accountid;
	}

	public void setAccountid(int accountid) {
		this.accountid = accountid;
	}

	public double getBalance() {
		return this.balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	
	public void increase(double amount) {
		if(amount < 0)
			throw new IllegalArgumentException("amount cannot be negative");
		this.balance += amount;
	}
	
	public void decrease(double amount) throws BankException {
		if(amount < 0)
			throw new IllegalArgumentException("amount cannot be negative");
		
		if(amount > this.balance)
			throw new BankException("Balance too small.");
		
		this.balance -= amount;
	}

}