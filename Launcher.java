public class Launcher {
    public static void main(String[] args) {
        Dijkstra<Airport> graph = new Dijkstra<>();
        UserInterface UI = new UserInterface();

        UI.greetings();

        while (!UI.loadGraph(graph)) {
            UI.loadingFailed();
        }

        do {
            UI.takeOrder();
        }
        while (UI.getStatus());

        UI.adios();
    }
}
