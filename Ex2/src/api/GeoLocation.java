package api;

public class GeoLocation implements geo_location {
    double x,y,z;

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public double distance(geo_location g) {
        double X = x-g.x();
        double Y = y-g.y();
        double Z = z-g.z();
        X = X*X ; Y = Y*Y ; Z = Z*Z;
        return Math.sqrt((X+Y+Z));
    }
}
