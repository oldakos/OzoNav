public class PlaygroundConfig {

    /**
     * Copy constructor
     * @param another The config to copy from.
     */
    public PlaygroundConfig(PlaygroundConfig another){
        this.pxWidth = another.pxWidth;
        this.pxHeight = another.pxHeight;
        this.mmWidth = another.mmWidth;
        this.mmHeight = another.mmHeight;
    }
    public PlaygroundConfig(){}

    public double pxWidth = 1600;
    public double pxHeight = 900;
    public double mmWidth = 460;
    public double mmHeight = 250;
}
