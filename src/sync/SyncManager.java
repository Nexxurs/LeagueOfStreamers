package sync;

import summoner.BackgroundListener;

public class SyncManager {
	private static SyncManager instance;
	private Thread syncTh;
	private SyncManager(){
		
	}
	public static SyncManager getInstance(){
		if(instance == null){
			instance = new SyncManager();
		}
		return instance;
	}
	public void syncNow(BackgroundListener listener){
		SyncThread sync = new SyncThread(listener);
		syncTh = new Thread(sync);
		syncTh.start();
	}
	public boolean isSyncing(){
		if(syncTh == null) return false;
		return syncTh.isAlive();
	}
	public void cancelSync(){
		syncTh.interrupt();
	}
}
