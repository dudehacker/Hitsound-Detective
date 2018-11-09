package detective.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import detective.MistakeType;
import detective.TimedMistake;
import osu.beatmap.Beatmap;

public class ImageDetective implements Runnable{
	
	public final static int MAX_WIDTH = 1920;
	public final static int MAX_HEIGHT = 1200;
	
	private File osuFile;
	private TimedMistake mistake;
	
	public ImageDetective(File osuFile) {
		this.osuFile = osuFile;
	}
	

	@Override
	public void run() {
		String bg = new Beatmap(osuFile).getEventSection().getBgImage();
		try {
			File bgFile = new File(osuFile.getParent()+"\\"+bg);
			if (!bgFile.exists()) {
				mistake = new TimedMistake(0,MistakeType.MissingImage);
				return;
			}
		
			BufferedImage bimg = ImageIO.read(bgFile);
			int width          = bimg.getWidth();
			int height         = bimg.getHeight();
			if (width > MAX_WIDTH || height > MAX_HEIGHT) {
				mistake= new TimedMistake(0,MistakeType.BadResolutionImage);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error opening bg image", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}
	
	public TimedMistake getMistake() {
		return mistake;
	}

}
