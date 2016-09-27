package glitchy.core.effects;

import glitchy.core.imageProcessing.PixelStream;

/**
 * This effect can perform circular bitshifts to an image's pixels.
 * @author Aksel and Mikkel
 */
public class BitShiftEffect extends AbstractEffect{
	private static final long serialVersionUID = 180121265823438187L;

	public BitShiftEffect(int[] modifiers, int[] range) {
		super(modifiers, range);
	}

	/**
	 * Performs a bit shift. 
	 * A shift of 8 bits will shift by a channel, i.e. ARGB to BARG. 
	 * The last remaining bits are put at the start, meaning it's a circular shift.
	 */
	public void applyEffect(PixelStream pixelStream){
		int[] pixels = pixelStream.getPixels();
		
		int alphaChannel;
		
		if(pixelStream.hasAlpha())
			alphaChannel = 0;
		else
			alphaChannel = 0xff000000;
		
		//How many of the bits to affect. 32 bits for all channels, 24 to ignore alpha.
		int affectedBits = modifiers[0];
		
		//Number of bits to shift by.
		int shiftNum = modifiers[1];
		
		//Applying this mask with the & operator gets the remainder. 
		int mask = (int) (Math.pow(2, shiftNum)-1);
		
		//The remaining bits from the previous shift.
		//Starts out as the last remainder.
		int pR = pixels[pixels.length-1] & mask;
		
		//The remaining bits from the current shift.
		int cR;
		
		for(int i = range[0]; i <= range[1]; i++){
			//Current pixel
			int p = pixels[i] ^ alphaChannel;
			
			//Get remainder.
			cR = p & mask;
			
			//Remove remainder, and insert previous remainder at start.
			p = alphaChannel | (p >>> shiftNum) | pR << affectedBits-shiftNum;
			
			//Store current remainder for use in next iteration.
			pR = cR;
			
			pixels[i] = p;
		}
	}

	@Override
	public String toString() {
		return "Bitshift";
	}
}