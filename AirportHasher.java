package grp;

public class AirportHasher implements Hasher<Location> {
	public int hash(Location myAirport) {
		return (myAirport.getAirportName() != null) ? myAirport.getAirportName().hashCode() : 0;
	}
}
