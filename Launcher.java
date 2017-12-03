public class Launcher {
    public static void main(String[] args) {
        Dijkstra<Airport> graph = new Dijkstra<>();
        UserInterface UI = new UserInterface();

        UI.greetings();

//        while (!UI.loadGraph(graph)) {
//            UI.loadingFailed();
//        }

        UI.tempGraphBuilder(graph);

        do {
            UI.takeOrder();
        }
        while (UI.getStatus());

        UI.adios();
    }
}
