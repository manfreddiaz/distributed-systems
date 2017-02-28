package ca.concordia.encs.distributed.model;

import ca.concordia.encs.distributed.security.Modifiable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.Calendar;

@XmlAccessorType(XmlAccessType.FIELD)
public class NurseRecord extends Record implements Serializable {
	@Modifiable
	private Designation designation; //EXP: Enumeration instead of boolean for extensibility purposes.
	@Modifiable
	private Status status;
	@Modifiable
	private Calendar date; //EXP: Calendar instead of Date for more precision

	public NurseRecord(String id, String firstName, String lastName, Location location, Designation designation, Status status, Calendar date) {
		super(id, firstName, lastName, location);
		this.designation = designation;
		this.status = status;
		this.date = date;
	}

	public Designation getDesignation() {
		return designation;
	}
	public void setDesignation(Designation designation) {
		this.designation = designation;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
}
