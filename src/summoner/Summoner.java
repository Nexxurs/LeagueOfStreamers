package summoner;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import API.RiotAPI;
import Exception.RiotApiException;
import constant.QueueType;
import constant.Region;
import main.constant;
import storage.dataStorage;

public class Summoner implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Region region;
	private String name;
	private String lowercaseName;
	private long summonerId;
	private int level;
	private String division;
	private int verif;
	private int lp;
	
	private transient BackgroundListener listener;
	
	public final int NOT_VERIFIED = -1;
	public final int VERIFIED = 1;
	public final int PENDING = 0;
	
	public Summoner(String name, Region region) {
		verif =  PENDING;
		this.name = name;
		this.region = region; 
		this.lowercaseName = name.toLowerCase().replaceAll(" ", "");
	}
	
	public void addFinishedListener(BackgroundListener obj){
		listener = obj;
	}
	
	
	public void verifySummonerAsyncron(){
		Thread addingThread = new newSummonerThread(this);
		addingThread.start();
	}
	
	private class newSummonerThread extends Thread{
		private Summoner sum;
		public newSummonerThread(Summoner sum) {
			this.sum = sum;
		}
		@Override
		public void run() {
			super.run();
			verify();
			if(listener != null){
				
				javafx.application.Platform.runLater(new Runnable(){

					@Override
					public void run() {
						listener.SummonerChecked(sum);
					}
				});
			
			}
			
		}
		
	}
	
	public void verify(){
		RiotAPI api = new RiotAPI(constant.getDevKey());
		Map<String, DTO.Summoner.Summoner> sumMap = null;
		try {
			sumMap = api.getSummonerByName(this.region, lowercaseName);
		} catch (RiotApiException e) {
			verif = NOT_VERIFIED;			
		}
		if(sumMap!=null){
			DTO.Summoner.Summoner sum = sumMap.get(lowercaseName);
			this.summonerId = sum.getId();
			this.level = (int)sum.getSummonerLevel();
			
			if(level==30){
				Map<String, List<DTO.League.League>> LeagueMap;
				try {
					LeagueMap = api.getLeagueEntry(region, summonerId);
					List<DTO.League.League> LeagueList = LeagueMap.get(summonerId+"");
					if(LeagueList == null) System.out.println("ERROR AT VERIFY");
					for(DTO.League.League league : LeagueList){
						
						if(league.getQueue().equals(QueueType.RANKED_SOLO_5x5.name())){
							this.division = league.getTier()+" "+league.getEntries().get(0).getDivision();
							this.lp =league.getEntries().get(0).getLeaguePoints();
						}
					}
				} catch (RiotApiException e) {
					e.printStackTrace();
				}
			}
			verif = VERIFIED;
		}
	}

	public Region getRegion() {
		return region;
	}
	public String getName() {
		return name;
	}
	public long getSummonerId() {
		return summonerId;
	}
	public int getLevel() {
		return level;
	}
	public String getDivision(){
		return division;
	}
	public int getLP(){
		return lp;
	}
	public boolean isLvl30(){
		return level==30;
	}
	public boolean isVerified() {
		return verif==VERIFIED;
	}

	@Override
	public String toString() {
		if(isLvl30()){
			return name+" ("+region+") - "+(division==null ? "unranked" : division);
		} else {
			return name+" ("+region+") - Lvl "+level;
		}
	}
	private void writeObject(java.io.ObjectOutputStream out)
		     throws IOException {
		out.defaultWriteObject();
	}
	private void readObject(java.io.ObjectInputStream in)
		     throws IOException, ClassNotFoundException{
		in.defaultReadObject();
	}
}
