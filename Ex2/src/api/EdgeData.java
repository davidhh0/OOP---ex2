package api;

public class EdgeData implements edge_data {
    int _src, _dest, _tag;
    double _weight;
    String info;

    /**
     * Constructor for Edge Data:
     * src - the source node that the edge coming from
     * dest - the destination node that the edge reaching to
     * weight - the double value of the weight.
     * @param src
     * @param dest
     * @param weight
     */
    public EdgeData(int src,int dest,double weight){
        _src=src;
        _dest=dest;
        _weight=weight;
        _tag=0;
        info = "Edge from: "+ _src +" to: "+ _dest +". Weight:"+ _weight;
    }

    /**
     * @return the source node represented by an integer of this edge data.
     */
    @Override
    public int getSrc() {
        return _src;
    }

    /**
     * @return the destination node represented by an integer of this edge data.
     */
    @Override
    public int getDest() {
        return _dest;
    }

    /**
     * @return the weight double value this edge data.
     */
    @Override
    public double getWeight() {
        return _weight;
    }

    /**
     * @return general information about this edge data as a string - (src,dest,weight)
     */
    @Override
    public String getInfo() {
        return info;
    }

    /**
     * set the info edge to the given String s
     * @param s
     */
    @Override
    public void setInfo(String s) {
        info = s;
    }

    /**
     * @return the edge data tag number.
     */
    @Override
    public int getTag() {
        return _tag;
    }

    /**
     * set the tag number of this edge data to the given t.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        _tag =t;
    }
}
