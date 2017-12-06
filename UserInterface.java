import java.util.*;
import java.io.*;

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
            System.out.print("\n1 for YES and 0 for NO: ");

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
}

/*

/Users/mantinglin/Downloads/TestData3.txt

 */
