package osu;
import java.util.Comparator;

public class Sample {
	private long startTime;
	private String hitSound;
	private int volume;

	public Sample(long t, String hs, int v){
		startTime = t;
		hitSound = hs;
		volume = v;
	}
	
	public String gethitSound(){
		return hitSound;
	}
	
	private String addQuotesToHS(){
		return  "\"" + hitSound + "\"";
	}
	
	@Override
	public String toString(){
		return "Sample,"+startTime+",0,"+addQuotesToHS()+","+volume;
	}
	
	public boolean equals(Sample s){
		if (startTime == s.startTime && hitSound.equals(s.hitSound) && volume == s.volume){
			return true;
		}
		return false;
		
	}
	
	@Override
	public Sample clone(){
		Sample s = new Sample(startTime,hitSound,volume);
		return s;
	}
	
	public static Comparator<Sample> StartTimeComparator = new Comparator<Sample>() {
		@Override
		public int compare(Sample n1, Sample n2) {
			long t1 = n1.startTime;
			long t2 = n2.startTime;
			/* For ascending order */
			return (int) (t1 - t2);
		}
	};

	public long getStartTime() {
		return startTime;
	}

	public boolean isSameSound(Sample s) {
		return hitSound.equals(s.hitSound);
	}

	public boolean isMuted() {
		return volume == 0;
	}
}
