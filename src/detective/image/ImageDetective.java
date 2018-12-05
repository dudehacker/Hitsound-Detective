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
		File bgFile = null;
		try {
			bgFile = new File(osuFile.getParent()+"\\"+bg);
			if (!bgFile.exists()) {
				mistake = new TimedMistake(0,MistakeType.MissingImage);
				System.out.println("missing image");
				return;
			}
		
			BufferedImage bimg = ImageIO.read(bgFile);
			int width          = bimg.getWidth();
			int height         = bimg.getHeight();
			if (width > MAX_WIDTH || height > MAX_HEIGHT) {
				mistake= new TimedMistake(0,MistakeType.BadResolutionImage);
				System.out.println("bad resolution image");
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error opening bg image " + bgFile.getAbsolutePath(), JOptionPane.ERROR_MESSAGE);
			
		}
	}
	
	public TimedMistake getMistake() {
		return mistake;
	}

}
