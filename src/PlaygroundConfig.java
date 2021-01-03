import java.util.Properties;

/**
 * The same as the application Properties but with values already parsed into numeric data types
 */
public class PlaygroundConfig {

    public double pxWidth;
    public double pxHeight;
    public double mmWidth;
    public double mmHeight;
    public double ozoSpeed;
    public long ozoRotationTime;
    public long placementTime;

    /**
     * Copy constructor
     *
     * @param old The config to copy from.
     */
    public PlaygroundConfig(PlaygroundConfig old) {
        this.pxWidth = old.pxWidth;
        this.pxHeight = old.pxHeight;
        this.mmWidth = old.mmWidth;
        this.mmHeight = old.mmHeight;
        this.ozoSpeed = old.ozoSpeed;
        this.ozoRotationTime = old.ozoRotationTime;
        this.placementTime = old.placementTime;
    }

    public PlaygroundConfig(Properties props) {
        pxWidth = Double.parseDouble(props.getProperty("pxwidth"));
        pxHeight = Double.parseDouble(props.getProperty("pxheight"));
        mmWidth = Double.parseDouble(props.getProperty("mmwidth"));
        mmHeight = Double.parseDouble(props.getProperty("mmheight"));
        ozoSpeed = Double.parseDouble(props.getProperty("ozospeed"));
        ozoRotationTime = Long.parseLong(props.getProperty("ozorotationtime"));
        placementTime = Long.parseLong(props.getProperty("placementtime"));
    }
}
