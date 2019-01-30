package detective.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import detective.mistake.Mistake;
import detective.mistake.MistakeType;

public class ImageDetective{
	
	public final static int MAX_WIDTH = 1920;
	public final static int MAX_HEIGHT = 1200;
	
	public static Mistake check(File bgFile) {
		
		try {
			if (!bgFile.exists()) {
				System.out.println("missing image");
				return new Mistake(MistakeType.MissingImage);
				
			}	
			BufferedImage bimg = ImageIO.read(bgFile);
			int width          = bimg.getWidth();
			int height         = bimg.getHeight();
			if (width > MAX_WIDTH || height > MAX_HEIGHT) {
				System.out.println("bad resolution image");
				return new Mistake(MistakeType.BadResolutionImage);
			}
		}
		
		catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error opening bg image " + bgFile.getAbsolutePath(), JOptionPane.ERROR_MESSAGE);
			
		}
		return null;
	}

}
