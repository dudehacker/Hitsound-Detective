package utils;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

public class StoryBoardUtils {
	public final static String format = "%.4f";
	
	public static String characterToUnicode(char ch) {
		return Integer.toHexString(ch | 0x10000).substring(1);
	}

	public static int characterToUnicodeInt(char ch) {
		String hex = characterToUnicode(ch);
		return Integer.decode("0x" + hex);
	}

	public static int storyboardXToHitObjectX(int x) {
		return x - 63;
	}

	public static int hitObjectXToStoryboardX(int x) {
		return x + 63;
	}

	public static int storyboardYToHitObjectY(int y) {
		return y - 71;
	}

	public static int hitObjectYToStoryboardY(int y) {
		return y + 71;
	}

	public static double degreesToRadian(double degrees) {
		return (degrees / 180.0) * Math.PI;
	}

	public static boolean isCharacterJapanese(char ch) {
		if (characterToUnicodeInt(ch) <= 9835) {
			return false;
		}

		return true;
	}

	public static String formatDoubleToString(double n) {
		String s = String.format(format, n);
		while (s.charAt(s.length() - 1) == '0') {
			s = s.substring(0, s.length() - 1);
		}
		if (s.charAt(s.length() - 1) == '.') {
			s = s.substring(0, s.length() - 1);
		}
		return s;
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return the 2 roots, 0 = small root, 1 = big root
	 */
	public static double[] quadraticFormula(double a, double b, double c) {
		double d = b * b - 4 * a * c;
		if (d < 0) {
			System.out.println("Discriminant < 0, no real solutions");
			System.out.println("b^2 = " + Math.pow(b, 2));
			System.out.println("4ac = " + 4 * a * c);
		}
		double[] output = new double[2];
		output[1] = (-b + Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a);
		output[0] = (-b - Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a);
		return output;
	}
	
	public static String getFileSuffix(final String path) {
		String result = null;
		if (path != null) {
			result = "";
			if (path.lastIndexOf('.') != -1) {
				result = path.substring(path.lastIndexOf('.'));
				if (result.startsWith(".")) {
					result = result.substring(1);
				}
			}
		}
		return result;
	}
	
	public static Dimension getImageDim(final String path) {
		Dimension result = null;
		String suffix = getFileSuffix(path);
		Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
		if (iter.hasNext()) {
			ImageReader reader = iter.next();
			try {
				ImageInputStream stream = new FileImageInputStream(new File(path));
				reader.setInput(stream);
				int width = reader.getWidth(reader.getMinIndex());
				int height = reader.getHeight(reader.getMinIndex());
				result = new Dimension(width, height);
			} catch (IOException e) {
				System.out.println(path);
			} finally {
				reader.dispose();
			}
		} else {

		}
		return result;
	}
	
	public static double getImageToSBSize(String fullBGPath) {
		double safetyRange = 1.1;
		double verticalScale = 480.0 / getImageDim(fullBGPath).getHeight() * safetyRange;
		return verticalScale;
	}
}
