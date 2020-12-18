package gameClient.util;

import api.DWGraph_DS;
import api.directed_weighted_graph;
import api.edge_data;
import api.node_data;

import java.util.*;

/**
 * This class runs a DFS algorithm through a given graph and returns the connected components represented by
 * linked-lists.
 */
public class DFS_Algo {
    private directed_weighted_graph _graph;
    public ArrayList<directed_weighted_graph> _graphList;

    enum G_Color {WHITE, GRAY, BLACK, GREEN}

    ;
    private HashMap<Integer, G_Color> colorMap;
    private PriorityQueue<StartFinish> startFinishes;
    private static int timer = 0;
    private static int greenCount = 0;

    /**
     * Constructor for the class that gets a directed weighted graph.
     * @param graph
     */
    public DFS_Algo(directed_weighted_graph graph) {
        this._graph = graph;
        this._graphList = new ArrayList<>();
        colorMap = new HashMap<>();
        startFinishes = new PriorityQueue<>();

    }

    public void DFS(int node) {
        StartFinish startFinish = new StartFinish(node, ++timer, 0);
        colorMap.put(node, G_Color.GRAY);
        for (edge_data edge : this._graph.getE(node)) {
            if (colorMap.get(edge.getDest()) == G_Color.WHITE) {
                int dest = edge.getDest();
                DFS(dest);
            }
        }
        startFinish.set_end(++timer);
        startFinishes.offer(startFinish);
        colorMap.put(node, G_Color.BLACK);
    }

    public void DFS_Transpose(int node) {
        colorMap.put(node, G_Color.GRAY);
        DWGraph_DS ds_graph = (DWGraph_DS) this._graph;
        for (Integer dest : (ds_graph.getInComing(node))) {
            if (colorMap.get(dest) == G_Color.WHITE) {
                DFS_Transpose(dest);
            }
        }
        colorMap.put(node, G_Color.BLACK);
    }


    public void StartDFS() {

        int nodeID = -1;
        //init all nodes into white
        for (node_data nodeData : this._graph.getV()) {
            if (colorMap.get(nodeData.getKey()) != G_Color.GREEN) {
                if (nodeID < 0) {
                    nodeID = nodeData.getKey();
                }
                colorMap.put(nodeData.getKey(), G_Color.WHITE);
            }
        }

        //Start DFS on first node

        DFS(nodeID);

        for (node_data nodeData : this._graph.getV()) {
            if (colorMap.get(nodeData.getKey()) != G_Color.GREEN)
                colorMap.put(nodeData.getKey(), G_Color.WHITE);
        }

        while (!startFinishes.isEmpty()) {
            StartFinish node = startFinishes.poll();
            if (colorMap.get(node.get_id()) != G_Color.BLACK) {
                DFS_Transpose(node.get_id());
            }
        }
        createGraph();
        if (greenCount != this._graph.nodeSize()) {
            this.StartDFS();
        }

        System.out.println("finish DFS");

    }


    private void createGraph() {
        directed_weighted_graph graph = new DWGraph_DS();
        //Add all nodes that are black
        for (Map.Entry<Integer, G_Color> entry : this.colorMap.entrySet()) {
            if (entry.getValue() == G_Color.BLACK) {
                graph.addNode(this._graph.getNode(entry.getKey()));
                this.colorMap.put(entry.getKey(), G_Color.GREEN);
                greenCount++;
            }
        }

        for (node_data node : graph.getV()) {
            for (edge_data edge : this._graph.getE(node.getKey())) {
                if (graph.getNode(edge.getSrc()) != null && graph.getNode(edge.getDest()) != null) {
                    graph.connect(edge.getSrc(), edge.getDest(), edge.getWeight());
                }
            }
        }

        this._graphList.add(graph);

    }
}