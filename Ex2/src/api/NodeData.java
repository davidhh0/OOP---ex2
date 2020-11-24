package api;

public class NodeData implements node_data {
    private int Tag;
    private int key;
    private geo_location location;
    @Override
    public int getKey() {
        return key;
    }

    @Override
    public geo_location getLocation() {
        return location;
    }

    @Override
    public void setLocation(geo_location p) {
        location = p;

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
    public int getTag() {
        return Tag;
    }

    @Override
    public void setTag(int t) {
    Tag=t;
    }
}
