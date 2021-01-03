import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class represents an Ozobot in "pixel space" and contains and manages its related graphical components.
 */
public class Ozobot {

    /**
     * Used for signalling to the robot.
     */
    private final Circle circle;

    /**
     * Used to guide the robot's movement.
     */
    private final Rectangle line;

    /**
     * Used to mark the end of a movement line.
     */
    private final Rectangle endLine;

    public double getX() {
        return x;
    }

    /**
     * The x coord in pixels where the robot was last stationary.
     */
    private double x;

    public double getY() {
        return y;
    }
    /**
     * The y coord in pixels where the robot was last stationary.
     */
    private double y;

    /**
     * The angle the robot is facing. 0 is straight towards increasing x, 90 is straight towards increasing y.
     */
    private double angle;

    private final Timer timer;

    private final RobotConfigPx cfg;

    /**
     * Construct a new instance of a robot in "pixel space".
     * @param x Starting X coord.
     * @param y Starting Y coord.
     * @param configPx Object describing the robot's dimensions and speeds in pixels.
     * @param angle Starting angle to face.
     * @param root The Pane to contain the robot's graphics objects.
     * @param timer The Timer the robot will use for timed procedures and animations.
     */
    public Ozobot(double x, double y, RobotConfigPx configPx, double angle, Pane root, Timer timer){

        this.x = x;
        this.y = y;
        this.cfg = new RobotConfigPx(configPx);
        this.timer = timer;

        circle = new Circle(x, y,cfg.getRadius() + 2);
        circle.setFill(new Color(0,0,0,0));
        circle.setStroke(Const.startPaint);
        circle.setStrokeWidth(2);
        circle.setVisible(true);

        line = new Rectangle(x - cfg.getRadius(), y - (cfg.getLineWidth() / 2), 2 * cfg.getRadius(), cfg.getLineWidth());
        line.setFill(Const.linePaint);
        line.setVisible(false);

        endLine = new Rectangle(x - cfg.getLineWidth(), y - (cfg.getLineWidth() * 2), cfg.getLineWidth(), cfg.getLineWidth() * 4);
        endLine.setFill(Const.linePaint);
        endLine.setVisible(false);

        setRotate(angle);

        root.getChildren().addAll(circle, line, endLine);
    }

    private void setRotate(double angle){
        this.angle = angle;

        Rotate r = new Rotate();
        r.setAngle(angle);
        r.setPivotX(x);
        r.setPivotY(y);

        line.getTransforms().clear();
        line.getTransforms().add(r);
    }

    private void updatePositionRelative(double dx, double dy) {
        updatePositionAbsolute(x + dx, y + dy);
    }

    private void updatePositionAbsolute(double x, double y) {

        circle.setCenterX(x);
        circle.setCenterY(y);
        line.setX(line.getX() - this.x + x);
        line.setY(line.getY() - this.y + y);

        //doesn't move the endLine which is instead moved in advance via `showEndLine`

        this.x = x;
        this.y = y;

        setRotate(angle); //to update the pivot points of rotations of components even though this.angle stays the same
    }

    /**
     * Moving and rotating the endLine as if through `moveAbsolute` and `setRotate`, but independently of the other parts.
     */
    private void showEndLine(double x, double y){
        endLine.setX(endLine.getX() - this.x + x);
        endLine.setY(endLine.getY() - this.y + y);
        Rotate r = new Rotate();
        r.setAngle(angle);
        r.setPivotX(x);
        r.setPivotY(y);
        endLine.getTransforms().clear();
        endLine.getTransforms().add(r);
        endLine.setVisible(true);
    }

    private void hideLine(){
        line.setVisible(false);
        endLine.setVisible(false);
    }

    public void hideCircle(){
        circle.setVisible(false);
        circle.setStroke(new Color(0.2, 0.2, 0.2, 1));
    }

    /**
     * Calculate which one of the robot's 6 possible sixth-of-a-circle changes is closest to target change and send the appropriate signals.
     * Depends on the robot's ability to start following a line that's within 30 degrees of the direction it is facing.
     */
    public void signalRotationRelative(double deltaAngle){

        while(deltaAngle > 180) deltaAngle = deltaAngle - 360;
        while(deltaAngle < -180) deltaAngle = deltaAngle + 360;
        //now delta is between -180 and 180

        Paint signalPaint = new Color(1,0,0,1);
        Paint signalPaint2 = new Color(0,0,0,0);

        //determine the appropriate signal
        if(deltaAngle < -150 || deltaAngle >= 150) {signalPaint2 = new Color(1,0,1,1);}
        else if(deltaAngle < -90) {signalPaint2 = new Color(0,0,0,1);}
        else if(deltaAngle < -30) {signalPaint2 = new Color(1,1,0,1);}
        else if(deltaAngle < 30) {signalPaint = new Color(0,0,0,0);} //this is "no operation" - change the FIRST signal to "none" and leave the second signal "none"
        else if(deltaAngle < 90) {signalPaint2 = new Color(0,1,0,1);}
        else {signalPaint2 = new Color(0,0,1,1);} //"else" ~ between 90 and 150

        Paint signalPaint2final = signalPaint2; //this needs to be "effectively final" in order to be allowed in the TimerTask

        //animate the appropriate signals
        TimerTask hideCircle = new GUITimerTask(() -> {
            circle.setVisible(false);
        });
        TimerTask rotateSignal2 = new GUITimerTask(() -> {
            circle.setFill(signalPaint2final);
            timer.schedule(hideCircle, Const.signalDuration);
        });
        circle.setFill(signalPaint);
        circle.setVisible(true);
        timer.schedule(rotateSignal2, Const.signalDuration);

        setRotate(this.angle + deltaAngle);
    }

    public void signalRotationAbsolute(double targetAngle){
        double delta = targetAngle - angle;
        signalRotationRelative(delta);
    }

    public void signalRotationTowards(double x, double y){
        double angle = Util.calculateAngle(this, x, y);
        signalRotationAbsolute(angle);
    }

    public void signalRotateAndMoveTowards(double x, double y){
        signalRotationTowards(x,y);
        TimerTask followLine = new GUITimerTask(() -> {
            signalFollowLine(Util.calculateDistance(this, x, y));
        });
        timer.schedule(followLine, 2 * Const.signalDuration + cfg.getRotationTime());
    }

    public void signalFollowLine(double distance){

        //The animation consists of two parts: first, move only the Line, and then, shortly before the end, show the intersecting line and continue to move the line.
        //Every local variable named like ~var1 or ~var2 is related only to the 1st or 2nd part of the animation.

        double distance2 = 1.5 * cfg.getRadius();

        /**
         * The length of the second animation part divided by length of the whole animation.
         */
        double ratio = distance2 / distance;

        double duration = 1000 * distance / cfg.getLineSpeed(); //bot movespeed is per seconds but we need milliseconds, hence *1000
        double duration2 = ratio * duration;

        double dx = distance * (Math.cos(Math.toRadians(angle)));   //total difference in coordinates between start and end of movement
        double dy = distance * (Math.sin(Math.toRadians(angle)));
        double dx2 = ratio * dx;
        double dy2 = ratio * dy;

        double targetX = x + dx;
        double targetY = y + dy;

        TranslateTransition tt2 = getRelativeLinearTranslation(dx2, dy2, duration2, line);
        TranslateTransition tt1 = getRelativeLinearTranslation(dx - dx2, dy - dy2, duration - duration2, line);

        line.setTranslateX(0);  //nullify the result of any previous animations because they were relative to now-outdated positions of the robot
        line.setTranslateY(0);

        TimerTask endAnimations = new GUITimerTask(() -> {
            hideLine();
            updatePositionAbsolute(targetX, targetY);
        });
        TimerTask animation2 = new GUITimerTask(() -> {
            showEndLine(targetX, targetY);
            timer.schedule(endAnimations, (long) (duration2));
            tt2.play();
        });
        TimerTask animation1 = new GUITimerTask(() -> {
            timer.schedule(animation2, (long) (duration - duration2));
            tt1.play();
        });
        TimerTask showLineAndWait = new GUITimerTask(() -> {
            circle.setVisible(false);
            line.setVisible(true);
            timer.schedule(animation1, 200);
        });
        timer.schedule(showLineAndWait, Const.signalDuration);
        circle.setFill(new Color(0,0,1,1));
        circle.setVisible(true);
    }

    private TranslateTransition getRelativeLinearTranslation(double byX, double byY, double duration, Node node){
        TranslateTransition tt = new TranslateTransition();
        tt.setDuration(Duration.millis(duration));
        tt.setInterpolator(Interpolator.LINEAR);
        tt.setNode(node);
        tt.setByX(byX);
        tt.setByY(byY);
        tt.setCycleCount(1);

        return tt;
    }

}
