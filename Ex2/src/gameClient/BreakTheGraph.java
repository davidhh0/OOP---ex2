package gameClient;

import java.util.HashMap;

import api.*;
import jdk.swing.interop.SwingInterOpUtils;

import java.util.*;


/**
 * This class helps us to break the graph into different components considering connectivity and area of responsibility for
 * each agent.
 */
public class BreakTheGraph {
    directed_weighted_graph theGraph;
    HashMap<Integer, ArrayList<node_data>> rangeToNode;
    public static ArrayList<directed_weighted_graph> graphs;
    ArrayList<numRange> ranges;
    int numOfNodes = 0;
    int numOfAgents = 0;
    int rangePerAgent = 0;
    double mostLeft;

    public BreakTheGraph(directed_weighted_graph graph, int nodes, int agents) {
        this.theGraph = graph;
        numOfNodes = nodes;
        rangeToNode = new HashMap<>();
        numOfAgents = agents;
        ranges = new ArrayList<>();
        graphs = new ArrayList<>();
        rangePerAgent = (int)(getRange(graph) / numOfAgents);
        for (int i = 0; i < numOfAgents; i++) {
            rangeToNode.put(i, new ArrayList<>());
            graphs.add(new DWGraph_DS());
        }
        getRange();
        groupByNodes();
        groupByEdges();
    }

    private double getRange(directed_weighted_graph graph){
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for(node_data node:graph.getV()){
            geo_location pos = node.getLocation();
            geo_location real = MyFrame.realLocation(pos);

            if(real.x()<min){
                min=real.x();
                mostLeft=real.x();
            }
            if(real.x()>max){
                max=real.x();
            }


        }
        return (max-min);
    }
    /**
     * Divide the graph into (numOfAgents) parts. Each part contains (numOfNodes/numOfAgents) nodes.
     * Each part is connected.
     * Determine the parts by theGraph nodes location.
     * As default width of the frame is 1000 -> each part ~~ (1000/numOfAgents)
     */
    public void groupByNodes() {
        Iterator<node_data> iter = theGraph.getV().iterator();
        while (iter.hasNext()) {
            node_data n = iter.next();
            geo_location pos = n.getLocation();
            geo_location real = MyFrame.realLocation(pos);
            for (int i = 0; i < ranges.size(); i++) {
                if (ranges.get(i).contains((int) real.x())) {
                    rangeToNode.get(i).add(n);
                }
            }
        }
        System.out.println();
    }

    public void groupByEdges() {
        for (int i = 0; i < numOfAgents; i++) {
            ArrayList<node_data> current = rangeToNode.get(i);
            for (int j = 0; j < current.size(); j++) {
                node_data currentNode = current.get(j);
                for (edge_data run : this.theGraph.getE(currentNode.getKey())) {
                    if (rangeToNode.get(i).contains(theGraph.getNode(run.getSrc())) || rangeToNode.get(i).contains(theGraph.getNode(run.getDest()))) {// GOOD
                        graphs.get(i).addNode(theGraph.getNode(run.getSrc()));
                        graphs.get(i).addNode(theGraph.getNode(run.getDest()));
                        graphs.get(i).connect(run.getSrc(), run.getDest(), run.getWeight());
                        if(theGraph.getEdge(run.getDest(),run.getSrc())!=null){
                            graphs.get(i).connect(run.getDest(), run.getSrc(), run.getWeight());
                        }
                    }
                }
            }

        }
        System.out.println();

    }


    public void getRange() {
        double start = mostLeft, end = mostLeft+rangePerAgent;
        for (int i = 0; i < numOfAgents; i++) {
            ranges.add(new numRange((int)start, (int)end));
            start += rangePerAgent;
            end += rangePerAgent;
        }

    }

}
