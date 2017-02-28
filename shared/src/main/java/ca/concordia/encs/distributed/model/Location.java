package ca.concordia.encs.distributed.model;

public enum Location {
	Montreal("Montreal", "MTL", 1),
	Laval("Laval", "LVL", 2),
	DollardDesOrmeaux("Dollard des Ormeaux", "DDO", 3);
	
	private final String abbr;
	private final String fullName;
	private final Integer index;
	
	Location(String name, String abbr, Integer index) {
		this.fullName = name;
		this.abbr = abbr;
		this.index = index;
	}
	
	public String abbr() { return abbr; }
	public String fullName() { return fullName; }
	public Integer index() { return  index; }

	public static Location fromAbbreviation(String abbr) {
		Location locationAbbr = null;
		for(Location location : Location.values()) {
			if(location.abbr().equals(abbr)) {
				locationAbbr = location;
				break;
			}

		}
		if(locationAbbr == null) {
			throw new IllegalArgumentException();
		}
		return locationAbbr;
	}
}
