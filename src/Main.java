import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Properties;

public class Main extends Application {

    private Properties props;
    private Stage mainMenu;

    @Override
    public void start(Stage primaryStage) throws Exception {

        mainMenu = primaryStage;
        initMainMenu();

        props = new DefaultProperties();
        ConfigStage configStage = new ConfigStage(props);
        configStage.show();
    }

    private void initMainMenu() {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        Button btnStartPlayground = new Button("Start the playground");
        btnStartPlayground.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                launchPlayground();
            }
        });
        vbox.getChildren().add(btnStartPlayground);

        Scene scene = new Scene(vbox, 300, 200);
        mainMenu.setScene(scene);
        mainMenu.show();
    }

    private void launchPlayground() {
        PlaygroundConfig cfg = new PlaygroundConfig(props);
        PlaygroundStage stage = new PlaygroundStage(cfg);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.show();
        PlanParserThread parser = new PlanParserThread(stage);
        parser.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
