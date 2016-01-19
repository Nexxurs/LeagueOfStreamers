package storage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import constant.Region;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.constant;
import summoner.Summoner;

public class storageObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient ObservableList<Summoner> summonerList;
	private String storageDirectory;
	private boolean syncThreadStartsWithProgram;
	private Region lastRegion;
	
	public storageObject() {
		summonerList = FXCollections.observableArrayList();
		String DefaultDirectory=  new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
		storageDirectory = DefaultDirectory+File.separator+constant.getProgName();
		syncThreadStartsWithProgram = true;
		lastRegion = Region.NA;
	}
	
	public boolean storageDirectoryExists(){
		return Files.exists(Paths.get(URI.create(storageDirectory)));
	}
	
	public boolean isSumAlreadyInList(String sumName, Region region){
		for(Summoner sum : summonerList){
			if(sum.getName().toLowerCase().equals(sumName.toLowerCase()) && sum.getRegion().equals(region)) return true;
		}
		return false;
	}
	
	// --- Getters and Setters ---
	public ObservableList<Summoner> getSummonerList() {
		return summonerList;
	}
	public String getStorageFileDirectory() {
		return "file://"+storageDirectory;
	}
	public boolean isSyncThreadStartingWithProgram() {
		return syncThreadStartsWithProgram;
	}
	public void setSummonerList(ObservableList<Summoner> summonerList) {
		this.summonerList = summonerList;
	}
	public void setStorageDirectory(String storageDirectory) {
		this.storageDirectory = storageDirectory;
	}
	public void setSyncThreadStartsWithProgram(boolean syncThreadStartsWithProgram) {
		this.syncThreadStartsWithProgram = syncThreadStartsWithProgram;
	}
	public Region getLastRegion() {
		return lastRegion;
	}
	public void setLastRegion(Region lastRegion) {
		this.lastRegion = lastRegion;
	}
	
	//--- Serilization ---
	private void writeObject(java.io.ObjectOutputStream out)
		     throws IOException {
		out.defaultWriteObject();
		ArrayList<Summoner> sumArrayList = new ArrayList<Summoner>(summonerList);
		out.writeObject(sumArrayList);
	}
	private void readObject(java.io.ObjectInputStream in)
		     throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		
		List<Summoner> sumArrayList = (List<Summoner>) in.readObject();
		this.summonerList = FXCollections.observableArrayList(sumArrayList);
	}
}
