package api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {

    private directed_weighted_graph _graph;

    /**
     * Init the DWGraph_Algo with graph.
     *
     * @param g - the graph that DWGraph_Algo will work on.
     */
    @Override
    public void init(directed_weighted_graph g) {
        this._graph = g;
    }

    /**
     * Returns the graph that initialized into DWGraph_Algo
     *
     * @return the directed_weighted_graph from algo
     */
    @Override
    public directed_weighted_graph getGraph() {
        return _graph;
    }

    /**
     * This function returns a deep copy of the initialized graph, considering all nodes and edges.
     *
     * @return directed weighted graph
     */
    @Override
    public directed_weighted_graph copy() {
        DWGraph_DS NewGraph = new DWGraph_DS();
        // Copying all the nodes
        for (node_data run : _graph.getV()) {
            NewGraph.addNode(run);
        }
        //Copying all edges
        for (node_data run : _graph.getV()) {
            if (NewGraph.edgeSize() == _graph.edgeSize()) break;
            for (edge_data run1 : _graph.getE(run.get_key())) { // returns a collection of edges coming out from run
                NewGraph.connect(run1.get_src(), run1.get_dest(), run1.get_weight());
            }

        }
        return NewGraph;
    }

    @Override
    public boolean isConnected() {
        if (_graph == null || _graph.nodeSize() == 1) return true;
        HashMap<Integer, Boolean> visited = new HashMap<>();
        Stack<node_data> stack = new Stack<>();
        node_data startPoint = null;
        for (node_data run : _graph.getV()) {
            stack.push(run);
            startPoint = run;
            break;
        }
        while (!stack.isEmpty()) {
            node_data temp = stack.pop();
            visited.put(temp.get_key(), true);
            for (edge_data run1 : _graph.getE(temp.get_key())) {
                if (visited.get(run1.get_dest()) == null) {
                    visited.put(run1.get_dest(), true);
                    stack.push(_graph.getNode(run1.get_dest()));
                }
            }
        }
        if (visited.size() != _graph.nodeSize()) return false;

        // ---------------------------- Transpose Graph-------------------------------

        HashMap<Integer, Boolean> visitedT = new HashMap<>();
        Stack<node_data> stackT = new Stack<>();

        stackT.push(startPoint);

        while (!stackT.isEmpty()) {
            node_data temp = stackT.pop();
            visitedT.put(temp.get_key(), true);
            for (Integer run1 : (((DWGraph_DS) this._graph).getInComing(temp.get_key()))) {
                if (visitedT.get(run1) == null) {
                    visitedT.put(run1, true);
                    stackT.push(_graph.getNode(run1));
                }
            }
        }

        return visitedT.size() == _graph.nodeSize();
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        if(getGraph().getNode(src) == null || getGraph().getNode(dest)==null) {
            System.err.println("One or both of the nodes are not in the graph!!");
            return -1;
        }
        LinkedList<node_data> answer = (LinkedList<node_data>) shortestPath(src,dest);
        if(answer==null) return -1;
        return _graph.getNode(dest).get_tag();
    }

    /**
     * This method computes the shortest path between two nodes. A shortest path is the minimum sum of edge weights from
     * "src" to "dest". The method has a "visited" and "parent" collection. The first one purpose is to know whether or not
     * a node has been visited and the second collection's purpose is to know the path we came through the visited nodes.
     * The method has a Priority Queue as well. A priority is given to the node with the smallest value of the tag.
     * The tag represents the total weight from "src" to it's node.
     * First, we initialize all the node's tag in the graph to infinity and the "src" node to 0 and insert "src" to the PQ.
     * Then, we search for the "dest" node in graph, but we traverse through the graph in a way that we pop from the
     * PQ the node with the smallest total weight from "src" to it. The method calculates the path from "src" to it's
     * neighbors and their neighbors... by adding the edge weight to the node's tag.
     * When we hit the desired "dest" node, we made sure that the have traversed through the path with the smallest value
     * in the graph thanks to the PriorityQueue that polls the node with smallest tag value not matter the order of the
     * inserting.
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        //PriorityQueue<node_data> PQ = new PriorityQueue<>();
        int initialCapacity = 10;

        HashMap<node_data, Boolean> visited = new HashMap<>();
        HashMap<node_data, node_data> parent = new HashMap<>();
        LinkedList<node_data> answer = new LinkedList<>();
        node_data sofi = null;
        HashMap<Integer,Double> tag =new HashMap<>();
        PriorityQueue<node_data> PQ = new PriorityQueue<node_data>(initialCapacity, new Comparator<node_data>() {
            @Override
            public int compare(node_data o1, node_data o2) {
                if (tag.get(o1.get_key()) < tag.get(o2.get_key()) )
                    return -1;
                else
                    return 1;
            }

        });
        boolean flag = true;
        for (node_data run : _graph.getV()) {
            tag.put(run.get_key(),Double.POSITIVE_INFINITY);
        }
        tag.put(src,0.0);
        PQ.offer(_graph.getNode(src));

        while (!PQ.isEmpty() && flag) {
            node_data temp = PQ.poll();
            if (visited.get(temp) == null) { // means temp wasn't visited yet
                if (temp.get_key() == dest) {
                    sofi = temp;
                    flag = false;
                }
                visited.put(temp, true);
                for (edge_data run : _graph.getE(temp.get_key())) {
                    if (visited.get(run.get_dest()) == null) { //means run wasn't visited
                        //int path = (int) (temp.get_tag() + run.get_weight());   // !!! CAST TO INT
                        double Path = run.get_weight() + tag.get(temp.get_key());
                        if (Path < tag.get(run.get_dest())) {
                            tag.put(run.get_dest() , Path);
                           // _graph.getNode(run.get_dest()).set_tag(path);
                            parent.put(_graph.getNode(run.get_dest()), temp);
                            PQ.offer(_graph.getNode(run.get_dest()));
                        }
                    }

                }

            }
        }
        if(sofi==null) return answer;
        if(sofi.get_key()!=dest) return answer;
        boolean flag1=true;
        try{
            answer.add(_graph.getNode(dest));
            while(flag1) {
                if (sofi.get_key() == src) {
                    flag1 = false;
                    break;
                }

                node_data answersfather = parent.get(sofi);
                answer.addFirst(answersfather);
                sofi = parent.get(sofi);
            }
        }
        catch (Exception e) {
            System.out.println("Wasn't found");}
        return answer;

    }

    /**
     * Using Gson to dump the DWGraph_DS into a json format and save it in the file.
     * Dump the hashmap of vertices as "Vertices" key in json.
     * Dump the hashmap of edges as "Edges" key in json.
     * And write the json string into file.
     *
     * @param file - the file name (may include a relative path).
     * @return true if saved successful
     */
    @Override
    public boolean save(String file) {
        Gson gson = new Gson();
        //Save the vertices
        JsonArray vertices = gson.toJsonTree(((DWGraph_DS) this._graph).getV()).getAsJsonArray();
        //Save the edges
        JsonObject edges_string = gson.toJsonTree(((DWGraph_DS) this._graph).getEdges()).getAsJsonObject();
        JsonObject json = new JsonObject();
        //Put them in jsonObject
        json.add("Vertices", vertices);
        json.add("Edges", edges_string);
        try {
            PrintWriter writer = new PrintWriter(new File(file));
            writer.write(gson.toJson(json));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean load(String file) {
        return false;
    }
}
