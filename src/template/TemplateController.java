package template;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import storage.dataStorage;

public class TemplateController {
	@FXML
	private Label nameLabel;
	@FXML
	private Label infoLabel;
	@FXML
	private TextArea tempText;
	@FXML
	private ListView tempList;
	@FXML
	private TextField newTemplateTF;
	
	@FXML
	public void initialize(){
		infoLabel.setWrapText(true);
		infoLabel.setText("Write here the text you want in your txt files. The possible phrases, which will be replaced are: '@name', '@lp', '@division' and '@level'." +
				" Please press the save button after you are finished editing.");
		tempList.setItems(dataStorage.getStorageObj().getTempList());
		tempList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            TemplateObject tempObject = (TemplateObject) newValue;
            nameLabel.setText(tempObject.getName());
            tempText.setText(tempObject.getText());
        });
		tempList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tempList.getSelectionModel().select(0);
	}
	
	@FXML
	public void onAdd(ActionEvent e){
		if(newTemplateTF.getText().replaceAll(" ","").length()!=0){
			dataStorage.getStorageObj().getTempList().add(new TemplateObject(newTemplateTF.getText()));
		}
	}

	@FXML
	public void onSave(ActionEvent e){
		((TemplateObject)tempList.getSelectionModel().getSelectedItem()).setText(tempText.getText());
	}
}
