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
    public int getSrc() {
        return _src;
    }

    @Override
    public int getDest() {
        return _dest;
    }

    @Override
    public double getWeight() {
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
    public int getTag() {
        return _tag;
    }

    @Override
    public void setTag(int t) {
        _tag =t;
    }
}
