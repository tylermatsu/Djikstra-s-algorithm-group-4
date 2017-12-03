import java.util.*;
import java.io.*;

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

    public void loadingFailed() {
        System.out.println("Would you like to try that again?\n-");
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

    public void takeOrder() {
        Scanner keyboard = new Scanner(System.in);

        displayMainMenu();

        System.out.println("\"May I take your order?\" George Clooney asks.\n");

        System.out.print("Item: ");
        int item = keyboard.nextInt();

        switch (item) {
            case 0: offStatus();
                break;
            case 1:  addEdge();
                break;
            case 2:  removeEdge();
                break;
            case 3:  graph.undo();
                break;
            case 4:  graph.showAdjTable();
                break;
            case 5:  solveBestRoute();
                break;
            case 6:  writeToFile();
                break;
            default:
                displayMainMenu();
                System.out.println("\n\"There is not such thing on the menu!\" Georgie says with fury.");
                waitForENTER();
                takeOrder();
                break;
        }
    }

    private void displayMainMenu() {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("-\nMenu:\n");
        System.out.println("1. Add Edge");
        System.out.println("2. Remove Edge:");
        System.out.println("3. Undo remove:");
        System.out.println("4. Display Graph");
        System.out.println("5. Solve Graph");
        System.out.println("6. Write Graph to File");
        System.out.println("\n* Enter 0 to QUIT *\n-\n");
    }

    private void offStatus() {
        status = false;
    }

    public boolean getStatus() {
        return status;
    }

    private void waitForENTER() {
        System.out.print("\nENTER to continue...");
        try { System.in.read(); } catch (IOException e) { }
    }

    public void adios() {
        System.out.println("\n\"Adios!\" George Clooney waves at you and smiles.\n");
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

    private static void tempGraphBuilder(Graph<Airport> graph) {
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
