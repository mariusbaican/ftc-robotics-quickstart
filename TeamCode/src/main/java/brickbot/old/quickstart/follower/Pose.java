package brickbot.old.quickstart.follower;

public class Pose {
    private double x;
    private double y;
    private double heading;

    public Pose(double x, double y, double heading){
        this.x = x;
        this.y = y;
        this.heading = heading;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getHeading() {
        return heading;
    }
}
