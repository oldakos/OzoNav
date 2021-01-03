public class RobotConfigPx {

    private final double radius;
    private final double lineWidth;

    public double getLineWidth() {
        return lineWidth;
    }

    private final double lineSpeed;

    public double getLineSpeed() {
        return lineSpeed;
    }

    private final long rotationTime;

    public RobotConfigPx(RobotConfigPx old) {
        radius = old.getRadius();
        lineWidth = old.getLineWidth();
        lineSpeed = old.getLineSpeed();
        rotationTime = old.getRotationTime();
    }

    public RobotConfigPx(double radius, double lineWidth, double lineSpeed, long rotationTime) {
        this.radius = radius;
        this.lineWidth = lineWidth;
        this.lineSpeed = lineSpeed;
        this.rotationTime = rotationTime;
    }

    public double getRadius() {
        return radius;
    }

    public long getRotationTime() {
        return rotationTime;
    }

}
