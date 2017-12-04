import java.util.*;
        import java.util.Map.Entry;
        import java.io.*;

public class Graph<E>
{
    // todo: ask for approval
    // actually we can move it into the Dijkstra class
    public LinkedStack<Edge<E>> deletedEdgeStack;

    // todo: ask for approval
    public void undo() {
        Edge<E> deletedEdge = deletedEdgeStack.pop();
        addEdge(deletedEdge.source.data, deletedEdge.dest.data, deletedEdge.cost);
        System.out.println("Undo request completed.");
    }

    // the graph data is all here --------------------------
    protected HashMap<E, Vertex<E> > vertexSet;

    // public graph methods --------------------------------
    public Graph ()
    {
        vertexSet = new HashMap<E, Vertex<E> >();
        // todo: ask for approval
        deletedEdgeStack = new LinkedStack<>();
    }

    public void addEdge(E source, E dest, double cost)
    {
        Vertex<E> src, dst;

        // put both source and dest into vertex list(s) if not already there
        src = addToVertexSet(source);
        dst = addToVertexSet(dest);

        // add dest to source's adjacency list
        src.addToAdjList(dst, cost);

        dst.addToAdjList(src, cost); // ADD THIS IF UNDIRECTED GRAPH
    }

    public void addEdge(E source, E dest, int cost)
    {
        addEdge(source, dest, (double)cost);
    }

    // adds vertex with x in it, and always returns ref to it
    public Vertex<E> addToVertexSet(E x)
    {
        Vertex<E> retVal=null;
        Vertex<E> foundVertex;

        // find if Vertex already in the list:
        foundVertex = vertexSet.get(x);

        if ( foundVertex != null ) // found it, so return it
        {
            return foundVertex;
        }

        // the vertex not there, so create one
        retVal = new Vertex<E>(x);
        vertexSet.put(x, retVal);

        return retVal;   // should never happen
    }

    public boolean remove(E start, E end)
    {
        Vertex<E> startVertex = vertexSet.get(start);
        boolean removedOK = false;

        if( startVertex != null )
        {
            Pair<Vertex<E>, Double> endPair = startVertex.adjList.remove(end);
            removedOK = endPair!=null;
        }

        // Add if UNDIRECTED GRAPH:
        Vertex<E> endVertex = vertexSet.get(end);
        if( endVertex != null )
        {
            Pair<Vertex<E>, Double> startPair = endVertex.adjList.remove(start);
            removedOK = startPair!=null ;
        }

        return removedOK;
    }

    public void showAdjTable()
    {
        Iterator<Entry<E, Vertex<E>>> iter;

        System.out.println( "------------------------ ");
        iter = vertexSet.entrySet().iterator();
        while( iter.hasNext() )
        {
            (iter.next().getValue()).showAdjList();
        }
        System.out.println();
    }

    public void clear()
    {
        vertexSet.clear();
    }

    // reset all vertices to unvisited
    public void unvisitVertices()
    {
        Iterator<Entry<E, Vertex<E>>> iter;

        iter = vertexSet.entrySet().iterator();
        while( iter.hasNext() )
        {
            iter.next().getValue().unvisit();
        }
    }

    /** Breadth-first traversal from the parameter startElement*/
    public void breadthFirstTraversal(E startElement, Visitor<E> visitor)
    {
        unvisitVertices();

        Vertex<E> startVertex = vertexSet.get(startElement);
        breadthFirstTraversalHelper( startVertex, visitor );
    }

    /** Depth-first traversal from the parameter startElement */
    public void depthFirstTraversal(E startElement, Visitor<E> visitor)
    {
        unvisitVertices();

        Vertex<E> startVertex = vertexSet.get(startElement);
        depthFirstTraversalHelper( startVertex, visitor );
    }

    protected void breadthFirstTraversalHelper(Vertex<E> startVertex,
                                               Visitor<E> visitor)
    {
        LinkedQueue<Vertex<E>> vertexQueue = new LinkedQueue<>();
        E startData = startVertex.getData();

        startVertex.visit();
        visitor.visit(startData);
        vertexQueue.enqueue(startVertex);
        while( !vertexQueue.isEmpty() )
        {
            Vertex<E> nextVertex = vertexQueue.dequeue();
            Iterator<Map.Entry<E, Pair<Vertex<E>, Double>>> iter =
                    nextVertex.iterator(); // iterate adjacency list

            while( iter.hasNext() )
            {
                Entry<E, Pair<Vertex<E>, Double>> nextEntry = iter.next();
                Vertex<E> neighborVertex = nextEntry.getValue().first;
                if( !neighborVertex.isVisited() )
                {
                    vertexQueue.enqueue(neighborVertex);
                    neighborVertex.visit();
                    visitor.visit(neighborVertex.getData());
                }
            }
        }
    } // end breadthFirstTraversalHelper

    public void depthFirstTraversalHelper(Vertex<E> startVertex, Visitor<E> visitor)
    {
        // YOU COMPLETE THIS (USE THE RECURSIVE ALGORITHM GIVEN FOR LESSON 11 EXERCISE)

        startVertex.visit();
        visitor.visit(startVertex.data);

        Iterator<Entry<E, Pair<Vertex<E>, Double>>> adjIter ;
        Entry<E, Pair<Vertex<E>, Double>> entry;
        Pair<Vertex<E>, Double> pair;

        // iterate through adjacencies in adjList
        adjIter = startVertex.adjList.entrySet().iterator();
        while( adjIter.hasNext() )
        {
            entry = adjIter.next();
            pair = entry.getValue();
            if (!pair.first.isVisited()) {
                depthFirstTraversalHelper(pair.first, visitor);
            }
        }
    }

// WRITE THE INSTANCE METHOD HERE TO
    //         WRITE THE GRAPH's vertices and its
    //         adjacency list TO A TEXT FILE (SUGGEST TO PASS AN
    //        ALREADY OPEN PrintWriter TO THIS) !

    void writeAdjListToFile() {
        // refresh
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // prompt for filename
        System.out.print("Filename: ");

        Scanner scanner = new Scanner(System.in);
        String filename = scanner.nextLine();

        // todo: change accordingly for the lab computer or something
        String tempAddress = "/Users/mantinglin/Desktop/" + filename + ".txt";

        // open file
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(tempAddress), "utf-8"))) {

            Iterator<Entry<E, Vertex<E>>> vIter;
            String graphStr = "";

            // iterate through vertexes in vertexSet
            vIter = vertexSet.entrySet().iterator();
            while( vIter.hasNext() )
            {
                Vertex<E> v = vIter.next().getValue();

                Iterator<Entry<E, Pair<Vertex<E>, Double>>> adjIter ;
                Entry<E, Pair<Vertex<E>, Double>> entry;
                Pair<Vertex<E>, Double> pair;
                String vertexStr;

                vertexStr = "Adj List for " + v.data + ": ";

                // iterate through adjacencies in adjList
                adjIter = v.adjList.entrySet().iterator();
                while( adjIter.hasNext() )
                {
                    entry = adjIter.next();
                    pair = entry.getValue();
                    vertexStr += pair.first.data + "("
                            + String.format("%3.1f", pair.second)
                            + ") ";
                }

                graphStr += vertexStr + "\n";
            }

            // write to file
            writer.write(graphStr);
            System.out.println("\nWriting complete.");
        } catch (Exception e) {
            System.out.println("Oops! This is super awkward.");
        }
    }
}
