package ca.concordia.encs.distributed.security;

import ca.concordia.encs.distributed.model.Location;

public class User {
	private String id;
	private Location location;
	private Role role; //TODO: extend to a role list

	public User(String id, Location location, Role role) {
		this.id = id;
		this.location = location;
		this.role = role;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	@Override
	public String toString() {
		return String.format("%s%s", location.abbr(), id);
	}
	
}
