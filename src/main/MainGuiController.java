package main;

import java.io.IOException;

import constant.Region;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import storage.dataStorage;
import summoner.BackgroundListener;
import summoner.Summoner;
import sync.SyncManager;

public class MainGuiController implements BackgroundListener{
	private String idleStatus = "";
	
	@FXML
	private TextField addSumTF;
	@FXML
	private ListView SumList;
	@FXML
	private Label StatusLB;
	@FXML
	private ProgressIndicator ProgInd;
	@FXML 
	private ChoiceBox regionCB;
	@FXML 
	private Button AddBTN;
	@FXML
	private Button SyncBTN;
	

	
	private boolean sumTFColorRed;
	
	public void init(){
		ObservableList<Region> regions = FXCollections.observableArrayList(Region.NA,Region.EUW,
				Region.EUNE,Region.BR,Region.KR,Region.TR,Region.RU,Region.LAN,Region.LAS,Region.OCE);
		regionCB.setItems(regions);
		regionCB.getSelectionModel().select(dataStorage.getStorageObj().getLastRegion());
		SumList.setItems(dataStorage.getStorageObj().getSummonerList());

	}
	
	// --- FXML Functions ---
	
	@FXML
	public void AddBtn(ActionEvent e){
		addSum();
	}
	
	@FXML
	public void onClickSyncBTN(ActionEvent e){
		StatusLB.setText("Started Syncing...");
		inProgress(true,true);
		SyncManager.getInstance().syncNow(this);
		
	}
	public void onSyncCancel(ActionEvent e){
		SyncManager.getInstance().cancelSync();
		StatusLB.setText("Cancelling Sync...");
	}
	@FXML
	public void onHelpButton(ActionEvent e){
		Alert helpAlert = new Alert(AlertType.INFORMATION);
		helpAlert.setTitle("League of Streamers - Help");
		helpAlert.setHeaderText("Thanks for using League of Streamers!");
		helpAlert.setContentText("This Program helps you with your streaming experience, especially when you do a "
		+ "'Level 1-30' or an 'unranked -> whatever' Stream. "+System.lineSeparator()
		+ "Just add the Summonernames you want to get the level or division from and hit the sync button. "
		+ "The informations are saved in '"+dataStorage.getMainDocumentDirectory()+"' in different txt files."+System.lineSeparator()
		+ "If you find any bugs or want additional information, please write me an Email (nexurs@gmx.at)"
		+ System.lineSeparator()+System.lineSeparator()+"Icons desinged by Freepik");
		helpAlert.showAndWait();
	}
	@FXML
	public void onKeyPressedInSumList(KeyEvent e){
		if(e.getCode()==KeyCode.DELETE||e.getCode()==KeyCode.BACK_SPACE){
			dataStorage.getStorageObj().getSummonerList().remove(SumList.getSelectionModel().getSelectedIndex());
		}
	}
	@FXML 
	public void onEnter(){
		addSum();
		
	}
	@FXML
	public void onSumTFChanged(KeyEvent e){
		SumTFWrongValue(false);
	}
	
	@FXML
	public void onTemplates(ActionEvent e){
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/Templates.fxml"));
		try {
			Pane rootPane = loader.load();
			Scene scene = new Scene(rootPane);
			scene.setFill(Color.TRANSPARENT);
			stage.setScene(scene);
			stage.setTitle("League of Streamers - Templates");
			stage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	// --- Help Methods ---
	private void addSum(){
		String sumName = addSumTF.getText();
		Region reg = (Region)regionCB.getSelectionModel().getSelectedItem();
		if(!dataStorage.getStorageObj().isSumAlreadyInList(sumName,reg)){
			inProgress(true,false);
			StatusLB.setText("Verifying the Summoner "+sumName+" at Server "+reg);
			Summoner newSum = new Summoner(sumName,reg);
			newSum.addFinishedListener(this);
			newSum.verifySummonerAsyncron();
		} else {
			StatusLB.setText("Summoner "+sumName+" is already verified");
		}
	}
	public Region getCurrRegion(){
		return (Region) regionCB.getSelectionModel().getSelectedItem();
	}
	public void SumTFWrongValue(boolean wrong){
		if(wrong && !sumTFColorRed) {
			addSumTF.setStyle("-fx-text-fill: red");
			sumTFColorRed = true;
		} else if(!wrong && sumTFColorRed) {
			addSumTF.setStyle("-fx-text-fill: black");
			sumTFColorRed=false;
		}
	}
	
	private void setSyncButtonToCancel(boolean bool){
		if(bool){
			SyncBTN.setText("Cancel");
			SyncBTN.setOnAction(e -> onSyncCancel(e));
		} else {
			SyncBTN.setText("Sync Now");
			SyncBTN.setOnAction(e -> onClickSyncBTN(e));
		}
		
	}
	
	private void inProgress(boolean progress, boolean sync){
		AddBTN.setDisable(progress);
		ProgInd.setVisible(progress);
		if(!sync) SyncBTN.setDisable(progress);
		if(sync) setSyncButtonToCancel(progress);
	}
	
	// --- Background Listener Methods ---
	
	@Override
	public void SummonerChecked(Summoner sum) {
		inProgress(false,false);
		if(sum.isVerified()) {
			dataStorage.getStorageObj().getSummonerList().add(sum);
			StatusLB.setText(sum.getName()+" succsessfully verified");
			addSumTF.setText("");
		} else {
			StatusLB.setText(sum.getName()+" doesn't seem to exist at Server "+sum.getRegion());
			SumTFWrongValue(true);
		}
	}

	@Override
	public void finishedSync() {
		StatusLB.setText("Sync finished");
		inProgress(false,true);
	}

	@Override
	public void canceledSync() {
		StatusLB.setText("Sync cancelled");
		inProgress(false,true);
	}
}
