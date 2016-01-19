package summoner;

public interface BackgroundListener {
	public void SummonerChecked(Summoner sum);
	public void finishedSync();
	public void canceledSync();
}
