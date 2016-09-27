package glitchy.core.effects;

import glitchy.core.imageProcessing.PixelStream;

/**
 * This effect can invert any of a pixel's channels.
 * @author Aksel and Mikkel
 */
public class InvertEffect extends AbstractEffect{
	private static final long serialVersionUID = 823124839380095016L;

	public InvertEffect(int[] modifiers, int[] range) {
		super(modifiers, range);
	}

	/**
	 * Inverts the specified color channels.
	 * @param pixelStream PixelStream to be inverted.
	 */
	public void applyEffect(PixelStream pixelStream){
		
		int[] pixels = pixelStream.getPixels();
		
		int mask = 0;
		
		for(int m : modifiers)
			mask |= m;
		
		for(int i = range[0]; i <= range[1]; i++){
			int p = pixels[i];
			
			p ^= mask;
			
			pixels[i] = p;
		}
	}

	@Override
	public String toString() {
		return "Invert";
	}
}