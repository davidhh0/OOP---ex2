package gameClient.util;

import api.edge_data;
import org.jetbrains.annotations.NotNull;

public class EdgeValue implements Comparable<EdgeValue>{

    private double _value;
    private edge_data _edge;
    private int _type;

    public EdgeValue(double v, edge_data edge, int type){
        this._edge = edge;
        this._value = v;
        this._type=type;
    }

    public double get_value() {
        return _value;
    }

    public edge_data get_edge() {
        return _edge;
    }

    public int get_type() {
        return _type;
    }


    @Override
    public int compareTo(@NotNull EdgeValue o) {
        if(this.get_value()>o.get_value()) return -1;
        else if(this.get_value()<o.get_value()) return 1;
        return 0;
    }
}
