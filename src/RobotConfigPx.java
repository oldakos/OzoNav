public class RobotConfigPx {

    public double getRadius() {
        return radius;
    }
    private double radius;

    public double getLineWidth() {
        return lineWidth;
    }
    private double lineWidth;

    public double getLineSpeed() {
        return lineSpeed;
    }
    private double lineSpeed;

    public RobotConfigPx(RobotConfigPx old){
        radius = old.getRadius();
        lineWidth = old.getLineWidth();
        lineSpeed = old.getLineSpeed();
    };

    public RobotConfigPx(double radius, double lineWidth, double lineSpeed){
        this.radius = radius;
        this.lineWidth = lineWidth;
        this.lineSpeed = lineSpeed;
    };

}
