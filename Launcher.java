public class Launcher {
    public static void main(String[] args) {
        Graph<Airport> graph = new Graph<>();

        tempGraphBuilder(graph);

        graph.showAdjTable();
    }

    public static void tempGraphBuilder(Graph<Airport> graph) {
        Airport airportA = new Airport("AAA");
        Airport airportB = new Airport("BBB");
        Airport airportC = new Airport("CCC");
        Airport airportD = new Airport("DDD");
        Airport airportE = new Airport("EEE");
        Airport airportF = new Airport("FFF");
        Airport airportG = new Airport("GGG");
        Airport airportH = new Airport("HHH");
        Airport airportI = new Airport("III");
        Airport airportJ = new Airport("JJJ");

        graph.addEdge(airportA, airportB, 1);
        graph.addEdge(airportA, airportC, 2);
        graph.addEdge(airportA, airportD, 3);
        graph.addEdge(airportA, airportE, 4);
        graph.addEdge(airportA, airportF, 5);
        graph.addEdge(airportA, airportG, 6);
        graph.addEdge(airportA, airportH, 7);
        graph.addEdge(airportA, airportI, 8);
        graph.addEdge(airportA, airportJ, 9);

        graph.addEdge(airportB, airportC, 11);
        graph.addEdge(airportB, airportD, 12);
        graph.addEdge(airportB, airportE, 13);
        graph.addEdge(airportB, airportF, 14);
        graph.addEdge(airportB, airportG, 15);
        graph.addEdge(airportB, airportH, 16);
        graph.addEdge(airportB, airportI, 17);
        graph.addEdge(airportB, airportJ, 18);

        graph.addEdge(airportC, airportD, 21);
        graph.addEdge(airportC, airportE, 22);
        graph.addEdge(airportC, airportF, 23);
        graph.addEdge(airportC, airportG, 24);
        graph.addEdge(airportC, airportH, 25);
        graph.addEdge(airportC, airportI, 26);
        graph.addEdge(airportC, airportJ, 27);

        graph.addEdge(airportD, airportE, 31);
        graph.addEdge(airportD, airportF, 32);
        graph.addEdge(airportD, airportG, 33);
        graph.addEdge(airportD, airportH, 34);
        graph.addEdge(airportD, airportI, 35);
        graph.addEdge(airportD, airportJ, 36);


        graph.addEdge(airportE, airportF, 41);
        graph.addEdge(airportE, airportG, 42);
        graph.addEdge(airportE, airportH, 43);
        graph.addEdge(airportE, airportI, 44);
        graph.addEdge(airportE, airportJ, 45);


        graph.addEdge(airportF, airportG, 51);
        graph.addEdge(airportF, airportH, 52);
        graph.addEdge(airportF, airportI, 53);
        graph.addEdge(airportF, airportJ, 54);


        graph.addEdge(airportG, airportH, 61);
        graph.addEdge(airportG, airportI, 62);
        graph.addEdge(airportG, airportJ, 63);


        graph.addEdge(airportH, airportI, 71);
        graph.addEdge(airportH, airportJ, 72);


        graph.addEdge(airportI, airportJ, 81);
    }
}

/*
------------------------
Adj List for FFF: BBB(14.0) GGG(51.0) III(53.0) AAA(5.0) DDD(32.0) JJJ(54.0) EEE(41.0) CCC(23.0) HHH(52.0)
Adj List for BBB: FFF(14.0) GGG(15.0) III(17.0) AAA(1.0) DDD(12.0) JJJ(18.0) EEE(13.0) CCC(11.0) HHH(16.0)
Adj List for GGG: FFF(51.0) BBB(15.0) III(62.0) AAA(6.0) DDD(33.0) JJJ(63.0) EEE(42.0) CCC(24.0) HHH(61.0)
Adj List for III: FFF(53.0) BBB(17.0) GGG(62.0) AAA(8.0) DDD(35.0) JJJ(81.0) EEE(44.0) CCC(26.0) HHH(71.0)
Adj List for AAA: FFF(5.0) BBB(1.0) GGG(6.0) III(8.0) DDD(3.0) JJJ(9.0) EEE(4.0) CCC(2.0) HHH(7.0)
Adj List for DDD: FFF(32.0) BBB(12.0) GGG(33.0) III(35.0) AAA(3.0) JJJ(36.0) EEE(31.0) CCC(21.0) HHH(34.0)
Adj List for JJJ: FFF(54.0) BBB(18.0) GGG(63.0) III(81.0) AAA(9.0) DDD(36.0) EEE(45.0) CCC(27.0) HHH(72.0)
Adj List for EEE: FFF(41.0) BBB(13.0) GGG(42.0) III(44.0) AAA(4.0) DDD(31.0) JJJ(45.0) CCC(22.0) HHH(43.0)
Adj List for CCC: FFF(23.0) BBB(11.0) GGG(24.0) III(26.0) AAA(2.0) DDD(21.0) JJJ(27.0) EEE(22.0) HHH(25.0)
Adj List for HHH: FFF(52.0) BBB(16.0) GGG(61.0) III(71.0) AAA(7.0) DDD(34.0) JJJ(72.0) EEE(43.0) CCC(25.0)
 */