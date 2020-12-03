package gameClient.util;

public class AgentMap {

    private int _id;
    private double _dist;

    public AgentMap(int id, double dist){
        this._dist = dist;
        this._id = id;
    }

    public int get_id() {
        return _id;
    }

    public double get_dist() {
        return _dist;
    }

}
