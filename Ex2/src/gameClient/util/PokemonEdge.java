package gameClient.util;

public class PokemonEdge {
    private int _src;
    private int _dest;
    private double _val;
    private double _id;

    public PokemonEdge(int src, int dest, double val, double id){
        this._dest = dest;
        this._src = src;
        this._val = val;
        this._id = id;
    }

    public int get_src() {
        return _src;
    }

    public int get_dest() {
        return _dest;
    }

    public double get_val() {
        return _val;
    }

    public double get_id() {
        return _id;
    }

    public String toString(){
        return "("+_src+","+_dest+")";
    }

}
