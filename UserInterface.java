import java.util.*;
import java.io.*;

// todo dealing with the unexpected input, not only the numbers

public class UserInterface {
    private boolean status;
    private Dijkstra<Airport> graph;

    public UserInterface() {
        status = true;
    }

    public void greetings() {
        System.out.println("Hi!");
        System.out.println("Before the stimulation, please provide the address for the graph file.\n");
    }

    public boolean loadGraph(Dijkstra<Airport> graphP) {
        graph = graphP;

        Scanner scanner = openInputFile();
        if (scanner != null) {

            loadFromFile(scanner, graph);

            return true;
        }
        return false;
    }

    private void loadFromFile(Scanner scanner, Dijkstra<Airport> graphP) {

        // todo: Tyler's function here, for loading the graph, which takes a
        //       scanner and a graph prepare to load as arguments

    }

    public void loadingFailed() {
        System.out.println("Would you like to try that again?\n-");
    }

    private void displayMainMenu() {
        refresh();
        System.out.println("-\nMenu:\n");
        System.out.println("1. Add Edge");
        System.out.println("2. Remove Edge:");
        System.out.println("3. Undo remove: (needs attention)");
        System.out.println("4. Display Graph");
        System.out.println("5. Solve Graph");
        System.out.println("6. Write Graph to File");
        System.out.println("\n* Enter 0 to QUIT *\n-\n");
    }

    public void takeOrder() {
        Scanner keyboard = new Scanner(System.in);

        displayMainMenu();

        System.out.println("\"May I take your order?\" George Clooney asks.\n");

        System.out.print("Item: ");
        int item = keyboard.nextInt();

        switch (item) {
            case 0: offStatus();
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

        // todo (enquiry): do we need to count the remove from the addEdge function
        // when we write undo function
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
        if (graph.deletedEdgeStack.isEmpty()) {
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
        int item = keyboard.nextInt();

        switch (item) {
            case 1:  showGraph_Depth_First();
                break;
            case 2:  showGraph_Breadth_First();
                break;
            case 3:
                graph.showAdjTable();
                waitForENTER();
                break;
            default:
                System.out.println("action needed for Man");
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
            printRoute(bestRoute, source, dest);
        }

        waitForENTER();
    }

    private void printRoute(Stack<Vertex<Airport>> bestRoute, Vertex<Airport> sourceVertex, Vertex<Airport> destVertex) {
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
            System.out.println(tempVertex.data.toString() + ", " + tempVertex.weight);
            tempVertex = stackForTrimming.pop();
        }
        System.out.println(destVertex.data.toString() + ", " + graph.vertexSet.get(destVertex.data).weight);
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

        System.out.print("Open Graph at: ");
        filename = userScanner.nextLine();
        File file= new File(filename);

        try{
            scanner = new Scanner(file);
        }// end try
        catch(FileNotFoundException fe){
            System.out.println("-\nCan't open input file");
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
