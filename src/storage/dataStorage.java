package storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import main.constant;

import javax.swing.JFileChooser;


public class dataStorage {	
	private final static storageObject stObj = getStorageObjFromFile();
	private final static String settingFILENAME="StreamersElo.settings";
	
	private static String getSettingsFilePath() {
		return getMainDocumentDirectory()+settingFILENAME;
	}
	public static String getMainDocumentDirectory(){
		String DefaultDirectory=  new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
		//System.out.println(DefaultDirectory);
		return DefaultDirectory+File.separator+constant.getProgName()+File.separator;
	}
	
	public static storageObject getStorageObj() {
		return stObj;
		
	}
	
	// --- Saving and Retrieving of the Settings
	private static storageObject getStorageObjFromFile(){
		String propertiesFilePath = getSettingsFilePath();
		File propertyFile = new File(propertiesFilePath);
		if(propertyFile.exists()){
			ObjectInput iObj = null;
			try {
				iObj = new ObjectInputStream(new BufferedInputStream(new FileInputStream(propertyFile)));
				return (storageObject) iObj.readObject();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				if(iObj != null){
					try {
						iObj.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}	
			}
		} else {
			File settingsDirectory = new File(getMainDocumentDirectory());
			if(!settingsDirectory.isDirectory()){
				settingsDirectory.mkdir();
			}
		}
		return new storageObject();
	}
	public static boolean safeStorageObjToFile(){
		if(stObj == null) return false;
		File settingsDirectory = new File(getMainDocumentDirectory());
		if(!settingsDirectory.isDirectory()){
			settingsDirectory.mkdir();
		}
		
		
		File propertyFile = new File(settingsDirectory,settingFILENAME);
		try {
			propertyFile.createNewFile();
		} catch (IOException e1) {
			System.out.println("ERROR CREATING FILE - dataStorage.safeStorageObjToFile");
		}
		ObjectOutput oObj = null;
		try{
			oObj = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(propertyFile)));
			oObj.writeObject(stObj);
			return true;
		} catch (IOException e){
			e.printStackTrace();
		} finally {
			if(oObj != null){
				try {
					oObj.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
}
