import java.util.*;
import java.io.*;

// todo dealing with the unexpected input, only the numeric

public class UserInterface {
    private boolean status;
    private Dijkstra<Airport> graph;

    public UserInterface() {
        status = true;
    }

    public void greetings() {
        refresh();
        System.out.println("Bonjour!");
        System.out.println("Please provide the address for the graph file so that we can set it up for you.");
    }

    public boolean loadGraph(Dijkstra<Airport> graphP) {
        graph = graphP;

        graph.clear();

        Scanner scanner = openInputFile();
        if (scanner != null) {

            loadFromFile(scanner, graph);

            return true;
        }
        return false;
    }

    private void loadFromFile(Scanner scanner, Dijkstra<Airport> graphP) {
        String portName1;
        String portName2;
        double distance;

        while(scanner.hasNext()) {
            portName1 = scanner.next();
            portName2 = scanner.next();
            distance = scanner.nextDouble();
            graphP.addEdge(new Airport(portName1), new Airport(portName2), distance);

        }
    }

    public void loadingFailed() {
        System.out.println("\nWould you like to try that again?");
    }

    private void displayMainMenu() {
        refresh();
        System.out.println("-\nMenu:\n");
        System.out.println("1. Add Edge");
        System.out.println("2. Remove Edge:");
        System.out.println("3. Undo Remove");
        System.out.println("4. Display Graph");
        System.out.println("5. Solve Graph");
        System.out.println("6. Write Graph to File");

        System.out.println("\n* Enter 0 to Change Graph *");
        System.out.println("* Enter -1 to QUIT *\n-\n");
    }

    public void takeOrder() {
        Scanner keyboard = new Scanner(System.in);

        displayMainMenu();

        System.out.println("\"May I take your order?\" George Clooney asks.\n");

        System.out.print("Item: ");
        while (!keyboard.hasNextInt()) {
            keyboard.next();
            System.out.println("\nThe input is not valid.");
            System.out.print("\nItem: ");
        }
        int item = keyboard.nextInt();

        switch (item) {
            case -1: offStatus();
                break;
            case 0:
                while (!loadGraph(graph)) { loadingFailed(); }
                break;
            case 1: addEdge();
                break;
            case 2: removeEdge();
                break;
            case 3: undo();
                break;
            case 4: displayGraph();
                break;
            case 5: solveBestRoute();
                break;
            case 6:
                graph.writeAdjListToFile();
                waitForENTER();
                break;
            default:
                displayMainMenu();
                System.out.println("\"There is not such thing on the menu!\" Georgie says with fury.");
                waitForENTER();
                takeOrder();
                break;
        }
    }

    private void offStatus() {
        status = false;
    }

    private void addEdge() {
        Scanner keyboard = new Scanner(System.in);

        System.out.print("\nSource airport: ");
        Airport source = new Airport(promptAirportStr());

        System.out.print("Destination airport: ");
        Airport dest = new Airport(promptAirportStr());

        System.out.print("Cost: ");
        double cost = keyboard.nextInt();

        graph.remove(source, dest);

        graph.addEdge(source, dest, cost);

        System.out.println("\nAdding complete.");

        waitForENTER();
    }

    private void removeEdge() {
        System.out.print("\nSource airport: ");
        Airport source = new Airport(promptAirportStr());

        System.out.print("Destination airport: ");
        Airport dest = new Airport(promptAirportStr());

        if (graph.remove(source, dest)) {
            System.out.println("\nRemoving complete.");
        }
        else {
            System.out.println("\nRemoving failed");
        }

        waitForENTER();
    }

    private void undo() {
        if (graph.deletedEdgeInFormOfPair.isEmpty()) {
            System.out.println("\nOops! No previous deleting action found.");
        }
        else {
            graph.undo();
        }
        waitForENTER();
    }

    private void displayGraph() {
        Scanner keyboard = new Scanner(System.in);

        refresh();

        System.out.println("-\nStyles:\n");
        System.out.println("1. Depth-First");
        System.out.println("2. Breadth-First");
        System.out.println("3. Adjacency List\n-\n");

        System.out.println("\"In which fashion do you prefer displaying the graph?\" George Clooney asks.\n");

        System.out.print("Item: ");
        while (!keyboard.hasNextInt()) {
            keyboard.next();
            System.out.println("\nThe input is not valid.");
            System.out.print("\nItem: ");
        }
        int item = keyboard.nextInt();

        switch (item) {
            case 1:  showGraph_Depth_First();
                break;
            case 2:  showGraph_Breadth_First();
                break;
            case 3:
                System.out.println();
                graph.showAdjTable();
                waitForENTER();
                break;
            default:
                displayGraph();
                waitForENTER();
                break;
        }
    }

    private void showGraph_Depth_First() {
        System.out.print("\nPreferred starting airport for traversal: ");
        Airport preferredStartingAirport = new Airport(promptAirportStr());

        boolean inSet = graph.vertexSet.containsKey(preferredStartingAirport);

        if (inSet) {
            System.out.println("\nDisplaying graph in a Depth-First Fashion: ");
            graph.depthFirstTraversal(preferredStartingAirport, new AirportVisitor());
        }
        else {
            System.out.println("\nStarting vertex does not exist.");
        }

        waitForENTER();
    }

    private void showGraph_Breadth_First() {
        System.out.print("\nPreferred starting airport for traversal: ");
        Airport preferredStartingAirport = new Airport(promptAirportStr());

        if (graph.vertexSet.containsKey(preferredStartingAirport)) {
            System.out.println("\nDisplaying graph in a Breadth_First Fashion: ");
            graph.breadthFirstTraversal(preferredStartingAirport, new AirportVisitor());
        }
        else {
            System.out.println("\nStarting vertex does not exist.");
        }

        waitForENTER();
    }

    private void solveBestRoute() {
        System.out.print("\nSource airport: ");
        Vertex<Airport> source = new Vertex<>(new Airport(promptAirportStr()));

        System.out.print("Destination airport: ");
        Vertex<Airport> dest = new Vertex<>(new Airport(promptAirportStr()));

        graph.unvisitVertices();

        Stack<Vertex<Airport>> bestRoute = graph.applyDijkstras(source, dest);

        if (bestRoute != null) {
            System.out.println("\nThe best route available is:");

            String temp = getRoute(bestRoute, source, dest);

            System.out.println(temp);

            System.out.println("\nWould you like to save it in to a text file? ");
            System.out.print("1 for yes and 0 for no: ");

            Scanner keyboard = new Scanner(System.in);
            int item = keyboard.nextInt();

            if (item != 0) {
                System.out.print("\nFilename: ");

                Scanner scanner = new Scanner(System.in);
                String filename = scanner.nextLine();

                String tempAddress = "/Users/mantinglin/Desktop/" + filename + ".txt";

                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(tempAddress), "utf-8"))) {
                    writer.write(temp);
                    System.out.println("\nWriting complete.");
                } catch (Exception e) {
                    System.out.println("Oops! This is super awkward.");
                }
            }
        }

        waitForENTER();
    }

    private String getRoute(Stack<Vertex<Airport>> bestRoute, Vertex<Airport> sourceVertex, Vertex<Airport> destVertex) {
        String routeStr = "";
        LinkedStack<Vertex<Airport>> stackForTrimming = new LinkedStack<>();

        // trim the bestRoute
        Vertex<Airport> tempVertex = bestRoute.pop();
        while (!tempVertex.data.equals(sourceVertex.data)) {
            stackForTrimming.push(tempVertex);
            tempVertex = bestRoute.pop();
        }
        stackForTrimming.push(tempVertex);
        // finish trimming

        tempVertex = stackForTrimming.pop();
        while (tempVertex != null) {
            routeStr += (tempVertex.data.toString() + ", " + tempVertex.weight + "\n");
            tempVertex = stackForTrimming.pop();
        }
        routeStr += (destVertex.data.toString() + ", " + graph.vertexSet.get(destVertex.data).weight);

        return routeStr;
    }

    private String promptAirportStr() {
        Scanner keyboard = new Scanner(System.in);
        return keyboard.nextLine();
    }

    public boolean getStatus() {
        return status;
    }

    public void adios() {
        System.out.println("\n\"Adios!\" George Clooney waves at you and smiles.\n");
    }

    private void waitForENTER() {
        System.out.print("\nENTER to continue...");
        try { System.in.read(); } catch (IOException e) { }
    }

    private void refresh() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static Scanner userScanner = new Scanner(System.in);

    private static Scanner openInputFile()
    {
        String filename;
        Scanner scanner=null;

        System.out.print("\nOpen Graph at: ");
        filename = userScanner.nextLine();
        File file= new File(filename);

        try{
            scanner = new Scanner(file);
        }// end try
        catch(FileNotFoundException fe){
            System.out.println("\nCan't open input file");
            return null; // array of 0 elements
        } // end catch
        return scanner;
    }

    public void tempGraphBuilder(Graph<Airport> graphP) {
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

        graphP.addEdge(airportA, airportB, 1);
        graphP.addEdge(airportA, airportC, 2);
        graphP.addEdge(airportA, airportD, 3);
        graphP.addEdge(airportA, airportE, 4);
        graphP.addEdge(airportA, airportF, 5);
        graphP.addEdge(airportA, airportG, 6);
        graphP.addEdge(airportA, airportH, 7);
        graphP.addEdge(airportA, airportI, 8);
        graphP.addEdge(airportA, airportJ, 9);

        graphP.addEdge(airportB, airportC, 11);
        graphP.addEdge(airportB, airportD, 12);
        graphP.addEdge(airportB, airportE, 13);
        graphP.addEdge(airportB, airportF, 14);
        graphP.addEdge(airportB, airportG, 15);
        graphP.addEdge(airportB, airportH, 16);
        graphP.addEdge(airportB, airportI, 17);
        graphP.addEdge(airportB, airportJ, 18);

        graphP.addEdge(airportC, airportD, 21);
        graphP.addEdge(airportC, airportE, 22);
        graphP.addEdge(airportC, airportF, 23);
        graphP.addEdge(airportC, airportG, 24);
        graphP.addEdge(airportC, airportH, 25);
        graphP.addEdge(airportC, airportI, 26);
        graphP.addEdge(airportC, airportJ, 27);

        graphP.addEdge(airportD, airportE, 31);
        graphP.addEdge(airportD, airportF, 32);
        graphP.addEdge(airportD, airportG, 33);
        graphP.addEdge(airportD, airportH, 34);
        graphP.addEdge(airportD, airportI, 35);
        graphP.addEdge(airportD, airportJ, 36);


        graphP.addEdge(airportE, airportF, 41);
        graphP.addEdge(airportE, airportG, 42);
        graphP.addEdge(airportE, airportH, 43);
        graphP.addEdge(airportE, airportI, 44);
        graphP.addEdge(airportE, airportJ, 45);


        graphP.addEdge(airportF, airportG, 51);
        graphP.addEdge(airportF, airportH, 52);
        graphP.addEdge(airportF, airportI, 53);
        graphP.addEdge(airportF, airportJ, 54);


        graphP.addEdge(airportG, airportH, 61);
        graphP.addEdge(airportG, airportI, 62);
        graphP.addEdge(airportG, airportJ, 63);


        graphP.addEdge(airportH, airportI, 71);
        graphP.addEdge(airportH, airportJ, 72);


        graphP.addEdge(airportI, airportJ, 81);

        graph = (Dijkstra<Airport>) graphP;
    }
}

/*
/Users/mantinglin/Downloads/TestData1.txt
------------------------
Adj List for LAX: ATL(1942.0) SJC(308.0) SFO(337.0) LAS(236.0)
Adj List for ATL: LAX(1942.0) SJC(2111.0) SFO(2134.0) LAS(1742.0)
Adj List for SJC: LAX(308.0) ATL(2111.0) SFO(30.0) LAS(385.0)
Adj List for SFO: LAX(337.0) ATL(2134.0) SJC(30.0) LAS(413.0)
Adj List for LAS: LAX(236.0) ATL(1742.0) SJC(385.0) SFO(413.0)

/Users/mantinglin/Downloads/TestData2.txt
------------------------
Adj List for ORD: PHX(1437.0) DFW(801.0) DEN(886.0) SEA(1715.0)
Adj List for PHX: ORD(1437.0) DFW(866.0) DEN(601.0) SEA(1107.0)
Adj List for DFW: ORD(801.0) PHX(866.0) DEN(641.0) SEA(1657.0)
Adj List for DEN: ORD(886.0) PHX(601.0) DFW(641.0) SEA(1022.0)
Adj List for SEA: ORD(1715.0) PHX(1107.0) DFW(1657.0) DEN(1022.0)

/Users/mantinglin/Downloads/TestData3.txt
------------------------
Adj List for MDW: SAN(1724.0) DCA(599.0) TPA(998.0) BWI(609.0) FLL(1168.0) HNL(4245.0) SLC(1255.0) IAD(576.0)
Adj List for SAN: MDW(1724.0) DCA(2270.0) TPA(2082.0) BWI(2290.0) FLL(2264.0) HNL(2610.0) SLC(626.0) IAD(2248.0)
Adj List for DCA: MDW(599.0) SAN(2270.0) TPA(815.0) BWI(29.0) FLL(901.0) HNL(4832.0) SLC(1846.0) IAD(23.0)
Adj List for TPA: MDW(998.0) SAN(2082.0) DCA(815.0) BWI(843.0) FLL(196.0) HNL(4683.0) SLC(1885.0) IAD(812.0)
Adj List for BWI: MDW(609.0) SAN(2290.0) DCA(29.0) TPA(843.0) FLL(927.0) HNL(4847.0) SLC(1859.0) IAD(45.0)
Adj List for FLL: MDW(1168.0) SAN(2264.0) DCA(901.0) TPA(196.0) BWI(927.0) HNL(4857.0) SLC(2081.0) IAD(902.0)
Adj List for HNL: MDW(4245.0) SAN(2610.0) DCA(4832.0) TPA(4683.0) BWI(4847.0) FLL(4857.0) SLC(2990.0) IAD(4808.0)
Adj List for SLC: MDW(1255.0) SAN(626.0) DCA(1846.0) TPA(1885.0) BWI(1859.0) FLL(2081.0) HNL(2990.0) IAD(1823.0)
Adj List for IAD: MDW(576.0) SAN(2248.0) DCA(23.0) TPA(812.0) BWI(45.0) FLL(902.0) HNL(4808.0) SLC(1823.0)
 */
