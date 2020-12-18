package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import gameClient.util.DFS_Algo;
import gameClient.util.EdgeValue;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.Type;
import java.util.*;

/**
 * This class is the main engine for the game.
 */
public class Ex2_Client implements Runnable {
    private static MyFrame _win;
    private static EnteringFrame _enterWin;
    private static Arena _ar;
    public static int _numberOfAgents;
    public static long timeToEnd = 0;
    private static HashMap<Integer, Integer> LastOne = new HashMap<>();
    public static HashMap<Integer, EdgeValue> AgentToPok = new HashMap<>();
    public static boolean isLogged = false;
    private static ArrayList<directed_weighted_graph> graphs = new ArrayList<>();
    public static int TzNumber = -1;
    private static int Senario = -1;
    private static boolean isArgs = false;
    private static Object lock = new Object();

    public static void main(String[] args) {
        if (args.length > 1) {
            TzNumber = Integer.parseInt(args[0]);
            Senario = Integer.parseInt(args[1]);
            isArgs = true;
        }
        Thread client = new Thread(new Ex2_Client());
        client.start();
    }

    /**
     *Starting the threaded game -
     * gets the ID and scenario number and starts the game.
     * This method also calls the DFS algorithm to determine how many connected components there are in game.
     */
    @Override
    public void run() {
        if (!isArgs) {
            _enterWin = new EnteringFrame("Enter Ex2");
            Thread t = new Thread() {
                public void run() {
                    synchronized (lock) {
                        while (_enterWin.isVisible())
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                    }
                }
            };
            t.start();
            _enterWin.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentHidden(ComponentEvent e) {
                    super.componentHidden(e);
                    synchronized (lock) {
//                    _enterWin.setVisible(false);
                        TzNumber = _enterWin._id;
                        Senario = _enterWin._sce;
                        System.out.println(TzNumber + "   " + Senario);
                        lock.notifyAll();
                    }
                }
            });
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        game_service game = Game_Server_Ex2.getServer(Senario); // you have [0,23] games
        if (TzNumber > 0) {
            isLogged = game.login(TzNumber);
        }

        init(game);

        DWGraph_Algo algo = new DWGraph_Algo();
        System.out.println(game.toString());
        Gson gson = new Gson();
        Type JsonObjectType = new TypeToken<JsonObject>() {
        }.getType();
        JsonObject gameJsonObject = gson.fromJson(game.toString(), JsonObjectType);
        String filepath = gameJsonObject.get("GameServer").getAsJsonObject().get("graph").getAsString();
        algo.load(filepath);
        directed_weighted_graph gg = algo.getGraph();

        DFS_Algo dfs_algo = new DFS_Algo(gg);
        dfs_algo.StartDFS();
        HashMap<directed_weighted_graph, Integer> graphToNumAgent = new HashMap<>();
        _numberOfAgents = gameJsonObject.get("GameServer").getAsJsonObject().get("agents").getAsInt();

        PriorityQueue<directed_weighted_graph> graphsQ = new PriorityQueue<>(new Comparator<directed_weighted_graph>() {
            @Override
            public int compare(directed_weighted_graph o1, directed_weighted_graph o2) {
                if (o1.nodeSize() > o2.nodeSize()) return 1;
                if (o1.nodeSize() < o2.nodeSize()) return -1;
                return 0;
            }
        });


        if (dfs_algo._graphList.size() > 1) {
            for (directed_weighted_graph g : dfs_algo._graphList) {
                graphsQ.offer(g);
            }
            while (graphsQ.size() > 1) {
                directed_weighted_graph g1 = graphsQ.poll();
                new BreakTheGraph(g1, g1.getV().size(), 1);
                _numberOfAgents--;
                graphs.addAll(BreakTheGraph.graphs);
            }
            directed_weighted_graph g = graphsQ.poll();
            new BreakTheGraph(g, g.getV().size(), _numberOfAgents);
            graphs.addAll(BreakTheGraph.graphs);

        } else {
            new BreakTheGraph(gg, gg.getV().size(), _numberOfAgents);

            graphs.addAll(BreakTheGraph.graphs);
        }

        for (int i = 0; i < graphs.size(); i++) {
            graphToNumAgent.put(graphs.get(i), i);
        }

        for (directed_weighted_graph graph : graphs) {
            init2(game, graph, graphToNumAgent.get(graph));
        }
        game.startGame();
        int ind = 0;
        long dt = 100;

        while (game.isRunning()) {
            String lg = game.move();
            List<CL_Agent> log = Arena.getAgents(lg, gg);
            _ar.setAgents(log);
            for (Map.Entry<directed_weighted_graph, Integer> entry : graphToNumAgent.entrySet()) {
                moveAgants(game, entry.getKey(), entry.getValue(), lg);
            }

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
     * This function determines where each agent goes.
     * There is a Priority Queue for all edges considering the total pokemon values on it.
     * For each agent,
     * the method considers the graph the agent belongs to and adds the value edges to the Priority Queue, considering:
     * The shortest path distance from the agent to the valued edge including the agent speed.
     * Then, the method run through the Priority Queue and sends each agent to the best valued edge FOR it.
     * @param game
     * @param gg
     * @param
     */
    private static void moveAgants(game_service game, directed_weighted_graph gg, int agentID, String lg) {
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs, gg);
        _ar.setPokemons(ffs);
        //Priority queue of edges by value
        PriorityQueue<EdgeValue> edgesQ = new PriorityQueue<>();
        //Map of edge to total val
        HashMap<edge_data, Double> edgeMap = new HashMap<>();
        //Map edge to type
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
        //Adding edge to the queue
        for (Map.Entry<edge_data, Double> entry : edgeMap.entrySet()) {
            CL_Agent agent = getAgent(agentID, lg, _ar.getGraph());
            DWGraph_Algo algo = new DWGraph_Algo();
            algo.init(gg);
            edge_data edge = entry.getKey();
            if (edge != null) {
                double pathDist = (algo.shortestPathDist(agent.getSrcNode(), entry.getKey().getSrc())) / (agent.getSpeed());
                List<node_data> path = algo.shortestPath(agent.getSrcNode(), entry.getKey().getSrc());
                //1-entry
                //bigger value min dist -> value* 1/(dist[0;infinity]*10)
                if (pathDist >= 0 && path.size() > 0) {
                    if (pathDist != 0) {
                        EdgeValue edgeValue = new EdgeValue(entry.getValue() * (1 / (pathDist * 10)), entry.getKey(), edgeToType.get(entry.getKey()));
                        edgesQ.offer(edgeValue);
                    } else {
                        EdgeValue edgeValue = new EdgeValue(entry.getValue(), entry.getKey(), edgeToType.get(entry.getKey()));
                        edgesQ.offer(edgeValue);
                    }
                }
            }

        }
        //Setting type for EdgeValue
        while (!edgesQ.isEmpty()) {
            //Most valued edge
            EdgeValue edge = edgesQ.poll();
            if (edge.get_edge() != null) {
                //edge in the graph
                if (gg.getNode(edge.get_edge().getSrc()) != null) {
                    CL_Agent agent = getAgent(agentID, lg, _ar.getGraph());
                    if (agent.getNextNode() == -1) {
                        int dest = nextNode(gg, agent, edge);
                        int id = agent.getID();
                        AgentToPok.put(agentID, edge);
                        game.chooseNextEdge(id, dest);
                        break;
                    }
                }
            }
        }

    }

    /**
     * @param id
     * @param lg
     * @param gg
     * @return CL_Agent represented by the given id.
     */
    private static CL_Agent getAgent(int id, String lg, directed_weighted_graph gg) {
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        for (CL_Agent ag : log) {
            if (ag.getID() == id) {
                return ag;
            }
        }
        return null;
    }

    /**
     * @param g
     * @param agent
     * @param edgeValue
     * @return the weight path value for CL_Agent agent to the given edgeValue by using shortestpath method.
     */
    private static int nextNode(directed_weighted_graph g, CL_Agent agent, EdgeValue edgeValue) {
        DWGraph_Algo algo = new DWGraph_Algo();
        algo.init(g);
        if (edgeValue.get_type() > 0) {
            if (agent.getSrcNode() == edgeValue.get_edge().getSrc()) {
                return edgeValue.get_edge().getDest();
            }
            List<node_data> path = algo.shortestPath(agent.getSrcNode(), edgeValue.get_edge().getSrc());
            if (path.size() < 1) {
                return -1;
            }
            return path.get(1).getKey();
        } else {
            if (agent.getSrcNode() == edgeValue.get_edge().getDest()) {
                return edgeValue.get_edge().getSrc();
            }
            List<node_data> path = algo.shortestPath(agent.getSrcNode(), edgeValue.get_edge().getDest());
            if (path.size() < 1) {

                return -1;
            }
            return path.get(1).getKey();
        }
    }

    /**
     * This method is an auxiliary for initializing the game.
     * It places the agents on the right spot in the beginning and checks if all the agents are inserted into the game.
     * @param game
     * @param gg
     * @param agentID
     */
    private void init2(game_service game, directed_weighted_graph gg, int agentID) {
        String fs = game.getPokemons();
        int rs = agentID;
        //Placing the agents
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
        int agentNum = 0;
        boolean isAdded = false;
        while (!edgesQ.isEmpty()) {
            EdgeValue edgeValue = edgesQ.poll();
            if (edgeValue.get_edge() != null) {
                if (edgeValue.get_type() < 0) {
                    game.addAgent(edgeValue.get_edge().getSrc());
                    agentNum++;
                    isAdded = true;
                    break;
                } else {
                    game.addAgent(edgeValue.get_edge().getDest());
                    agentNum++;
                    isAdded = true;
                    break;
                }
            }
        }

        if (!isAdded) {
            for (node_data node : gg.getV()) {
                game.addAgent(node.getKey());
                break;
            }
        }
    }

    /**
     * This method initializes the game by setting the graph,agents,pokemons from the given game in a Json format
     * to the Arena parameter.
     * @param game
     */
    private void init(game_service game) {
        String g = game.getGraph();

        String fs = game.getPokemons();
        DWGraph_Algo algo = new DWGraph_Algo();
        System.out.println(game.toString());
        Gson gson = new Gson();
        Type JsonObjectType = new TypeToken<JsonObject>() {
        }.getType();
        JsonObject gameJsonObject = gson.fromJson(game.toString(), JsonObjectType);
        String filepath = gameJsonObject.get("GameServer").getAsJsonObject().get("graph").getAsString();
        algo.load(filepath);
        directed_weighted_graph gg = algo.getGraph();

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

        } catch (JSONException e) {
            e.printStackTrace();
        }

        arrayList.add(game.getAgents());
        arrayList.add(game.getGraph());
        arrayList.add(game.getPokemons());
        updateGraphInfo(game, _ar);
    }

    /**
     * This function updating the graph details every 0.3 seconds in order to show them on the main frame.
     * @param game
     * @param ar
     */
    public void updateGraphInfo(game_service game, Arena ar) {
        ArrayList<String> arrayList = new ArrayList<>();
        new Thread() {
            int counter = 1;

            public void run() {
                while (counter >= 0) {
                    arrayList.clear();
                    arrayList.add(game.getAgents());
                    arrayList.add(game.getGraph());
                    arrayList.add(game.getPokemons());
                    ar.set_info(arrayList);
                    try {
                        Thread.sleep(300);
                    } catch (Exception e) {
                    }
                }
            }
        }.start();

    }


}

