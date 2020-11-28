package Tests;

import api.DWGraph_Algo;
import api.DWGraph_DS;
import api.NodeData;
import api.node_data;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class JunitTest {
    DWGraph_DS graph = new DWGraph_DS();
    double EPS = 0.0001;

    @Test
    public void AddAndRemoveTest(){
        NodeData n1 = new NodeData(1);
        NodeData n2 = new NodeData(2);

        graph.addNode(n1);
        graph.addNode(n2);

        node_data a = graph.removeNode(1);
        node_data b = graph.getNode(2);

        Assertions.assertEquals(a.get_key(),n1.get_key());
    }

    public DWGraph_DS Graph_1A(){
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

        g.connect(0,1,3);
        g.connect(0,3,8);
        g.connect(0,4,8);
        g.connect(1,2,1);
        g.connect(1,3,4);
        g.connect(3,2,2);
        g.connect(4,3,3);

        return g;
    }

    @Test
    public void ConnectivityTest(){
        DWGraph_DS graph = Graph_1A();
        Assertions.assertEquals(graph.nodeSize(),5);
        Assertions.assertEquals(graph.edgeSize(),7);
        graph.removeEdge(0,4);
        Assertions.assertEquals(graph.edgeSize(),6);
        graph.removeNode(0);
        Assertions.assertEquals(graph.edgeSize(),4);

    }

    @Test
    public void SaveTest(){
        DWGraph_DS graph = Graph_1A();
        DWGraph_Algo algo = new DWGraph_Algo();
        algo.init(graph);
        algo.save("TestSave1.json");
    }


    @Test
    public void LoadTest(){
        DWGraph_DS graph = Graph_1A();
        DWGraph_Algo algo = new DWGraph_Algo();
        algo.load("/Users/yuvalmarmer/Files/Study/OOP/Ex2/OOP---ex2/Ex2/data/A5");
        //Assertions.assertEquals(graph.edgeSize(), algo.getGraph().edgeSize());
        //Assertions.assertEquals(graph.getV().size(), algo.getGraph().getV().size());
        algo.isConnected();
        System.out.println(";algo.shortestPathDist(0,5)");
    }



}
