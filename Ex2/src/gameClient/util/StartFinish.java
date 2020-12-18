package gameClient.util;

/**
 * This class helps DFS algorithm to mange the discovery time and finish time of each node.
 */
public class StartFinish implements Comparable<StartFinish> {

    private int _start;
    private int _end;
    private int _id;
    /**
     * Default constructor that get the discovery time and finish time and the id of the node.
     * @param id id of node.
     * @param start discovery time.
     * @param end finishing time.
     */
    public StartFinish(int id, int start, int end){
        this._start = start;
        this._end = end;
        this._id = id;
    }

    /**
     * Returns the discovery time
     * @return the discovery time
     */
    public int get_start() {
        return _start;
    }

    /**
     * Setting the discovery time
     * @param _start discovery time
     */
    public void set_start(int _start) {
        this._start = _start;
    }
    /**
     * Returns the finish time
     * @return the finish time
     */
    public int get_end() {
        return _end;
    }

    /**
     * Setting the finish time
     * @param _end the finish time
     */
    public void set_end(int _end) {
        this._end = _end;
    }
    /**
     * Returns the id of node
     * @return the id of node
     */
    public int get_id() {
        return _id;
    }

    /**
     * This is comparator for this class from determinate what StartFinish object is bigger by the end value.
     * @param o
     * @return 1 if this.end bigger then o.end, -1 if o.end bigger then this.end. if equals return 0
     */
    @Override
    public int compareTo(StartFinish o) {
        if(this._end>o._end) return 1;
        if(this._end<o._end) return -1;
        return 0;
    }
}