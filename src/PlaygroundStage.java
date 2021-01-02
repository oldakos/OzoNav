import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.*;

public class PlaygroundStage extends Stage {

    /**
     * Calculated as an average of these ratios in width and height of the monitor given in config - the user probably made some measurement errors.
     * Assumes that the monitor has square pixels.
     */
    private double averagePixelsPerMillimeter;
    private PlaygroundConfig config;
    private ArrayList<Ozobot> agents;
    private Pane root;
    private double pxOzobotRadius;
    private double pxLineWidth;
    private Timer timer;

    public PlaygroundStage(PlaygroundConfig cfg) {
        config = new PlaygroundConfig(cfg); //using the copy ctor
        averagePixelsPerMillimeter = ((config.pxHeight / config.mmHeight) + (config.pxWidth / config.mmWidth)) / 2;
        agents = new ArrayList<Ozobot>();
        root = new Pane();
        pxOzobotRadius = lengthToPx(Const.ozoRadius);
        pxLineWidth = lengthToPx(5);

        timer = new Timer();
        setOnCloseRequest((WindowEvent we) -> {
            timer.cancel();
        });

        Scene scene = new Scene(root, config.pxWidth, config.pxHeight);
        scene.setCursor(Cursor.CROSSHAIR);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.ESCAPE) {
                    timer.cancel();
                    ke.consume();
                    close();
                }
                if (ke.getCode() == KeyCode.R) {
                    rotateAndMoveBotMilli(0, 300, 150);
                }
                if (ke.getCode() == KeyCode.E) {
                    rotateAndMoveBotMilli(0, 100, 100);
                }
                if (ke.getCode() == KeyCode.W) {
                    rotateAndMoveBotMilli(0, 200, 200);
                }
            }
        });
        setScene(scene);
        setFullScreenExitHint("");
        //setFullScreen(true);
    }

    public double lengthToPx(double mm) {
        return mm * averagePixelsPerMillimeter;
    }

    public double lengthToMm(double px) {
        return px / averagePixelsPerMillimeter;
    }

    /**
     * Introduces a new agent to the playground on the given position facing the given direction.
     *
     * @param id    The number of the new agent.
     * @param x     X coordinate in millimeters.
     * @param y     Y coordinate in millimeters.
     * @param angle The angle the agent is facing, in degrees. 0 is straight towards increasing X, 270 is straight towards increasing Y.
     */
    public void createBotMilli(int id, double x, double y, double angle) {
        createBotPx(id, lengthToPx(x), lengthToPx(y), angle);
    }

    /**
     * Introduces a new agent to the playground on the given position facing the given direction.
     *
     * @param id    The number of the new agent.
     * @param pxX   X coordinate in pixels.
     * @param pxY   Y coordinate in pixels.
     * @param angle The angle the agent is facing, in degrees. 0 is straight towards increasing X, 90 is straight towards increasing Y.
     */
    public void createBotPx(int id, double pxX, double pxY, double angle) {

        RobotConfigPx cfg = new RobotConfigPx(lengthToPx(Const.ozoRadius), lengthToPx(Const.ozoLineWidth), lengthToPx(Const.ozoMoveSpeed));

        Ozobot bot = new Ozobot(pxX, pxY, cfg, angle, root, timer);
        agents.add(bot);

        //though we could draw the arrow within Ozobot ctor, here we can use millimeter measurements easily and the Ozobot never needs an arrow again
        Polygon arrow = new Polygon();
        arrow.getPoints().addAll(new Double[]{
                pxX + pxOzobotRadius, pxY + lengthToPx(2),
                pxX + pxOzobotRadius, pxY - lengthToPx(2),
                pxX + pxOzobotRadius + lengthToPx(8), pxY
        });
        arrow.setFill(Const.startPaint);
        Rotate r = new Rotate();
        r.setAngle(angle);
        r.setPivotX(pxX);
        r.setPivotY(pxY);
        arrow.getTransforms().add(r);

        TimerTask hideCircle = new GUITimerTask(() -> {
            arrow.setVisible(false);
            bot.hideCircle();
        });
        timer.schedule(hideCircle, Const.placementWindow);
        root.getChildren().add(arrow);
    }

    /**
     * Line up and move the identified robot to the coordinates given in millimeters.
     */
    public void rotateAndMoveBotMilli(int id, double x, double y) {
        rotateAndMoveBotPx(id, lengthToPx(x), lengthToPx(y));
    }

    /**
     * Line up and move the identified robot to the coordinates given in pixels.
     */
    public void rotateAndMoveBotPx(int id, double pxX, double pxY) {
        Ozobot bot = agents.get(id);
        bot.signalRotateAndMoveTowards(pxX, pxY);
    }

    public void moveBotForwardMilli(int id, double mmDistance) {
        moveBotForwardPx(id, lengthToPx(mmDistance));
    }

    public void moveBotForwardPx(int id, double pxDistance) {
        Ozobot bot = agents.get(id);
        bot.signalFollowLine(pxDistance);
    }

    public void rotateBotAbsolute(int id, double angle) {
        Ozobot bot = agents.get(id);
        bot.signalRotationAbsolute(angle);
    }

    public void rotateBotRelative(int id, double angle) {
        Ozobot bot = agents.get(id);
        bot.signalRotationRelative(angle);
    }

    public void schedule(RobotCommand command, long delay) {
        TimerTask task;
        switch (command.cmd) {
            case "add":
                task = new GUITimerTask(() -> {
                    createBotMilli(command.id, command.x, command.y, command.angle);
                });
                break;
            case "rRel":
                task = new GUITimerTask(() -> {
                    rotateBotRelative(command.id, command.angle);
                });
                break;
            case "rAbs":
                task = new GUITimerTask(() -> {
                    rotateBotAbsolute(command.id, command.angle);
                });
                break;
            case "mvDist":
                task = new GUITimerTask(() -> {
                    moveBotForwardMilli(command.id, command.distance);
                });
                break;
            case "mvTo":
                task = new GUITimerTask(() -> {
                    rotateAndMoveBotMilli(command.id, command.x, command.y);
                });
                break;
            default:
                return;
                //unknown command
        }
        timer.schedule(task, delay);
    }
}
