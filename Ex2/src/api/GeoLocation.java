package api;

public class GeoLocation implements geo_location {
    double x,y,z;

    /**
     * Constructor for 2D location.
     * @param _x
     * @param _y
     */
    public GeoLocation(double _x, double _y){
        this.y = _y;
        this.x = _x;
        this.z =0;
    }

    /**
     * Constructor for 3D location.
     * @param _x
     * @param _y
     * @param _z
     */
    public GeoLocation(double _x, double _y,double _z){
        this.y = _y;
        this.x = _x;
        this.z =_z;
    }

    /**
     * @return the x value of the this location.
     */
    @Override
    public double x() {
        return x;
    }

    /**
     * @return the y value of the this location.
     */
    @Override
    public double y() {
        return y;
    }

    /**
     * @return the z value of the this location.
     */
    @Override
    public double z() {
        return z;
    }

    /**
     * This function calculates the distance from this location to the given location g.
     * @param g
     * @return double value for the distance from this location to g location.
     */
    @Override
    public double distance(geo_location g) {
        double X = x-g.x();
        double Y = y-g.y();
       // double Z = z-g.z();
        X = X*X ; Y = Y*Y ;// Z = Z*Z;
        return Math.sqrt((X+Y));
    }

    /**
     * @return a String for the location values.
     */
    @Override
    public String toString(){
        return this.x + ","+this.y+","+this.z;
    }
}
