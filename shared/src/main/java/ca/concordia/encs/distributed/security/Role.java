package ca.concordia.encs.distributed.security;

public class Role {
	private String authority;

	public Role(String authority) {
		super();
		this.authority = authority;
	}

	public String getAuthority() {
		return authority;
	}
}
