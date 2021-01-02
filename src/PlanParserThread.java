import java.util.Scanner;
import java.util.Timer;

/**
 * A thread that scans standard input for robot commands, wraps them in RobotCommand objects, and schedules them on the stage.
 */
public class PlanParserThread extends Thread {

    private long startTime;
    private boolean exitFlag;
    private PlaygroundStage stage;

    public PlanParserThread(PlaygroundStage stage){
        this.stage = stage;
        exitFlag = false;
    }

    private void processLine(String line) throws Exception{
        String[] split = line.split("\\s+");
        if(split[0].equals("end")){
            exitFlag = true;
            return;
        }
        long commandTime = Long.parseLong(split[0]);
        int id = Integer.parseInt(split[1]);
        double x = 0;
        double y = 0;
        double angle = 0;
        double distance = 0;
        switch (split[2]){
            case "add":
                x = Double.parseDouble(split[3]);
                y = Double.parseDouble(split[4]);
                angle = Double.parseDouble(split[5]);
                break;
            case "rRel":    //merge with rAbs branch
            case "rAbs":
                angle = Double.parseDouble(split[3]);
                break;
            case "mvDist":
                distance = Double.parseDouble(split[3]);
                break;
            case "mvTo":
                x = Double.parseDouble(split[3]);
                y = Double.parseDouble(split[4]);
                break;
            default:
                throw new Exception("Unknown command on input: " + split[2]);
        }
        RobotCommand command = new RobotCommand(id, split[2], x, y, angle, distance);
        long remainingTimeToCommand = startTime + commandTime - System.currentTimeMillis();
        if(remainingTimeToCommand < 1) {remainingTimeToCommand = 1;}
        stage.schedule(command, remainingTimeToCommand);
    }

    public void run(){
        try {
            startTime = System.currentTimeMillis();
            Scanner sc = new Scanner(System.in);
            while (!exitFlag && sc.hasNextLine()) {
                processLine(sc.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
