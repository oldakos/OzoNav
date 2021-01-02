import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = new GridPane();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        //===============================

        PlaygroundConfig cfg = new PlaygroundConfig();
        PlaygroundStage stage = new PlaygroundStage(cfg);
        stage.setTitle("testtest");
        stage.initOwner(primaryStage);
//        stage.initModality(Modality.WINDOW_MODAL);

        stage.show();
        PlanParserThread parser = new PlanParserThread(stage);
        parser.start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
