package api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        JsonObject json = new JsonObject();
        //Save the vertices
        JsonArray vertices_array = new JsonArray();
        for (node_data _node : this._graph.getV()) {
            JsonObject node = new JsonObject();
            if(_node.get_location() != null) {
                node.addProperty("pos", ((GeoLocation) (_node.get_location())).toString());
            }
            else{
                node.addProperty("pos", "NULL,NULL,NULL");
            }
            node.addProperty("id", _node.get_key());
            vertices_array.add(node);
        }
        //Save the edges


        JsonArray edges_array = new JsonArray();
        for (Map.Entry<Integer, HashMap<Integer, edge_data>> entry: ((DWGraph_DS) this._graph).getEdges().entrySet()) {
            for (Map.Entry<Integer, edge_data> entry1 : entry.getValue().entrySet()) {
                JsonObject edge = new JsonObject();
                edge.addProperty("src", entry1.getValue().get_src());
                edge.addProperty("w", entry1.getValue().get_weight());
                edge.addProperty("dest", entry1.getValue().get_dest());
                edges_array.add(edge);
            }
        }
        //Put them in jsonObject
        json.add("Nodes", vertices_array);
        json.add("Edges", edges_array);

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
        try{
            this._graph = new DWGraph_DS();
            Gson gson = new Gson();
            //Type for JsonObject
            Type JsonObjectType = new TypeToken<JsonObject>() {}.getType();
            //Parse json string into object
            JsonReader reader = new JsonReader(new FileReader(file));
            JsonObject graph_json = gson.fromJson(reader, JsonObjectType);
            JsonArray vertices = graph_json.get("Nodes").getAsJsonArray();
            JsonArray edges = graph_json.get("Edges").getAsJsonArray();
            //Adding all vertices and add them to graph
            for(JsonElement ver  : vertices){
                JsonObject verObj = ver.getAsJsonObject();
                NodeData node = new NodeData(verObj.get("id").getAsInt());
                String geoString[] = verObj.get("pos").getAsString().split(",");
                String x = geoString[0];
                String y = geoString[1];
                String z = geoString[2];

                if(x.equals("NULL") || y.equals("NULL") || z.equals("NULL")){

                }
                else{
                    GeoLocation geo = new GeoLocation(Double.parseDouble(x),Double.parseDouble(y),Double.parseDouble(z));
                    node.set_location(geo);

                }
                this._graph.addNode(node);
            }

            //Connect all edges
            for(JsonElement edge : edges){
                JsonObject edgeObj = edge.getAsJsonObject();
                this._graph.connect(Integer.parseInt(edgeObj.get("src").getAsString()),Integer.parseInt(edgeObj.get("dest").getAsString()), edgeObj.get("w").getAsDouble());
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
