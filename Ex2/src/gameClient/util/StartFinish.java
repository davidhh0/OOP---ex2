package gameClient.util;


public class StartFinish implements Comparable<StartFinish> {

    private int _start;
    private int _end;
    private int _id;

    public StartFinish(int id, int start, int end){
        this._start = start;
        this._end = end;
        this._id = id;
    }
    public int get_start() {
        return _start;
    }

    public void set_start(int _start) {
        this._start = _start;
    }

    public int get_end() {
        return _end;
    }

    public void set_end(int _end) {
        this._end = _end;
    }

    public int get_id() {
        return _id;
    }

    @Override
    public int compareTo(StartFinish o) {
        if(this._end>o._end) return 1;
        if(this._end<o._end) return -1;
        return 0;
    }
}