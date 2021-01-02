import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Const {

    /**
     * Radius of the physical Ozobot in millimeters
     */
    public static final double ozoRadius = 17;
    /**
     * Width of lines that bots follow, in millimeters
     */
    public static final double ozoLineWidth = 5;
    /**
     * Movement speed of bots when following a line, in millimeters per second
     */
    public static final double ozoMoveSpeed = 30;
    /**
     * Paint to be used for the placement indicator for robots
     */
    public static final Paint startPaint = new Color(0,0.8,0,1);
    /**
     * Paint to be used to draw lines that the robots follow
     */
    public static final Paint linePaint = new Color(0,0,0,1);
    /**
     * Time allowed for human operator to place a new robot, in milliseconds
     */
    public static final long placementWindow = 5000;
    /**
     * Duration of individual color signals to the robot, in milliseconds
     */
    public static final long signalDuration = 777;
    /**
     * Maximum duration of a rotation (--> 180 degrees) of the robot
     */
    public static final long rotationDuration = 2000; //maximum duration of a full (180) rotation of the robot

}
