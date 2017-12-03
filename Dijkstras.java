import java.util.*;
import java.util.Map.Entry;
import java.util.Comparator;
import java.io.*;

public class Dijkstras<E> extends Graph<E>
{
    private PriorityQueue< Vertex<E> > vertexPriorityQueue; // will add vertexes from largest to smallest weight
    private Stack< Vertex<E> > visitedRouteStack; // track the route
    private ArrayList< Edge<E> > theGrandRouteEdgeList; // the route

    void saveToFile(String graphStr, String filename) {
        // todo: address will be updated accordingly to the lab computer or something
        String tempAddress = "/Users/mantinglin/Desktop/" + filename + ".txt";

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(tempAddress), "utf-8"))) {
            writer.write(graphStr);
        } catch (Exception e) {
            System.out.println("Oops! This is super awkward.");
        }
    }

    // getGraph_AdjList:
    // return a string contains all the AdjLists of its vertexes, ready to
    // write to file
    String getGraph_AdjList() {
        Iterator<Entry<E, Vertex<E>>> iter;
        String graphStr = "";

        iter = vertexSet.entrySet().iterator();
        while( iter.hasNext() )
        {
            graphStr += getAdjListInVertex( iter.next().getValue() ) + "\n";
        }

        return graphStr;
    }

    // todo (enquriy): should be in Vertex class

    // getAdjListInVertex:
    // return a string contains the AdjLists of the vertex parameter
    String getAdjListInVertex(Vertex<E> v)
    {
        Iterator<Entry<E, Pair<Vertex<E>, Double>>> iter ;
        Entry<E, Pair<Vertex<E>, Double>> entry;
        Pair<Vertex<E>, Double> pair;
        String vertexStr;

        vertexStr = "Adj List for " + v.data + ": ";
        iter = v.adjList.entrySet().iterator();
        while( iter.hasNext() )
        {
            entry = iter.next();
            pair = entry.getValue();
            vertexStr += pair.first.data + "("
                    + String.format("%3.1f", pair.second)
                    + ") ";
        }

        return vertexStr;
    }

    public Dijkstras ()
    {
        vertexPriorityQueue = new PriorityQueue<>(vertexComparator);
        visitedRouteStack = new Stack<>();
        theGrandRouteEdgeList = new ArrayList<>();
    }

    // algorithms
    public ArrayList< Edge<E> > applyDijkstras(Vertex<E> source, Vertex<E> dest)
    {
        Iterator<Entry<E, Pair<Vertex<E>, Double>>> iter ;
        Entry<E, Pair<Vertex<E>, Double>> entry;
        Pair<Vertex<E>, Double> pair;
        Vertex<E> vertex;

        /* @todo: write hashCode() for our E object, that's how we can locate the incoming object
                  which contains the E object from the vertexSet, because the vertexSet hash the E object
                  during storing */

        // @HELLO: we have to make sure that our data set is a "perfect channel", which means if two vertexes exist
        // in our data set, there must be way to get from a to b, beside of the direct road, because like we
        // discussed before, the direct route may not be available.

        // if we have both the source and the dest
        if (vertexSet.containsKey(source.data) && vertexSet.containsKey(dest.data)) {
            Vertex<E> sourceVertex = vertexSet.get(source.data);

            // set the the source's weight to 0
            sourceVertex.setWeight(0.0);

            // add a useless beginning vertex into our route stack for the convenience of the future route counting
            visitedRouteStack.add(new Vertex<>());

            // add it to queue, lets begin
            vertexPriorityQueue.add(sourceVertex);

            do {
                // get our least weighed vertex on graph (in the first loop, this will be the source)
                // this will not have a nullptr exception, hopefully
                vertex = vertexPriorityQueue.remove();

                // 1. visit the next least weighed vertex
                // 2. save the route from previous vertex to current vertex
                // 3. same current vertex
                vertex.visit();
                theGrandRouteEdgeList.add(new Edge<>(visitedRouteStack.peek(), vertex, (vertex.adjList.get(visitedRouteStack.peek().data)).second));
                visitedRouteStack.push(vertex);

                // iterate the adjList
                iter = vertex.adjList.entrySet().iterator();
                while (iter.hasNext()) {
                    entry = iter.next();
                    pair = entry.getValue();

                    // update the adj vertex by the cost of edge,
                    // providing the edge cost is smaller
                    if (pair.second < pair.first.getWeight()) {
                        pair.first.setWeight(pair.second);

                        // if the weight of a vertex which is contained in the priority queue changed,
                        // remove it first, and then add it on.
                        if (vertexPriorityQueue.contains(pair.first)) {
                            vertexPriorityQueue.remove(pair.first);
                        }
                        vertexPriorityQueue.add(pair.first);
                    }
                }
                // finish update the weight of the vertex from adjList
            }
            // if we reaches the dest, by that it means that the least weighed vertex in the graph
            //  is now the dest vertex.
            // vertex's equal depends on data's equal
            while (!vertexPriorityQueue.peek().equals(dest));

            // this is just for the completion of the algorithm
            // 1. visit the dest
            // 2. save route to dest
            // 3. push dest to visited
            // useless check, can be removed later
            if (vertexPriorityQueue.peek().equals(dest)) {
                dest.visit();
                theGrandRouteEdgeList.add(new Edge<>(visitedRouteStack.peek(), dest, (dest.adjList.get(visitedRouteStack.peek().data)).second));
                visitedRouteStack.push(dest);
            }

            return theGrandRouteEdgeList;

        } else {
            System.out.println("Please check your source and destination, merci.");
            return null;
        }
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
