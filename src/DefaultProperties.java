import java.util.Properties;

public class DefaultProperties extends Properties {

    public DefaultProperties() {
        setProperty("pxwidth", "1920");
        setProperty("pxheight", "1080");
        setProperty("mmwidth", "530");
        setProperty("mmheight", "295");
        setProperty("ozospeed", "30");
        setProperty("ozorotationtime", "2000");
        setProperty("placementtime", "5000");
    }

}
