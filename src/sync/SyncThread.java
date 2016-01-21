package sync;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import API.RiotAPI;
import javafx.collections.ObservableList;
import main.constant;
import storage.dataStorage;
import summoner.BackgroundListener;
import summoner.Summoner;
import template.TemplateObject;

public class SyncThread implements Runnable {
	RiotAPI api;
	BackgroundListener listener;
	public SyncThread(BackgroundListener listener){
		api = new RiotAPI(constant.getDevKey());
		this.listener =listener;
	}
	
	@Override
	public void run() {
		ObservableList<Summoner> sumList = dataStorage.getStorageObj().getSummonerList();
		for(Summoner sum : sumList){
			if(Thread.currentThread().isInterrupted()){
				finishedSync(false);
				return;
			}
			sum.verify();
			String mainDirectory = dataStorage.getMainDocumentDirectory();

			for(TemplateObject tempObj : dataStorage.getStorageObj().getTempList()){
				try (PrintStream out = new PrintStream(new FileOutputStream(mainDirectory+File.separator+sum.getName()+"-"+tempObj.getName()+".txt"))) {
					out.print(tempObj.getOutputText(sum));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		}
		if(Thread.currentThread().isInterrupted()){
			finishedSync(false);
			return;
		}
		finishedSync(true);
		
	}
	private void finishedSync(boolean success){
		if(listener != null){
			if(success) javafx.application.Platform.runLater(() -> listener.finishedSync());
			else javafx.application.Platform.runLater(() -> listener.canceledSync());
		}
	}
	
}
