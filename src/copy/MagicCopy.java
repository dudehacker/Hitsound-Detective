package copy;
import java.io.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import detective.TimedMistake;
import detective.MistakeType;
import osu.beatmap.Chord;
import osu.beatmap.event.Sample;
import osu.beatmap.hitobject.HitObject;
import osu.beatmap.timing.Timing;
import util.BeatmapUtils;

public class MagicCopy implements Runnable{
	
	//Variables
	private File hitsoundFile;
	private File targetFile;
	private boolean isKeysound;
	private boolean clear;
	private List<HitObject> sourceHO = new ArrayList<>();
	private List<HitObject> targetHO = new ArrayList<>();
	private List<Sample> sourceSB = new ArrayList<>();
	private List<Timing> sourceTimingTotal;
	private List<Timing> targetTiming;
	private String nl = BeatmapUtils.nl;
	
	// Constructor
	public MagicCopy(File input, File output, boolean keysound, boolean clear){
		hitsoundFile = input;
		targetFile = output;
		isKeysound = keysound;
		this.clear = clear;
	}

	@Override
	public void run() {
		try {
			if (clear){
				// Clear all hitsounds except SB
				BeatmapUtils.clearHitsounds(targetFile);
			}
			parseSource();
			parseTarget();
			exportBeatmap();
			JOptionPane.showMessageDialog(null, "Beatmap exported at " + targetFile.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void parseSource() throws Exception{
		sourceHO = BeatmapUtils.getListOfHitObjects(hitsoundFile, true);
		System.out.println("Hitsound total size = "+sourceHO.size());
		sourceTimingTotal = BeatmapUtils.getTimingPoints(hitsoundFile);
		sourceSB = BeatmapUtils.getSamples(hitsoundFile);
	}
	
	private void parseTarget() throws Exception{
		targetHO = BeatmapUtils.getListOfHitObjects(targetFile,false);
		targetTiming = BeatmapUtils.getTimingPoints(targetFile);
	}
	
	private String copyHS(){
		Collections.sort(sourceSB,Sample.StartTimeComparator);
		String output = "";
		
		Map<Long, Chord> sourceChords = BeatmapUtils.convertToChordMapWithHitsound(sourceHO, sourceSB);
		Map<Long, Chord> targetChords = BeatmapUtils.convertToChordMapWithHitsound(targetHO, null);
		
		List<Long> list_time = BeatmapUtils.getDistinctStartTime(sourceHO,targetHO);
		for (Long t : list_time){
			
			Chord sourceChord = sourceChords.get(t);
			Chord targetChord = targetChords.get(t);
			int sourceSize = sourceChord.getSize();
			int targetSize = targetChord.getSize();
			if (sourceSize == targetSize){
				// CASE 1
				//System.out.println("same size at " +t);
				for (int i = 0; i<targetSize;i++){
					HitObject source_ho = sourceChord.get(i);
					HitObject target_ho = targetChord.get(i);
					target_ho.copyHS(source_ho);
					outputHOs.add(target_ho);
				}
			} else if (sourceSize> targetSize){
				// CASE 2
				System.out.println("sourceSize> targetSize at " +t);
				if (targetSize ==0 ){
					if (isKeysound){
						// keysound = true then copy to SB, else do nothing
						for (int j = 0; j<sourceSize;j++){
							HitObject source_ho = sourceChord.get(j);
							if (source_ho.toSample().toString().contains(".wav")){
								sourceSB.addAll(source_ho.toSample());
							}
						}
						
					}

				}  else{
					int defaultHitSoundSize = BeatmapUtils.getDefaultHSChordSizeForTime(sourceChord, t);
					switch (defaultHitSoundSize){
					case 0:
					case 1:
						System.out.println("source size 0|1 at " +t);
						for (int i = 0; i<targetSize;i++){
							HitObject source_ho = sourceChord.get(i);
							HitObject target_ho = targetChord.get(i);
							target_ho.copyHS(source_ho);
							outputHOs.add(target_ho);
						}
						for (int j = targetSize; j < sourceSize; j++){
							HitObject source_ho = sourceChord.get(j);
							if (source_ho.toSample().toString().contains(".wav")){
								sourceSB.addAll(source_ho.toSample());
							}
						}
						break;
					
						
					case 2: // Combine both default hitsounds into 1 HitObject
						System.out.println("source size 2 at " +t);
						outputHOs.addAll(combineDefaultHS(sourceChord,targetChord,2));
						break;
						
					case 3:
						System.out.println("source size 3 at " +t);
						if (targetSize > 2){
							outputHOs.addAll(combineDefaultHS(sourceChord,targetChord,2));
						} else {
							outputHOs.addAll(combineDefaultHS(sourceChord,targetChord,3));
						}
						break;
					}
				}
			
			}else{
				// CASE 3 sourceSize < targetSize
				//System.out.println("sourceSize < targetSize at " +t);
				for (int i = 0; i<sourceSize;i++){
					HitObject source_ho = sourceChord.get(i);
					HitObject target_ho = targetChord.get(i);
					target_ho.copyHS(source_ho);
					outputHOs.add(target_ho);
				}
				for (int j = sourceSize; j<targetSize;j++){
					HitObject target_ho = targetChord.get(j);
					outputHOs.add(target_ho);
				}
			}
		}
		Collections.sort(outputHOs, HitObject.StartTimeComparator);
		for (HitObject ho : outputHOs) {
			output += ho.toString() + nl;
		}
		return output;
	}
	

	
	
	private List<HitObject> combineDefaultHS(List<HitObject> sourceChord, List<HitObject> targetChord, int n){
		
		List<HitObject> output = new ArrayList<>();
		Collections.sort(sourceChord, HitObject.AdditionComparator);
		int sourceSize = sourceChord.size();
		int targetSize = targetChord.size();
		HitObject source_ho1 = sourceChord.get(0);
		HitObject source_ho2 = sourceChord.get(1);
		HitObject newHO = source_ho1.clone();
		if (n==2){
			if (newHO.getAddition() == source_ho2.getAddition()) {
				newHO.addWhistleFinishClap(source_ho2.getWhistleFinishClap());
				
				HitObject target_ho1 = targetChord.get(0);
				target_ho1.copyHS(newHO);
				output.add( target_ho1);
				for (int x = 0; x < n ; x++){
					sourceChord.remove(0);
				}
				targetChord.remove(0);
				
			} 
			
		} else if (n==3){
			HitObject source_ho3 = sourceChord.get(2);
			if (source_ho3.getAddition() == source_ho2.getAddition() && source_ho2.getAddition() == source_ho1.getAddition()) {
				newHO.addWhistleFinishClap(source_ho2.getWhistleFinishClap(),source_ho3.getWhistleFinishClap());
				
				HitObject target_ho1 = targetChord.get(0);
				target_ho1.copyHS(newHO);
				output.add( target_ho1);
				for (int x = 0; x < n ; x++){
					sourceChord.remove(0);
				}
				targetChord.remove(0);
				
			} else if (source_ho1.getAddition()==source_ho2.getAddition()) {
				newHO.addWhistleFinishClap(source_ho2.getWhistleFinishClap());
				
				HitObject target_ho1 = targetChord.get(0);
				target_ho1.copyHS(newHO);
				output.add( target_ho1);
				for (int x = 0; x < 2 ; x++){
					sourceChord.remove(0);
				}
				targetChord.remove(0);
				
			} else if(source_ho2.getAddition() == source_ho3.getAddition()) {
				newHO = source_ho2.clone();
				newHO.addWhistleFinishClap(source_ho3.getWhistleFinishClap());
				
				HitObject target_ho1 = targetChord.get(0);
				target_ho1.copyHS(newHO);
				output.add( target_ho1);
				sourceChord.remove(1);
				sourceChord.remove(1);
				targetChord.remove(0);
			} 
			
		} else {
			throw new IllegalArgumentException();
		}

		// copy rest of hitsounds
		try {
		if (sourceChord.size()>0 && targetChord.size()>=0){
			for (int i = 0; i<targetChord.size();i++){
				HitObject source_ho = sourceChord.get(i);
				HitObject target_ho = targetChord.get(i);
				target_ho.copyHS(source_ho);
				output.add(target_ho);
			}
			for (int j = targetChord.size(); j < sourceChord.size(); j++){
				HitObject source_ho = sourceChord.get(j);
				sourceSB.addAll(source_ho.toSample());
			}
			
		}
		
		} catch (Exception e){
			System.out.println(n + " targetsize " + targetSize + " source size " + sourceSize);
			e.printStackTrace();
		}
		return output;
	}

	
	@SuppressWarnings("unchecked")
	private void exportBeatmap() throws Exception{
		String[] beatmap = BeatmapUtils.getAllInfo(targetFile);
		Collections.sort(sourceSB,Sample.StartTimeComparator);
		String generalInfo = beatmap[0];
		String hitObjectsInfo = "[HitObjects]" + nl;
		hitObjectsInfo += copyHS();

		String sampleInfo = BeatmapUtils.convertListToString(sourceSB);
		//String[] beatmapSource = OsuUtils.getAllInfo(hitsoundFile);
		
		// only copy useful timing for default hitsounds to target difficulty

		Timing t1, t2;
		for (Timing t : targetTiming){
			for (int i = 0; i < sourceTimingTotal.size(); i++){
				if (i<sourceTimingTotal.size()-1){
					t1 = sourceTimingTotal.get(i);
					t2 = sourceTimingTotal.get(i+1);
					if (t1.getOffset() <= t.getOffset() && t.getOffset() < t2.getOffset()){
						t1 = new Timing(t);
						break;
					}
				} else {
					// last timing
					sourceTimingTotal.get(i) = new Timing(t);
				}
			}
		}
		// add timings that exist in HS but not in target
		List<Timing> targetTimingCopy = (List<Timing>) targetTiming.clone();
		List<Long> offsets = new ArrayList<>();
		
		for (Timing t_target: targetTimingCopy){
			if (!offsets.contains(t_target.getOffset())){
				offsets.add(t_target.getOffset());
			}
		}
		for (Timing t_source : sourceTimingTotal){
			if (!offsets.contains(t_source.getOffset())){
				Timing t = getTimingFromOffset(t_source.getOffset());
				targetTiming.add(t);
			}
		}
		/*System.out.println("Size of target Timings = " +targetTiming.size());
		for (Timing t : targetTiming) {
			System.out.println(t.toString());
		}*/
		targetTiming.sort(Timing.StartTimeComparator);
		String timingInfo = "[TimingPoints]" + nl + BeatmapUtils.convertListToString(targetTiming);
		String outputText = 
				generalInfo + nl + 
				sampleInfo+ nl+ 
		        timingInfo + nl +
		        hitObjectsInfo;
		BeatmapUtils.exportBeatmap(targetFile, outputText);
	}
	
	private Timing getTimingFromOffset(long offset){
		
		for (Timing t:sourceTimingTotal){
			//System.out.println(t.toString());
			if (t.getOffset() == offset){
				return t;
			}
		}
		System.out.println("offset = "+offset);
		System.out.println("Error in getTimingFromOffset()");
		return null;
	}
}
