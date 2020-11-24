package api;

public class EdgeData implements edge_data {
    int _src, _dest, _tag;
    double _weight;

    public EdgeData(int src,int dest,double weight){
        _src=src;
        _dest=dest;
        _weight=weight;
        _tag=0;
    }
    @Override
    public int get_src() {
        return _src;
    }

    @Override
    public int get_dest() {
        return _dest;
    }

    @Override
    public double get_weight() {
        return _weight;
    }

    @Override
    public String getInfo() {
        return "Edge from: "+ _src +" to: "+ _dest +". Weight:"+ _weight;
    }

    @Override
    public void setInfo(String s) {

    }

    @Override
    public int get_tag() {
        return _tag;
    }

    @Override
    public void set_tag(int t) {
        _tag =t;
    }
}
