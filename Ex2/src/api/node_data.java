package api;

/**
 * This interface represents the set of operations applicable on a 
 * node (vertex) in a (directional) weighted graph.
 * @author boaz.benmoshe
 *
 */
public interface node_data {
	/**
	 * Returns the key (id) associated with this node.
	 * @return
	 */
	public int get_key();
	/** Returns the location of this node, if
	 * none return null.
	 * 
	 * @return
	 */
	public geo_location get_location();
	/** Allows changing this node's location.
	 * @param p - new new location  (position) of this node.
	 */
	public void set_location(geo_location p);
	/**
	 * Returns the weight associated with this node.
	 * @return
	 */
	public double getWeight();
	/**
	 * Allows changing this node's weight.
	 * @param w - the new weight
	 */
	public void setWeight(double w);
	/**
	 * Returns the remark (meta data) associated with this node.
	 * @return
	 */
	public String getInfo();
	/**
	 * Allows changing the remark (meta data) associated with this node.
	 * @param s
	 */
	public void setInfo(String s);
	/**
	 * Temporal data (aka color: e,g, white, gray, black) 
	 * which can be used be algorithms 
	 * @return
	 */
	public int get_tag();
	/** 
	 * Allows setting the "tag" value for temporal marking an node - common
	 * practice for marking by algorithms.
	 * @param t - the new value of the tag
	 */
	public void set_tag(int t);
}
