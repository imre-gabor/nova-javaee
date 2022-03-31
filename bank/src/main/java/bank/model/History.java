package bank.model;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class History {

	@Id
	@GeneratedValue
	private long id;
	private String message;
	private OffsetDateTime ts;
	
	public History() {
	}
	
	public History(String message) {
		super();
		this.message = message;
		this.ts = OffsetDateTime.now();
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public OffsetDateTime getTs() {
		return ts;
	}
	public void setTs(OffsetDateTime ts) {
		this.ts = ts;
	}
	
}
