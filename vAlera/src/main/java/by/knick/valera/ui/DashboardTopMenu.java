package by.knick.valera.ui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;

import by.knick.valera.project.Config;
import by.knick.valera.utils.JsonUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class DashboardTopMenu implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	@SneakyThrows
	private void onOpenMenuClicked(){
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);		
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save vAlera Project");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setSelectedExtensionFilter(new ExtensionFilter("vAlera project", "vpr"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
        	Config config = JsonUtils.read(FileUtils.readFileToString(file, "UTF-8"), Config.class);
        	DashboardController.getInstance().openNewProject(config);
        }
	}
	
	@FXML
	@SneakyThrows
	private void onSaveAsMenuClicked(){
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);		
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save vAlera Project");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setSelectedExtensionFilter(new ExtensionFilter("vAlera project", "vpr"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
        	Config config = DashboardController.getInstance().getProject().getConfig();
			FileUtils.write(file, JsonUtils.writePretty(config), "UTF-8");
        }
	}
}
