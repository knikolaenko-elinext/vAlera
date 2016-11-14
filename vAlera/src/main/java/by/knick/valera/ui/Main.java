package by.knick.valera.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class Main extends Application {
	@Override
	@SneakyThrows
	public void start(Stage primaryStage) {
		URL fxml = getClass().getResource("Dashboard.fxml");
		ResourceBundle resources = UiStrings.getInstance().getBundle();
		Parent root = (Parent) FXMLLoader.load(fxml, resources);
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setTitle("vAlera");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
