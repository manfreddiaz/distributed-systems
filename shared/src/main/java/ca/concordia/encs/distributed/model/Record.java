package ca.concordia.encs.distributed.model;

import ca.concordia.encs.distributed.exception.AccessViolationException;
import ca.concordia.encs.distributed.exception.InvalidOperationException;
import ca.concordia.encs.distributed.security.Modifiable;
import ca.concordia.encs.distributed.util.ReflectiveFieldModifier;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.concurrent.locks.StampedLock;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Record implements Serializable {
	public String id;
	public String firstName;
	public String lastName;

	@Modifiable
	protected Location location; //TODO: check unclear requirement

	public Record() {
		super();
	}
	public Record(String id, String firstName, String lastName, Location location) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.location = location;
	}
	
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getId() {
		return id;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void edit(String fieldName, String newValue) throws InvalidOperationException, AccessViolationException {
		ReflectiveFieldModifier.modifyRecordField(this, fieldName, newValue);
	}
}
