package hu.bme.aait.vicc_air;

import java.io.Serializable;
import java.util.Date;

public class TicketOrderBean implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum Status {
		PENDING, CONFIRMED, DECLINED, CANCELLED
	}

	private int orderId;
	private String customername;
	private String flightId;
	private Date depart;
	private int seats;
	private Status status;

	public TicketOrderBean() {
	}

	public TicketOrderBean(int orderId, String customername, String flightId,
			Date depart, int seats) {
		this.orderId = orderId;
		this.customername = customername;
		this.flightId = flightId;
		this.depart = depart;
		this.seats = seats;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getFlightId() {
		return flightId;
	}

	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}

	public Date getDepart() {
		return depart;
	}

	public void setDepart(Date depart) {
		this.depart = depart;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
