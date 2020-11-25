package api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class DWGraph_DS implements directed_weighted_graph {

    private HashMap<Integer, HashMap<Integer, edge_data>> edges;
    private HashMap<Integer, node_data> vertices;
    private HashMap<Integer,ArrayList<Integer>> inComing;
    private int NumberOfEdges = 0;
    private int ModeCount = 0;

    public DWGraph_DS(){
        edges = new HashMap<>();
        vertices = new HashMap<>();
        inComing = new HashMap<>();
    }

    /**
     * Return the Hashmap of edges....
     * @return
     */
    public HashMap<Integer, HashMap<Integer, edge_data>> getEdges() {
        return edges;
    }

    /**
     * This function returns the node_data represented by the unique given key.
     * If there is not such a node in the graph, returns null
     * @param key - the node_id
     * @return node_data represented by given key
     */
    @Override
    public node_data getNode(int key) {
        return vertices.get(key);
    }

    /**
     * This function returns edge_data between src to dest. If no such an edge exists between those two, return null
     * @param src
     * @param dest
     * @return edge_data between source (src) to destination (dest)
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if(edges.get(src).get(dest) == null) {
            System.err.println("Error in getEdge - there isn't such an edge");
            return null;
        }
        return edges.get(src).get(dest);
    }

    /**
     * This function adds a new node to the graph as long as the unique key of the new node is not represented any other
     * node in the graph.
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if (!vertices.containsKey(n.get_key())) {
            //the node is NOT in vertices:
            vertices.put(n.get_key(), n);
            HashMap<Integer, edge_data> EdgesHashMap = new HashMap<>();
            ArrayList<Integer> newInComing = new ArrayList<>();
            if (edges != null)
                edges.put(n.get_key(), EdgesHashMap);
            inComing.put(n.get_key(),newInComing);
            ModeCount++;
        }
    }

    /**
     * This function connects src node to dest node with an edge with weight w.
     * If one of the nodes are not in the graph, it does nothing.
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (!vertices.containsKey(src) || !vertices.containsKey(dest) || src == dest || w <= 0) {
            System.err.println("(connect issue) One or both of the nodes are not in the graph OR both of the nodes key are the same");
            return;
        }
        try {
            if (edges.get(src).get(dest) == null)
                NumberOfEdges++;
            EdgeData newEdge = new EdgeData(src, dest, w);
            edges.get(src).put(dest, newEdge);
            inComing.get(dest).add(src);
            ModeCount++;
        } catch (Exception e) {
            System.err.println("Exception in connecting to nodes");
            e.printStackTrace();
        }
    }

    /**
     * This function returns a collection of the nodes in the graph.
     * @return Collection of total vertices in the graph.
     */
    @Override
    public Collection<node_data> getV() {
        try {
            if (vertices == null)
                return Collections.<node_data>emptyList();

            return vertices.values();
        } catch (Exception e) {
            System.err.println("No vertices");
        }
        return null;
    }

    /**
     * This function returns a collection of edge_data with all the edges coming out of node_id.
     * If there is not even one edge, return an empty set.
     * @param node_id
     * @return Collection of edge_data of specified node.
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        try {
            HashMap<Integer, edge_data> toReturn = new HashMap<>();
            for (Integer run : edges.get(node_id).keySet()) {
                toReturn.put(run, getEdge(node_id, run));
            }
            if (toReturn == null)
                return Collections.<edge_data>emptyList();

            return toReturn.values();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This function removes a node_data represented by the given key and then returns it.
     * This function considers all the edges coming out of the node and removes them as well.
     * @param key
     * @return the removed node_data
     */
    @Override
    public node_data removeNode(int key) {
        if (vertices.containsKey(key)) {
            node_data nodeToRemove = vertices.get(key);
            ArrayList<Integer> keysToRemove = new ArrayList();
            for (Integer run : edges.get(key).keySet()) {
                keysToRemove.add(run);
            }
            for (int i = 0; i < keysToRemove.size(); i++) {
                removeEdge(key, keysToRemove.get(i));
            }
            for(Integer run : inComing.get(key)){
                removeEdge(run,key);
            }
            vertices.remove(key);
            edges.remove(key);
            inComing.remove(key);
            ModeCount++;
            return nodeToRemove;
        }
        System.err.println("removeNode error - there isn't such a key");
        return null;
    }

    /**
     * This function removes an edge between src to dest and returns it.
     * @param src
     * @param dest
     * @return the removed edge.
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if (edges.get(src) != null) {
            edge_data edgeToRemove = edges.get(src).get(dest);
            edges.get(src).remove(dest);
            NumberOfEdges--;
            ModeCount++;
            return edgeToRemove;
        }
        System.err.println("Error in removeEdge - there isn't such an edge");
        return null;
    }

    /**
     * This function returns the total number of vertices in the graph.
     * @return number of vertices in the graph.
     */
    @Override
    public int nodeSize() {
        return vertices.size();
    }

    /**
     * This function returns the total number of edges in the graph.
     * @return number of edges in the graph.
     */
    @Override
    public int edgeSize() {
        return NumberOfEdges;
    }

    /**
     * This function return the ModeCount - how many changes have occured in the graph.
     * @return number of changes in the graph.
     */
    @Override
    public int getMC() {
        return ModeCount;
    }
}
