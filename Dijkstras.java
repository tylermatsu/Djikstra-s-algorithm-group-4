import java.util.*;
import java.util.Map.Entry;
import java.util.Comparator;

public class Dijkstra<E> extends Graph<E>
{
    private PriorityQueue< Vertex<E> > vertexPriorityQueue; // will add vertexes from largest to smallest weight

    public Dijkstra ()
    {
        vertexPriorityQueue = new PriorityQueue<>(vertexComparator);
    }

    // algorithms
    public Stack<Vertex<E>> applyDijkstras(Vertex<E> source, Vertex<E> dest)
    {
        unvisitVertices();

        Iterator<Entry<E, Pair<Vertex<E>, Double>>> iter ;
        Entry<E, Pair<Vertex<E>, Double>> entry;
        Pair<Vertex<E>, Double> pair;

        if (vertexSet.containsKey(source.data) && vertexSet.containsKey(dest.data)) {
            Vertex<E> destVertex = vertexSet.get(dest.data);
            Vertex<E> currVertex;

            Vertex<E> nextVertex = vertexSet.get(source.data);

            nextVertex.setWeight(0.0);

            do {
                // update next vertex to current
                currVertex = nextVertex;

                // visit the current vertex
                currVertex.visit();

                if (destVertex.isVisited()) {
                    break;
                }

                // iterate the adj. vertexes of currVertex, if has any
                iter = currVertex.adjList.entrySet().iterator();
                while (iter.hasNext()) {
                    entry = iter.next();
                    pair = entry.getValue();

                    // if the adj. vertex is not visited
                    if (!pair.first.isVisited()) {

                        // if the weight can be updated, smaller
                        if ((currVertex.getWeight() + pair.second) < pair.first.getWeight()) {

                            // if a shorter path is available, record it in vertex
                            recordTheShortestPathToVertex(currVertex, pair.first);

                            //update
                            pair.first.setWeight(currVertex.getWeight() + pair.second);

                            // add iterated vertex in to priority queue, and special case
                            if (vertexPriorityQueue.contains(pair.first)) {
                                vertexPriorityQueue.remove(pair.first);
                            }
                            vertexPriorityQueue.add(pair.first); // add it on
                        }
                    }
                }

                // if has next vertex to go
                if (!vertexPriorityQueue.isEmpty()) {
                    nextVertex = vertexPriorityQueue.remove();
                }
            }
            while (!destVertex.isVisited());

            if (destVertex.isVisited()) {
                return destVertex.previousVertexesInShortestPath;
            }
            else {
                return null;
            }

        } else {
            System.out.println("\nPlease check your source and destination, merci.");
            return null;
        }
    }

    private void recordTheShortestPathToVertex(Vertex<E> currVertex, Vertex<E> adjVertex) {
        Iterator<Vertex<E>> iter;
        Vertex<E> tempVertex;

        iter = currVertex.previousVertexesInShortestPath.iterator();
        while (iter.hasNext()) {
            tempVertex = iter.next();
            adjVertex.previousVertexesInShortestPath.push(tempVertex);
        }
        adjVertex.previousVertexesInShortestPath.push(currVertex);
    }

    // to not to make too much change in vertex class, override the comparator for priority queue
    // https://stackoverflow.com/questions/24706304/attempting-to-override-existing-comparator-for-priorityqueue-in-java
    private Comparator<Vertex<E>> vertexComparator = new Comparator<Vertex<E>>() {

        @Override
        public int compare(Vertex<E> left, Vertex<E> right) {
            if (left.getWeight() < right.getWeight()) { return -1; }
            if (left.getWeight() > right.getWeight()) { return 1; }
            return 0;
        }
    };
}
