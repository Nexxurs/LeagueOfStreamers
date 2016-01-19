package main;

public class constant {
	private static String DevKey = "8c3g=g875=67708;;@5ka160fh=jA>4f37;6";
	private static String ProgName = "League of Streamers";
	public static String getDevKey(){
		char[] charArray=DevKey.toCharArray();
		for(int i =0;i<charArray.length;i++){
			charArray[i]=(char) (charArray[i]-(i%10));
		}
		return new String(charArray);
	}
	public static String getProgName(){
		return ProgName;
	}
}
