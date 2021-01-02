import static java.lang.Math.atan;
import static java.lang.Math.toDegrees;

public class Util {

    /**
     * Calculate the "angle" of the directed line [x1,y1]->[x2,y2].
     * Straight towards increasing X gives an angle of 0.
     * Straight towards increasing Y gives an angle of 90.
     * @return A double value between 0 and 360.
     */
    public static double calculateAngle(double x1, double y1, double x2, double y2){

        double tan;
        double angle;

        if(x1 == x2){
            if(y1 < y2) return 270;
            else return 90;
        }
        if(x1 < x2){
            if(y1 < y2) {
                tan = (y2-y1) / (x2-x1);
                angle = toDegrees(atan(tan));
                return angle;
            }
            else {
                tan = (y1-y2) / (x2-x1);
                angle = toDegrees(atan(tan));
                return 360 - angle;
            }
        }
        if(x1 > x2){
            if(y1 < y2) {
                tan = (y2-y1) / (x1-x2);
                angle = toDegrees(atan(tan));
                return 180 - angle;
            }
            else {
                tan = (y1-y2) / (x1-x2);
                angle = toDegrees(atan(tan));
                return 180 + angle;
            }
        }
        return 0;   //this line shouldn't be reached but it can't be compiled without it
    }

    /**
     * Calculate the straight line distance between the points [x1,y1], [x2,y2].
     */
    public static double calculateDistance(double x1, double y1, double x2, double y2){
        double x = Math.abs(x1 - x2);
        double y = Math.abs(y1 - y2);
        return Math.hypot(x,y);
    }

    public static double calculateAngle(Ozobot bot, double x, double y){
        return calculateAngle(bot.getX(), bot.getY(), x, y);
    }

    public static double calculateDistance(Ozobot bot, double x, double y){
        return calculateDistance(bot.getX(), bot.getY(), x, y);
    }
}
