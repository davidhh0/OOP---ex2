package gameClient;

import api.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import gameClient.util.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a multi Agents Arena which move on a graph - grabs Pokemons and avoid the Zombies.
 * @author boaz.benmoshe
 *
 */
public class Arena {
	public static final double EPS1 = 0.001, EPS2=EPS1*EPS1, EPS=EPS2;
	private directed_weighted_graph _gg;
	private List<CL_Agent> _agents;
	private List<CL_Pokemon> _pokemons;
	private List<String> _info;
	private static Point3D MIN = new Point3D(0, 100,0);
	private static Point3D MAX = new Point3D(0, 100,0);

	public Arena() {;
		_info = new ArrayList<String>();
	}
	private Arena(directed_weighted_graph g, List<CL_Agent> r, List<CL_Pokemon> p) {
		_gg = g;
		this.setAgents(r);
		this.setPokemons(p);
	}
	public void setPokemons(List<CL_Pokemon> f) {
		this._pokemons = f;
	}
	public void setAgents(List<CL_Agent> f) {
		this._agents = f;
	}
	public void setGraph(directed_weighted_graph g) {this._gg =g;}//init();}
	private void init( ) {
		MIN=null; MAX=null;
		double x0=0,x1=0,y0=0,y1=0;
		Iterator<node_data> iter = _gg.getV().iterator();
		while(iter.hasNext()) {
			geo_location c = iter.next().getLocation();
			if(MIN==null) {x0 = c.x(); y0=c.y(); x1=x0;y1=y0;MIN = new Point3D(x0,y0);}
			if(c.x() < x0) {x0=c.x();}
			if(c.y() < y0) {y0=c.y();}
			if(c.x() > x1) {x1=c.x();}
			if(c.y() > y1) {y1=c.y();}
		}
		double dx = x1-x0, dy = y1-y0;
		MIN = new Point3D(x0-dx/10,y0-dy/10);
		MAX = new Point3D(x1+dx/10,y1+dy/10);
		
	}
	public List<CL_Agent> getAgents() {return _agents;}
	public List<CL_Pokemon> getPokemons() {return _pokemons;}

	
	public directed_weighted_graph getGraph() {
		return _gg;
	}
	public List<String> get_info() {
		return _info;
	}
	public void set_info(List<String> _info) {
		this._info = _info;
	}

	////////////////////////////////////////////////////
	public static List<CL_Agent> getAgents(String aa, directed_weighted_graph gg) {
		ArrayList<CL_Agent> ans = new ArrayList<CL_Agent>();
		try {
			JSONObject ttt = new JSONObject(aa);
			JSONArray ags = ttt.getJSONArray("Agents");
			for(int i=0;i<ags.length();i++) {
				CL_Agent c = new CL_Agent(gg,0);
				c.update(ags.get(i).toString());
				ans.add(c);
			}
			//= getJSONArray("Agents");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ans;
	}
	public static ArrayList<CL_Pokemon> json2Pokemons(String fs, directed_weighted_graph graph) {
		ArrayList<CL_Pokemon> ans = new  ArrayList<CL_Pokemon>();
		try {
			JSONObject ttt = new JSONObject(fs);
			JSONArray ags = ttt.getJSONArray("Pokemons");
			for(int i=0;i<ags.length();i++) {
				JSONObject pp = ags.getJSONObject(i);
				JSONObject pk = pp.getJSONObject("Pokemon");
				int t = pk.getInt("type");
				double v = pk.getDouble("value");
				//double s = 0;//pk.getDouble("speed");

				String p = pk.getString("pos");
				edge_data edge = FindPokemonsLocation(p,graph, t);
				String[] pok_location = p.split(",");

				double pokemon_x = Double.parseDouble(pok_location[0]);
				double pokemon_y = Double.parseDouble(pok_location[1]);
				int id = (int)(t * v * (pokemon_x - pokemon_y)*1000000);

				CL_Pokemon f = new CL_Pokemon(new Point3D(p), t, v, 0, edge,id);
				ans.add(f);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
		return ans;
	}
	private static edge_data FindPokemonsLocation(String pos, directed_weighted_graph graph, int type) {

		//For each pokemon need to find the edge he is on


		String[] pok_location = pos.split(",");

		double pokemon_x = Double.parseDouble(pok_location[0]);
		double pokemon_y = Double.parseDouble(pok_location[1]);

		geo_location pokemon_geo = new GeoLocation(pokemon_x, pokemon_y, 0.0);

		for (node_data node : graph.getV()) {

			for (edge_data edge : graph.getE(node.getKey())) {
				geo_location location1 = graph.getNode(edge.getSrc()).getLocation();
				geo_location location2 = graph.getNode(edge.getDest()).getLocation();


				//Type 1 = from big to small
				//Type -1 = from small to big

				if (Math.abs(location1.distance(pokemon_geo) + pokemon_geo.distance(location2) - location1.distance(location2)) < Math.exp(-35)) {
//					if(type == 1 && edge.getSrc() < edge.getDest()) {
//						//System.out.println(String.format("Found the pokemon on edge from %d to %d on coordinates (%f,%f) ", edge.getSrc(), edge.getDest(), (float) pokemon_geo.x(), (float) pokemon_geo.y()));
//
////						int id = (int)(pokemon.get("type").getAsInt() * pokemon.get("value").getAsInt() * (pokemon_x - pokemon_y)*1000000);
//						pokemonEdge = new PokemonEdge(edge.getSrc(),edge.getDest(),pokemon.get("value").getAsDouble(),id);
//						pokemon_Edges.add(pokemonEdge);
//						graph.getEdge()
//					}
//					else if(type == -1 && edge.getSrc() > edge.getDest()){
//						//System.out.println(String.format("Found the pokemon on edge from %d to %d on coordinates (%f,%f) ", edge.getSrc(), edge.getDest(), (float) pokemon_geo.x(), (float) pokemon_geo.y()));
//						int id = (int)(pokemon.get("type").getAsInt() * pokemon.get("value").getAsInt() * (pokemon_x - pokemon_y)*1000000);
//						pokemonEdge = new PokemonEdge(edge.getSrc(),edge.getDest(),pokemon.get("value").getAsDouble(),id);
//						pokemon_Edges.add(pokemonEdge);
//					}
					edge_data edgeData;
					if(type==-1){
						edgeData = new EdgeData(edge.getSrc(),edge.getDest(),edge.getWeight());
					}
					else{
						edgeData = new EdgeData(edge.getDest(),edge.getSrc(),edge.getWeight());
					}

					return edgeData;
				}
			}
		}
		return null;

	}


	public static void updateEdge(CL_Pokemon fr, directed_weighted_graph g) {
		//	oop_edge_data ans = null;
		Iterator<node_data> itr = g.getV().iterator();
		while(itr.hasNext()) {
			node_data v = itr.next();
			Iterator<edge_data> iter = g.getE(v.getKey()).iterator();
			while(iter.hasNext()) {
				edge_data e = iter.next();
				boolean f = isOnEdge(fr.getLocation(), e,fr.getType(), g);
				if(f) {fr.set_edge(e);}
			}
		}
	}

	private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest ) {

		boolean ans = false;
		double dist = src.distance(dest);
		double d1 = src.distance(p) + p.distance(dest);
		if(dist>d1-EPS2) {ans = true;}
		return ans;
	}
	private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
		geo_location src = g.getNode(s).getLocation();
		geo_location dest = g.getNode(d).getLocation();
		return isOnEdge(p,src,dest);
	}
	private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
		int src = g.getNode(e.getSrc()).getKey();
		int dest = g.getNode(e.getDest()).getKey();
		if(type<0 && dest>src) {return false;}
		if(type>0 && src>dest) {return false;}
		return isOnEdge(p,src, dest, g);
	}

	private static Range2D GraphRange(directed_weighted_graph g) {
		Iterator<node_data> itr = g.getV().iterator();
		double x0=0,x1=0,y0=0,y1=0;
		boolean first = true;
		while(itr.hasNext()) {
			geo_location p = itr.next().getLocation();
			if(first) {
				x0=p.x(); x1=x0;
				y0=p.y(); y1=y0;
				first = false;
			}
			else {
				if(p.x()<x0) {x0=p.x();}
				if(p.x()>x1) {x1=p.x();}
				if(p.y()<y0) {y0=p.y();}
				if(p.y()>y1) {y1=p.y();}
			}
		}
		Range xr = new Range(x0,x1);
		Range yr = new Range(y0,y1);
		return new Range2D(xr,yr);
	}
	public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
		Range2D world = GraphRange(g);
		Range2Range ans = new Range2Range(world, frame);
		return ans;
	}

}
