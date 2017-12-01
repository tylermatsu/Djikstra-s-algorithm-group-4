package grp;

public class Location {
	private String airportName = "";
	
	public Location() {}
	
	public Location (String paramStr) {
		airportName = paramStr;
	}
	
	
	
	public void SetAirportName(String paramStr) {
		airportName = paramStr;
	}
	public String getAirportName() {
		return airportName;
	}
	
}
