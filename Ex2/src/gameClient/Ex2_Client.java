package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import gameClient.util.AgentEdgeDist;
import gameClient.util.EdgeValue;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.lang.reflect.Type;
import java.util.*;


public class Ex2_Client implements Runnable {
    private static MyFrame _win;
    private static Arena _ar;
    public static int _numberOfAgents;
    public static long timeToEnd = 0;
    private static HashMap<Integer, Integer> LastOne = new HashMap<>();
    public static HashMap<Integer, EdgeValue> AgentToPok = new HashMap<>();
    private static PriorityQueue<AgentEdgeDist> agentEdgeDistQ = new PriorityQueue<>();
    private static HashMap<Integer, directed_weighted_graph> agentToGraph;
    public static boolean isLogged=false;

    public static void main(String[] a) {
        Thread client = new Thread(new Ex2_Client());
        client.start();
    }

    @Override
    public void run() {
        int choice=0;
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel<Integer>();
        for(int i=0;i<=23;i++) comboBoxModel.addElement(i);
        JComboBox cb = new JComboBox(comboBoxModel);
        int result = JOptionPane.showConfirmDialog(null, cb, "Select a scenario number", JOptionPane.OK_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            choice = (int) cb.getSelectedItem();
        }
        else
            System.exit(0);


        int scenario_num = 11;
        game_service game = Game_Server_Ex2.getServer(choice); // you have [0,23] games
//	 	int id = 206320863;
//		game.login(id);

        //isLogged=game.login();

        DWGraph_Algo algo = new DWGraph_Algo();
        System.out.println(game.toString());
        Gson gson = new Gson();
        Type JsonObjectType = new TypeToken<JsonObject>() {}.getType();
        JsonObject gameJsonObject = gson.fromJson(game.toString(), JsonObjectType);
        String filepath = gameJsonObject.get("GameServer").getAsJsonObject().get("graph").getAsString();
        algo.load(filepath);
        directed_weighted_graph gg = algo.getGraph();



        init(game);
        _numberOfAgents = game.getAgents().split("id").length - 1;

        new BreakTheGraph(gg,gg.getV().size(),_numberOfAgents);

        game.startGame();
        //_win.setTitle("Ex2 - OOP: (NONE trivial Solution) " + game.toString());

        int ind = 0;
        long dt = 100;

        while (game.isRunning()) {
            moveAgants(game, gg);
            try {
                if (ind % 1 == 0) {
                    timeToEnd = game.timeToEnd();
                    _win.repaint();
                }
                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }

    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen (randomly).
     *
     * @param game
     * @param gg
     * @param
     */
    private static void moveAgants(game_service game, directed_weighted_graph gg) {


        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _ar.setAgents(log);
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs, gg);
        _ar.setPokemons(ffs);

        List<CL_Agent> copy_listAgnets = new ArrayList<>();

        for (CL_Agent a : log) {
            copy_listAgnets.add(a);
        }
//
        PriorityQueue<EdgeValue> edgesQ = new PriorityQueue<>();

        HashMap<edge_data, Double> edgeMap = new HashMap<>();
        HashMap<edge_data, Integer> edgeToType = new HashMap<>();

        List<CL_Pokemon> pokemonsList = Arena.json2Pokemons(fs, gg);

        for (CL_Pokemon pok : pokemonsList) {
            if (edgeMap.containsKey(pok.get_edge())) {
                edgeMap.put(pok.get_edge(), edgeMap.get(pok.get_edge()) + pok.getValue());
                edgeToType.put(pok.get_edge(), pok.getType());
            } else {
                edgeMap.put(pok.get_edge(), pok.getValue());
                edgeToType.put(pok.get_edge(), pok.getType());
            }
        }

        for (Map.Entry<edge_data, Double> entry : edgeMap.entrySet()) {
            EdgeValue edgeValue = new EdgeValue(entry.getValue(), entry.getKey(), edgeToType.get(entry.getKey()));
            edgesQ.offer(edgeValue);
        }
        //Setting type for EdgeValue


        while (!edgesQ.isEmpty()) {
            //Most valued edge
            EdgeValue edge = edgesQ.poll();
            //Most
            for (CL_Agent agent : copy_listAgnets) {
                distCalc(gg, agent.getSrcNode(), edge.get_edge(), agent);
            }

            AgentEdgeDist dist = agentEdgeDistQ.poll();
            CL_Agent agent = null;
            for (CL_Agent ag : copy_listAgnets) {
                if (ag.getID() == dist.get_agentID()) {
                    agent = ag;
                    break;
                }
            }
            for (int i = 0; i < copy_listAgnets.size(); i++) {
                CL_Agent agent1 = copy_listAgnets.get(i);
                if (agent1.getID() == agent.getID()) {
                    copy_listAgnets.remove(i);
                    break;
                }
            }
            if (agent == null) {
                break;
            }
            if (agent.getNextNode() == -1) {
                int dest = nextNode(gg, agent, edge);
                int id = agent.getID();
                AgentToPok.put(agent.getID(), edge);
                game.chooseNextEdge(id, dest);
                //System.out.println("Agent: " + agent.getID() + ", val: " + agent.getValue() + " turned to node: " + dest);
            }
            agentEdgeDistQ.clear();
        }

    }


    private static int nextNode(directed_weighted_graph g, CL_Agent agent, EdgeValue edgeValue) {
        DWGraph_Algo algo = new DWGraph_Algo();
        algo.init(g);

        if (edgeValue.get_type() > 0) {
            if (agent.getSrcNode() == edgeValue.get_edge().getSrc()) {
                return edgeValue.get_edge().getDest();
            }

            List<node_data> path = algo.shortestPath(agent.getSrcNode(), edgeValue.get_edge().getSrc());
            return path.get(1).getKey();
        } else {
            if (agent.getSrcNode() == edgeValue.get_edge().getDest()) {
                return edgeValue.get_edge().getSrc();
            }

            List<node_data> path = algo.shortestPath(agent.getSrcNode(), edgeValue.get_edge().getDest());
            return path.get(1).getKey();
        }


    }


    private static void distCalc(directed_weighted_graph g, int src, edge_data edge, CL_Agent agent) {
        DWGraph_Algo algo = new DWGraph_Algo();
        algo.init(g);
        double dist = algo.shortestPathDist(src, edge.getSrc());
        AgentEdgeDist agentEdgeDist = new AgentEdgeDist(agent.getID(), dist / agent.getSpeed());
        agentEdgeDistQ.offer(agentEdgeDist);
    }


    private void init(game_service game) {
        String g = game.getGraph();

        String fs = game.getPokemons();
        DWGraph_Algo algo = new DWGraph_Algo();
        System.out.println(game.toString());
        Gson gson = new Gson();
        Type JsonObjectType = new TypeToken<JsonObject>() {}.getType();
        JsonObject gameJsonObject = gson.fromJson(game.toString(), JsonObjectType);
        String filepath = gameJsonObject.get("GameServer").getAsJsonObject().get("graph").getAsString();
        algo.load(filepath);
        directed_weighted_graph gg = algo.getGraph();

        //gg.init(g);
        _ar = new Arena();

        ArrayList<String> arrayList = new ArrayList<String>();

        //arrayList.add(game.move());
        _ar.set_info(arrayList);
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs, gg));
        _win = new MyFrame("test Ex2");

        _win.update(_ar);


        _win.show();
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject gameServer = line.getJSONObject("GameServer");
            int rs = gameServer.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            ArrayList<CL_Pokemon> pokemonArrayList = Arena.json2Pokemons(game.getPokemons(), gg);
            for (int a = 0; a < pokemonArrayList.size(); a++) {
                Arena.updateEdge(pokemonArrayList.get(a), gg);
            }

            //TODO better start location for agents
            PriorityQueue<EdgeValue> edgesQ = new PriorityQueue<>();

            HashMap<edge_data, Double> edgeMap = new HashMap<>();
            HashMap<edge_data, Integer> edgeToType = new HashMap<>();

            List<CL_Pokemon> pokemonsList = Arena.json2Pokemons(fs,gg);

            for (CL_Pokemon pok : pokemonsList) {
                if(edgeMap.containsKey(pok.get_edge())){
                    edgeMap.put(pok.get_edge(),edgeMap.get(pok.get_edge())+pok.getValue());
                    edgeToType.put(pok.get_edge(),pok.getType());
                }else{
                    edgeMap.put(pok.get_edge(),pok.getValue());
                    edgeToType.put(pok.get_edge(),pok.getType());
                }
            }

            for(Map.Entry<edge_data,Double> entry : edgeMap.entrySet()){
                EdgeValue edgeValue = new EdgeValue(entry.getValue(), entry.getKey(), edgeToType.get(entry.getKey()));
                edgesQ.offer(edgeValue);
            }


            for(int a = 0;a<rs;a++) {
                EdgeValue edgeValue = edgesQ.poll();
                if(edgeValue.get_type()<0){
                    game.addAgent(edgeValue.get_edge().getSrc());
                }
                else{
                    game.addAgent(edgeValue.get_edge().getDest());
                }
            }
        }
        catch (JSONException e) {e.printStackTrace();}

        arrayList.add(game.getAgents());
        arrayList.add(game.getGraph());
        arrayList.add(game.getPokemons());
        updateGraphInfo(game,_ar);
    }

    public void mappingAgentToGraph(directed_weighted_graph graph) {
        directed_weighted_graph gg = _ar.getGraph();

        Iterator<node_data> iter = gg.getV().iterator();

        while (iter.hasNext()) {
            node_data n = iter.next();

            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();

            while (itr.hasNext()) {
                edge_data e = itr.next();
                geo_location s = gg.getNode(e.getSrc()).getLocation();
                geo_location d = gg.getNode(e.getDest()).getLocation();

            }
        }

    }
    public void updateGraphInfo(game_service game,Arena ar){
       ArrayList<String> arrayList = new ArrayList<>();
        new Thread() {
            int counter = 10;
            public void run() {
                while(counter >= 0) {
                    arrayList.clear();
                    arrayList.add(game.getAgents());
                    arrayList.add(game.getGraph());
                    arrayList.add(game.getPokemons());
                    ar.set_info(arrayList);
                    try{
                        Thread.sleep(300);
                    } catch(Exception e) {}
                }
            }
        }.start();

    }


}

