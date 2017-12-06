public class Airport {
    private String airportName = "";

    public Airport() {}

    public Airport (String paramStr) {
        airportName = paramStr;

        // todo (enquiry): to initialize the hash filed, ask for professor's opinion
        hashCode();
    }

    public void SetAirportName(String paramStr) {
        airportName = paramStr;
    }

    public String getAirportName() {
        return airportName;
    }

    @Override
    public String toString() {
        return airportName;
    }

    @Override
    public int hashCode() {
        return airportName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Airport)) return false;

        Airport airport = (Airport) o;

        return airportName != null ? airportName.equals(airport.airportName) : airport.airportName == null;
    }
}
