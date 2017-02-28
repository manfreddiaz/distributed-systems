package ca.concordia.encs.distributed.model;

import ca.concordia.encs.distributed.security.Modifiable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
public class DoctorRecord extends Record implements Serializable {

	@Modifiable
	public String address;
	@Modifiable
	public String phone;
	@Modifiable
	public String specialization;

	public DoctorRecord(String id, String firstName, String lastName, Location location, String address, String phone, String specialization) {
		super(id, firstName, lastName, location);
		this.address = address;
		this.phone = phone;
		this.specialization = specialization;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
}
