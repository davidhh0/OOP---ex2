package gameClient.util;

public class PokemonEdge {
    private int _src;
    private int _dest;
    private double _val;


    public PokemonEdge(int src, int dest, double val){
        this._dest = dest;
        this._src = src;
        this._val = val;
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

    public String toString(){
        return "("+_src+","+_dest+")";
    }

}
