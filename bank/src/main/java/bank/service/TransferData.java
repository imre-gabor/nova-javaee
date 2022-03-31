package bank.service;

import java.io.Serializable;

public class TransferData implements Serializable {

	private int fromAccountId;
	private int toAccountId;
	private double amount;
	
	public TransferData(int fromAccountId, int toAccountId, double amount) {
		super();
		this.fromAccountId = fromAccountId;
		this.toAccountId = toAccountId;
		this.amount = amount;
	}

	public int getFromAccountId() {
		return fromAccountId;
	}

	public int getToAccountId() {
		return toAccountId;
	}

	public double getAmount() {
		return amount;
	}
	
}
