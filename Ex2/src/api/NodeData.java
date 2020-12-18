package api;

public class NodeData implements node_data {
    private int _tag;
    private int _key;
    private geo_location _location;
    private String _info;

    /**
     * Constructor for a node data based of a given key.
     *
     * @param key
     */
    public NodeData(int key) {
        _key = key;
        _tag = 0;
        _info = "Key: "+key + "Tag: " + _tag;
    }

    /**
     * @return the integer value key of this node.
     */
    @Override
    public int getKey() {
        return _key;
    }

    /**
     * @return the location of this node.
     */
    @Override
    public geo_location getLocation() {
        return _location;
    }

    /**
     * sets a location for this node.
     *
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        _location = p;

    }

    /**
     * @return
     */
    @Override
    public double getWeight() {
        return 0;
    }

    @Override
    public void setWeight(double w) {

    }

    @Override
    public String getInfo() {
        return _info;
    }

    @Override
    public void setInfo(String s) {

    }

    /**
     * @return a tag number represented by an integer value.
     */
    @Override
    public int getTag() {
        return _tag;
    }

    /**
     * sets the tag number for this node.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        _tag = t;
        String s = "Key: "+ _key + "Tag: "+ t;
        _info = s;
    }

}
