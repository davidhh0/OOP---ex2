package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import gameClient.util.PokemonEdge;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.*;

public class Ex2_Client implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;

	private static HashMap<Integer, Integer> PokemonToAgent = new HashMap<>();
	private static HashMap<Integer, List<node_data>> AgentToPath = new HashMap<>();
	private static HashMap<Integer, Integer> LastOne = new HashMap<>();


	public static void main(String[] a) {
		Thread client = new Thread(new Ex2_Client());
		client.start();
	}

	@Override
	public void run() {
		int scenario_num = 11;
		game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
	 	//int id = 999;
		//game.login(id);
		String g = game.getGraph();
		String pks = game.getPokemons();
		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
		init(game);

		game.startGame();
		_win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
		int ind=0;
		long dt=100;

		while(game.isRunning()) {
			moveAgants(game, gg);
			try {
				if(ind%1==0) {_win.repaint();}
				Thread.sleep(dt);
				ind++;
			}
			catch(Exception e) {
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
	 * @param game
	 * @param gg
	 * @param
	 */
	private static void moveAgants(game_service game, directed_weighted_graph gg) {
		String lg = game.move();
		List<CL_Agent> log = Arena.getAgents(lg, gg);
		_ar.setAgents(log);
		String fs =  game.getPokemons();
		List<CL_Pokemon> ffs = Arena.json2Pokemons(fs,gg);
		_ar.setPokemons(ffs);

		//Priority queue from slowest agent to the fastest
		PriorityQueue<CL_Agent> agentsQ = new PriorityQueue<>(new Comparator<CL_Agent>() {
			@Override
			public int compare(CL_Agent o1, CL_Agent o2) {
				if(o1.getSpeed()>o2.getSpeed()) return 1;
				if(o1.getSpeed()<o2.getSpeed()) return -1;
				return 0;
			}
		});
		//Adding all agents
		for(CL_Agent agent : log){
			agentsQ.add(agent);
		}
		List<CL_Pokemon> pokemonsList = Arena.json2Pokemons(fs,gg);

		while(!agentsQ.isEmpty()){
			CL_Agent ag = agentsQ.poll();
			int id = ag.getID();
			int dest = ag.getNextNode();
			int src = ag.getSrcNode();
			double v = ag.getValue();
			if(dest==-1) {
				dest = nextNode(gg, src, pokemonsList, id);
				game.chooseNextEdge(ag.getID(), dest);
				//System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
			}
		}
	}

	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(directed_weighted_graph g, int src, List<CL_Pokemon> pokemonList, int agentID) {

		int ans = -1;
		//If Last one return dest
		if(LastOne.containsKey(agentID)){
			int pokID = LastOne.get(agentID);
			LastOne.remove(agentID);
			for(CL_Pokemon pok : pokemonList){
				if(pok.get_id()==pokID){
					return pok.get_edge().getDest();
				}
			}
		}


		//If agent has path to pokemon
		if(AgentToPath.containsKey(agentID) && AgentToPath.get(agentID).size()>0){
			List<node_data> path= AgentToPath.get(agentID);
			node_data element = path.remove(0);
			//Was the last one
			if(AgentToPath.get(agentID).size()==0){
				int pokID = -1;
				for(Map.Entry<Integer,Integer> entry : PokemonToAgent.entrySet()){
					if(entry.getValue() == agentID){
						pokID=entry.getKey();
						break;
					}
				}
				LastOne.put(agentID,pokID);
				PokemonToAgent.remove(pokID);
			}
			return element.getKey();
		}
		//If dosent have path to pokemon

		DWGraph_Algo algo = new DWGraph_Algo();
		algo.init(g);

		//Finding the most closest pokemon
		int closestPokIndex = -1;
		double closestPok = Double.MAX_VALUE;


		for (int i = 0; i <pokemonList.size(); i++) {
			double dist =  algo.shortestPathDist(src, pokemonList.get(i).get_edge().getSrc());
			if(closestPok > algo.shortestPathDist(src, pokemonList.get(i).get_edge().getSrc()) && !PokemonToAgent.containsKey(pokemonList.get(i).get_id())){
				closestPokIndex = i;
				closestPok = dist;
			}
		}

		//if found the pokemon
		if(closestPokIndex!=-1){
			PokemonToAgent.put(pokemonList.get(closestPokIndex).get_id(), agentID);
			List<node_data> path = algo.shortestPath(src, pokemonList.get(closestPokIndex).get_edge().getSrc());
			node_data first = path.remove(0);
			AgentToPath.put(agentID,path);
			return first.getKey();
		}
		return -1;

	}

	private void init(game_service game) {
		String g = game.getGraph();
		String fs = game.getPokemons();
		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
		//gg.init(g);
		_ar = new Arena();
		_ar.setGraph(gg);
		_ar.setPokemons(Arena.json2Pokemons(fs,gg));
		_win = new MyFrame("test Ex2");
		_win.setSize(1000, 700);
		_win.update(_ar);

	
		_win.show();
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("agents");
			System.out.println(info);
			System.out.println(game.getPokemons());
			int src_node = 0;  // arbitrary node, you should start at one of the pokemon
			ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons(),gg);
			for(int a = 0;a<cl_fs.size();a++) { Arena.updateEdge(cl_fs.get(a),gg);}
			for(int a = 0;a<rs;a++) {
				int ind = a%cl_fs.size();
				CL_Pokemon c = cl_fs.get(ind);
				int nn = c.get_edge().getDest();
				if(c.getType()<0 ) {nn = c.get_edge().getSrc();}
				
				game.addAgent(nn);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
	}
}
