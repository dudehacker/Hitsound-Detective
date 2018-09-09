# Hitsound-Detective
Check hitsound issues in osu!mania maps


## Instructions

1. Pick 1 difficulty as your hitsound difficulty. Pick the highest difficulty if you don't have a custom hitsound difficulty
2. The result will be displayed as hyperlink, clicking it will direct you to the note in osu editor. 

## Download
https://drive.google.com/open?id=1rWBwBx5jrHVRnvvvavME8BP5yuTK3xfU

## Usage
1. Check unused hitsound
2. Check muted hitsound or Storyboard sample
3. Check hitsound inconsistency
4. Check missing hitsound

Note: support WFC stacked on 1 note, custom hitsound and additions

## Change Log
###### Update: Sept 25, 2017
Check SB hitsound, and also added check for unused hitsound.
If there's no inconsistency with a difficulty, it will not be displayed in results window.

###### Update: October 1, 2017
Fixed Bug where it close it self when inconsistency is detected.

###### Update: August 6, 2018
Support addition (addition overrides sampleset)
Can detect missing hitsound (if you used "soft-hitfinish.wav" but you dont have it in your song folder)
can detect muted hitsound or SB sample (SB sample has 0 volume, or hitsound is "xxx.wav" with 0 volume)
it does not consider hitnormal as a hitsound, ignored in check for inconsistency.

###### Update: August 12, 2018
Fixed bug with checking SB sample when there is no note at that time
Can detect unused & duplicate timing points
Reference: https://osu.ppy.sh/beatmapsets/597113/discussion/-/generalAll#/476017

###### Update: August 25, 2018
Fixed bug with showing inconsistency when target has note but hitsound diff doesnt have note
Added snapping uncertainty, for example, a note at time = 1447 will be checked against 1446, 1447 and 1448 in source difficulty.

## Screenshot
![alt text](http://i.imgur.com/0j2en9y.jpg)


