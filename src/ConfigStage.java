import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

/**
 * A form for loading, manipulating, and saving the provided Properties.
 */
public class ConfigStage extends Stage {

    private final String propsFileName = "cfg.properties";

    public ConfigStage(Properties properties) {

        Properties props = properties; //get an effectively final pointer for use inside lambdas

        loadPropertiesFile(props);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label lblPxWidth = new Label("Width (px):");
        grid.add(lblPxWidth, 0, 1);
        TextField txtfldPxWidth = new TextField();
        txtfldPxWidth.setText(props.getProperty("pxwidth"));
        grid.add(txtfldPxWidth, 1, 1);

        Label lblPxHeight = new Label("Height (px):");
        grid.add(lblPxHeight, 0, 2);
        TextField txtfldPxHeight = new TextField();
        txtfldPxHeight.setText(props.getProperty("pxheight"));
        grid.add(txtfldPxHeight, 1, 2);

        Label lblMmWidth = new Label("Width (mm):");
        grid.add(lblMmWidth, 0, 3);
        TextField txtfldMmWidth = new TextField();
        txtfldMmWidth.setText(props.getProperty("mmwidth"));
        grid.add(txtfldMmWidth, 1, 3);

        Label lblMmHeight = new Label("Height (mm):");
        grid.add(lblMmHeight, 0, 4);
        TextField txtfldMmHeight = new TextField();
        txtfldMmHeight.setText(props.getProperty("mmheight"));
        grid.add(txtfldMmHeight, 1, 4);

        Label lblOzoSpeed = new Label("Ozobot line following speed:");
        grid.add(lblOzoSpeed, 0, 5);
        TextField txtfldOzoSpeed = new TextField();
        txtfldOzoSpeed.setText(props.getProperty("ozospeed"));
        grid.add(txtfldOzoSpeed, 1, 5);

        Label lblOzoRotationTime = new Label("Ozobot 180deg rotation time:");
        grid.add(lblOzoRotationTime, 0, 6);
        TextField txtfldOzoRotationTime = new TextField();
        txtfldOzoRotationTime.setText(props.getProperty("ozorotationtime"));
        grid.add(txtfldOzoRotationTime, 1, 6);

        Label lblPlacementTime = new Label("Time to place new bot on screen:");
        grid.add(lblPlacementTime, 0, 7);
        TextField txtfldPlacementTime = new TextField();
        txtfldPlacementTime.setText(props.getProperty("placementtime"));
        grid.add(txtfldPlacementTime, 1, 7);

        Button btnConfirm = new Button("Save and continue");
        btnConfirm.setOnAction(e -> {
            props.setProperty("pxwidth", txtfldPxWidth.getText());
            props.setProperty("pxheight", txtfldPxHeight.getText());
            props.setProperty("mmwidth", txtfldMmWidth.getText());
            props.setProperty("mmheight", txtfldMmHeight.getText());
            props.setProperty("ozospeed", txtfldOzoSpeed.getText());
            props.setProperty("ozorotationtime", txtfldOzoRotationTime.getText());
            props.setProperty("placementtime", txtfldPlacementTime.getText());
            savePropertiesFile(props);
            close();
        });
        grid.add(btnConfirm, 1, 9);

        Scene scene = new Scene(grid, 400, 500);
        setScene(scene);
        setTitle("OzoNav configuration");
        initModality(Modality.APPLICATION_MODAL);
    }

    private void loadPropertiesFile(Properties properties) {

        File propsFile = new File(propsFileName);

        try {
            FileReader reader = new FileReader(propsFile);
            properties.load(reader);
            reader.close();
        } catch (FileNotFoundException ex) {
            // file does not exist - this is fine, we have the default props ready
        } catch (IOException ex) {
            System.out.println("Exception occurred when reading properties file: " + ex.toString());
        }
    }

    private void savePropertiesFile(Properties properties) {
        File propsFile = new File(propsFileName);

        try {
            FileWriter writer = new FileWriter(propsFile, false);
            properties.store(writer, "OzoNav screen and robot configuration");
            writer.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Properties file doesn't exist and cannot be created.");
        } catch (IOException ex) {
            System.out.println("Exception occurred when writing properties file: " + ex.toString());
        }
    }

}
