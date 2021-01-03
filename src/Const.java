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
     * Paint to be used for the placement indicator for robots
     */
    public static final Paint startPaint = new Color(0,0.8,0,1);
    /**
     * Paint to be used to draw lines that the robots follow
     */
    public static final Paint linePaint = new Color(0,0,0,1);
    /**
     * Duration of individual color signals to the robot, in milliseconds
     */
    public static final long signalDuration = 777;
}
