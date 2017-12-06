public class Airport {
    private String airportName = "";

    public Airport() {}

    public Airport (String paramStr) {
        airportName = paramStr;
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
    
    // todo: hasher needed
    
}

//
