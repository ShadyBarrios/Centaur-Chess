package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage stage) throws Exception{
		Parent parent = FXMLLoader.load(getClass().getResource("Board.fxml"));
		Scene scene = new Scene(parent);
		stage.setTitle("Centaur Chess");
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
