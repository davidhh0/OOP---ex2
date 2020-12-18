package Tests;

import Server.Game_Server_Ex2;
import api.*;
import gameClient.BreakTheGraph;
import gameClient.util.DFS_Algo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class JunitTest {
   // DWGraph_DS graph = new DWGraph_DS();
    double EPS = 0.0001;

    @Test
    public void AddAndRemoveTest() {
        DWGraph_DS graph = new DWGraph_DS();
        NodeData n1 = new NodeData(1);
        NodeData n2 = new NodeData(2);

        graph.addNode(n1);
        graph.addNode(n2);

        node_data a = graph.removeNode(1);
        node_data b = graph.getNode(2);

        Assertions.assertEquals(a.getKey(), n1.getKey());
    }

    public DWGraph_DS Graph_1B() {
        DWGraph_DS g = new DWGraph_DS();
        NodeData n1 = new NodeData(1);
        NodeData n2 = new NodeData(2);
        NodeData n3 = new NodeData(3);
        NodeData n0 = new NodeData(0);
        NodeData n4 = new NodeData(4);

        g.addNode(n0);
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);

        g.connect(0, 1, 3);
        g.connect(0, 3, 8);
        g.connect(0, 4, 8);
        g.connect(1, 2, 1);
        g.connect(1, 3, 4);
        g.connect(3, 2, 2);
        g.connect(4, 3, 3);

        return g;
    }

    public DWGraph_DS Graph_2B() {
        DWGraph_DS g = new DWGraph_DS();
        NodeData n1 = new NodeData(1);
        NodeData n2 = new NodeData(2);
        NodeData n3 = new NodeData(3);
        NodeData n0 = new NodeData(0);
        NodeData n4 = new NodeData(4);

        g.addNode(n0);
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);

        g.connect(0, 1, 1);
        g.connect(1, 2, 2);
        g.connect(2, 4, 10);
        g.connect(2, 3, 20);
        g.connect(3, 0, 30);
        g.connect(4, 2, 40);

        return g;
    }

    public DWGraph_DS Graph_3B() {
        DWGraph_DS g = new DWGraph_DS();
        NodeData n1 = new NodeData(1);
        NodeData n2 = new NodeData(2);
        NodeData n3 = new NodeData(3);
        NodeData n0 = new NodeData(0);
        NodeData n4 = new NodeData(4);
        NodeData n5 = new NodeData(5);
        NodeData n6 = new NodeData(6);
        NodeData n7 = new NodeData(7);

        g.addNode(n0);
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);
        g.addNode(n5);
        g.addNode(n6);
        g.addNode(n7);

        g.connect(0, 1, 1);
        g.connect(0, 2, 2);
        g.connect(0, 3, 5);
        g.connect(1, 4, 4);
        g.connect(1, 5, 11);
        g.connect(2, 4, 9);
        g.connect(2, 5, 5);
        g.connect(2, 6, 16);
        g.connect(3, 6, 2);
        g.connect(4, 7, 18);
        g.connect(5, 7, 13);
        g.connect(6, 7, 2);

        return g;


    }

    @Test
    public void ConnectivityTest() {
        DWGraph_DS graph = Graph_1B();
        Assertions.assertEquals(graph.nodeSize(), 5);
        Assertions.assertEquals(graph.edgeSize(), 7);
        graph.removeEdge(0, 4);
        Assertions.assertEquals(graph.edgeSize(), 6);
        graph.removeNode(0);
        Assertions.assertEquals(graph.edgeSize(), 4);

    }

    @Test
    public void SaveTest() {
        DWGraph_DS graph = Graph_1B();
        DWGraph_Algo algo = new DWGraph_Algo();
        algo.init(graph);
        algo.save("TestSave1.json");
    }


    @Test
    public void LoadTest() {
        DWGraph_DS graph = Graph_1B();
        DWGraph_Algo algo = new DWGraph_Algo();
        algo.load("TestSave1.json");
        Assertions.assertEquals(graph.edgeSize(), algo.getGraph().edgeSize());
        Assertions.assertEquals(graph.getV().size(), algo.getGraph().getV().size());
    }

    @Test
    public void DeepCopyTest() {
        DWGraph_DS graph = Graph_1B();
        dw_graph_algorithms graph_algo = new DWGraph_Algo();
        graph_algo.init(graph);
        directed_weighted_graph newGraph = graph_algo.copy();
        Assertions.assertEquals(graph.edgeSize(), newGraph.edgeSize());
        Assertions.assertEquals(graph, newGraph);
    }

    @Test
    public void isConnected() {
        DWGraph_DS graph_1B = Graph_1B(); // is NOT connected!!
        DWGraph_DS graph_2B = Graph_2B(); // is connected !!
        dw_graph_algorithms graph_algo1B = new DWGraph_Algo();
        dw_graph_algorithms graph_algo2B = new DWGraph_Algo();

        graph_algo1B.init(Graph_1B());
        graph_algo2B.init(Graph_2B());

        Assertions.assertFalse(graph_algo1B.isConnected());
        Assertions.assertTrue(graph_algo2B.isConnected());

    }

    @Test
    public void PQtest2() {
        DWGraph_DS graph = new DWGraph_DS();
        int initialCapacity = 10;
        HashMap<Integer, Double> tag = new HashMap<>();
        PriorityQueue<node_data> PQ = new PriorityQueue<node_data>(initialCapacity, new Comparator<node_data>() {
            @Override
            public int compare(node_data o1, node_data o2) {
                if (tag.get(o1.getKey()) < tag.get(o2.getKey()))
                    return -1;
                else
                    return 1;
            }

        });
        graph.addNode(new NodeData(1));
        graph.addNode(new NodeData(2));
        graph.addNode(new NodeData(3));
        tag.put(1, 5.0);
        tag.put(2, 10.0);
        tag.put(3, 2.0);
        PQ.offer(graph.getNode(1));
        PQ.offer(graph.getNode(2));
        PQ.offer(graph.getNode(3));
        System.out.println(PQ.poll().getKey());
        System.out.println(PQ.poll().getKey());
        System.out.println(PQ.poll().getKey());


    }

    @Test
    public void shortestPath() {
        DWGraph_DS graph = Graph_3B();
        dw_graph_algorithms graph_algo3B = new DWGraph_Algo();
        graph_algo3B.init(Graph_3B());

        LinkedList<node_data> list = (LinkedList<node_data>) graph_algo3B.shortestPath(0, 7);
        Assertions.assertEquals(list.size(), 4);

    }

    @Test
    public void shortestPathDist() {
        DWGraph_DS graph = Graph_3B();
        dw_graph_algorithms graph_algo3B = new DWGraph_Algo();
        graph_algo3B.init(Graph_3B());

        Assertions.assertEquals(graph_algo3B.shortestPathDist(0, 0), 0);

    }
    @Test
    public void overWriteEdge(){
        DWGraph_DS graph = Graph_2B();
        // 2 -> 4 (10)
        Assertions.assertEquals(graph.getEdge(2,4).getWeight(),10);
        graph.connect(2,4,20);
        Assertions.assertEquals(graph.getEdge(2,4).getWeight(),20);
    }

    @Test
    public void emptyList(){
        DWGraph_DS graph = Graph_2B();
        dw_graph_algorithms algo = new DWGraph_Algo();
        algo.init(graph);
        LinkedList<node_data> path = (LinkedList<node_data>) algo.shortestPath(2,99);
        Assertions.assertNull(path);
    }
    // ----------------------------- Tests for Part II ------------------------------------------

    @Test
    public void DFStest() {
        dw_graph_algorithms theGraph = new DWGraph_Algo();
        theGraph.load("data/A1");
        directed_weighted_graph graph = theGraph.getGraph();
        DFS_Algo dfs = new DFS_Algo(graph);
        dfs.StartDFS();
        Assertions.assertEquals(dfs._graphList.size(), 1);
    }

    @Test
    public void DFStest2() {
        dw_graph_algorithms theGraph = new DWGraph_Algo();
        theGraph.load("data/A2");
        directed_weighted_graph graph = theGraph.getGraph();
        DFS_Algo dfs = new DFS_Algo(graph);
        dfs.StartDFS();
        Assertions.assertEquals(dfs._graphList.size(), 1);
    }

    @Test
    public void DFSfor2components() {
        dw_graph_algorithms theGraph = new DWGraph_Algo();
        theGraph.load("data/A4broken");
        directed_weighted_graph graph = theGraph.getGraph();
        DFS_Algo dfs = new DFS_Algo(graph);
        dfs.StartDFS();
        Assertions.assertEquals(dfs._graphList.size(), 2);
    }

}
