package hu.bme.aait;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ @NamedQuery(name = "Orders.findByFlightId", query = "SELECT o FROM Orders o WHERE o.flightId = :flightId"), })
public class Orders implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum Status {
		PENDING, CONFIRMED, DECLINED, CANCELLED
	}

	@Id
	@GeneratedValue
	private int orderId;
	private String customername;
	private String flightId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date depart;
	@Temporal(TemporalType.TIMESTAMP)
	private Date arrive;
	private int seats;
	private Status status;

	public Orders() {
	}

	public Orders(int orderId) {
		this.orderId = orderId;
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

	public Date getArrive() {
		return arrive;
	}

	public void setArrive(Date arrive) {
		this.arrive = arrive;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + orderId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Orders other = (Orders) obj;
		if (orderId != other.orderId)
			return false;
		return true;
	}
}
