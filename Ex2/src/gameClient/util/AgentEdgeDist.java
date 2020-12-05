package gameClient.util;

import org.jetbrains.annotations.NotNull;

public class AgentEdgeDist implements Comparable<AgentEdgeDist>{

    private int _agentID;
    private double _dist;

    public AgentEdgeDist(int aID, double dist){
        this._agentID = aID;
        this._dist = dist;
    }

    public int get_agentID() {
        return _agentID;
    }

    public double get_dist() {
        return _dist;
    }

    @Override
    public int compareTo(@NotNull AgentEdgeDist o) {
        if(this.get_dist()>o.get_dist()) return 1;
        else if(this.get_dist()<o.get_dist()) return -1;
        return 0;
    }
}
