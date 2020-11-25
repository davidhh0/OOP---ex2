package api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class DWGraph_Algo implements dw_graph_algorithms {

    private directed_weighted_graph _graph;

    /**
     * Init the DWGraph_Algo with graph.
     * @param g - the graph that DWGraph_Algo will work on.
     */
    @Override
    public void init(directed_weighted_graph g) {
        this._graph=g;
    }
    /**
     * Returns the graph that initialized into DWGraph_Algo
     * @return the directed_weighted_graph from algo
     */
    @Override
    public directed_weighted_graph getGraph() {
        return _graph;
    }

    @Override
    public directed_weighted_graph copy() {
        return null;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        return null;
    }
    /**
     * Using Gson to dump the DWGraph_DS into a json format and save it in the file.
     * Dump the hashmap of vertices as "Vertices" key in json.
     * Dump the hashmap of edges as "Edges" key in json.
     * And write the json string into file.
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
