package api;

import org.jetbrains.annotations.NotNull;

public class NodeData implements node_data {
    private int _tag;
    private int _key;
    private geo_location _location;

    public NodeData(int key){
        _key=key;
        _tag=0;
    }

    @Override
    public int get_key() {
        return _key;
    }
    @Override
    public geo_location get_location() {
        return _location;
    }

    @Override
    public void set_location(geo_location p) {
        _location = p;

    }

    @Override
    public double getWeight() {
        return 0;
    }

    @Override
    public void setWeight(double w) {

    }

    @Override
    public String getInfo() {
        return null;
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

//    @Override
//    public int compareTo(node_data o) {
//        if (o.get_tag() > this.get_tag()) {
//            return -1;
//        } else {
//            return 1;
//        }
//    }
}
