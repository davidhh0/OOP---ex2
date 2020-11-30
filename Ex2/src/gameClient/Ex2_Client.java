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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Ex2_Client implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;

	private static ArrayList<PokemonEdge> pokemonEdges;
	public static void main(String[] a) {
		Thread client = new Thread(new Ex2_Client());
		client.start();
	}
	
	@Override
	public void run() {
		int scenario_num = 14;
		game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
	//	int id = 999;
	//	game.login(id);
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
		//ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
		String fs =  game.getPokemons();
		List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
		_ar.setPokemons(ffs);
		pokemonEdges = getNextEdge(fs, gg);

		for(int i=0;i<log.size();i++) {
			CL_Agent ag = log.get(i);
			int id = ag.getID();
			int dest = ag.getNextNode();
			int src = ag.getSrcNode();
			double v = ag.getValue();
			if(dest==-1) {
				dest = nextNode(gg, src);
				game.chooseNextEdge(ag.getID(), dest);
				System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
			}
		}
	}

	private static ArrayList<PokemonEdge> getNextEdge(String pokemons, directed_weighted_graph graph){

		Gson gson = new Gson();
		Type JsonObjectType = new TypeToken<JsonObject>() {}.getType();
		ArrayList<PokemonEdge> pokemon_Edges = new ArrayList<>();
		JsonObject poke = gson.fromJson(pokemons, JsonObjectType);
		JsonArray pok_array = poke.get("Pokemons").getAsJsonArray();

		for(JsonElement pok : pok_array){

			//For each pokemon need to find the edge he is on

			JsonObject pokemon_obj = pok.getAsJsonObject();

			JsonObject pokemon = pokemon_obj.get("Pokemon").getAsJsonObject();
			String[] pok_location = pokemon.get("pos").getAsString().split(",");

			double pokemon_x = Double.parseDouble(pok_location[0]);
			double pokemon_y = Double.parseDouble(pok_location[1]);

			geo_location pokemon_geo = new GeoLocation(pokemon_x,pokemon_y,0.0);

			for(node_data node : graph.getV()){

				for(edge_data edge : graph.getE(node.getKey())){
					PokemonEdge pokemonEdge;
					geo_location location1 = graph.getNode(edge.getSrc()).getLocation();
					geo_location location2 = graph.getNode(edge.getDest()).getLocation();


					//Type 1 = from big to small
					//Type -1 = from small to big
					if(Math.abs(location1.distance(pokemon_geo) + pokemon_geo.distance(location2) -  location1.distance(location2)) < 0.00005){
						if(pokemon.get("type").getAsInt() == 1 && edge.getSrc() < edge.getDest()) {
							//System.out.println(String.format("Found the pokemon on edge from %d to %d on coordinates (%f,%f) ", edge.getSrc(), edge.getDest(), (float) pokemon_geo.x(), (float) pokemon_geo.y()));
							pokemonEdge = new PokemonEdge(edge.getSrc(),edge.getDest(),pokemon.get("value").getAsDouble());
							pokemon_Edges.add(pokemonEdge);
						}
						else if(pokemon.get("type").getAsInt() == -1 && edge.getSrc() > edge.getDest()){
							//System.out.println(String.format("Found the pokemon on edge from %d to %d on coordinates (%f,%f) ", edge.getSrc(), edge.getDest(), (float) pokemon_geo.x(), (float) pokemon_geo.y()));
							pokemonEdge = new PokemonEdge(edge.getSrc(),edge.getDest(),pokemon.get("value").getAsDouble());
							pokemon_Edges.add(pokemonEdge);
						}
					}
				}
			}
		}

		return pokemon_Edges;

	}

	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(directed_weighted_graph g, int src) {
		int ans = -1;
		DWGraph_Algo algo = new DWGraph_Algo();
		algo.init(g);
		//Finding the most closest pokemon
		int closestPokIndex = -1;
		double closestPok = Double.MAX_VALUE;
		for (int i = 0; i <pokemonEdges.size(); i++) {
			double dist =  algo.shortestPathDist(src, pokemonEdges.get(i).get_src());
			if(closestPok > algo.shortestPathDist(src, pokemonEdges.get(i).get_src())){
				closestPokIndex = i;
				closestPok = dist;
			}
		}
		System.out.println("Closest Pokemon " +  pokemonEdges.get(closestPokIndex));
		if(src == pokemonEdges.get(closestPokIndex).get_src()){
			return pokemonEdges.get(closestPokIndex).get_dest();
		}

		List<node_data> path = algo.shortestPath(src,pokemonEdges.get(closestPokIndex).get_src());
		return path.get(1).getKey();



//		List<node_data> path = algo.shortestPath(src, pokemonEdges.get(0).get_dest());

//		return path.get(0).getKey();
//		Collection<edge_data> ee = g.getE(src);
//		Iterator<edge_data> itr = ee.iterator();
//		int s = ee.size();
//		int r = (int)(Math.random()*s);
//		int i=0;
//		while(i<r) {itr.next();i++;}
//		ans = itr.next().getDest();
//		return ans;
	}
	private void init(game_service game) {
		String g = game.getGraph();
		String fs = game.getPokemons();
		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
		//gg.init(g);
		_ar = new Arena();
		_ar.setGraph(gg);
		_ar.setPokemons(Arena.json2Pokemons(fs));
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
			ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
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
