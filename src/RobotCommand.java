/**
 * A simple representation of any single command for a robot.
 */
public class RobotCommand {

    public final int id;
    public final String cmd;
    public final double x;
    public final double y;
    public final double angle;
    public final double distance;

    public RobotCommand(int id, String cmd, double x, double y, double angle, double distance) {
        this.id = id;
        this.cmd = cmd;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.distance = distance;
    }
}
