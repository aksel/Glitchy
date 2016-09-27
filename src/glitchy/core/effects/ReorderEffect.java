package glitchy.core.effects;

import glitchy.core.imageProcessing.PixelStream;

/**
 * This effect can change the order of a pixel's color channels.
 * @author Aksel and Mikkel
 *
 */
public class ReorderEffect extends AbstractEffect{
	private static final long serialVersionUID = 7196305127500273482L;

	public ReorderEffect(int[] modifiers, int[] range) {
		super(modifiers, range);
	}

	/**
	 * Reorders the channels into the order specified by reorderMasks.
	 * @param pixelStream PixelStream pixelStream
	 */
	public void applyEffect(PixelStream pixelStream){
		
		int[] pixels = pixelStream.getPixels();
		
		//Masks [A , R , G , B]
		int[] masks = { 0xff000000,
						0x00ff0000,
						0x0000ff00,
						0x000000ff
		};
		
		int[] reorderMasks = this.modifiers;
		
		int pixel;
		int reorderedPixel;
		
		for(int i = range[0]; i <= range[1]; i++){
			pixel = pixels[i];
			reorderedPixel = pixel;
			
			for(int mI = 0; mI < masks.length; mI++){
				//Mask has not changed position
				if(masks[mI] == modifiers[mI])
					continue;
				
				//Grab channel out of original pixel
				int channel = (pixel & reorderMasks[mI]);
				
				//How many bits to shift by for channel to end up in correct position.
				int shiftNum = getShift(reorderMasks[mI]) - getShift(masks[mI]);
				
				//Shift channel into position.
				//Left shift if shiftNum is negative
				if(shiftNum < 0)
					channel <<= Math.abs(shiftNum);
				
				//Right shift if shiftNum is positive
				else
					channel >>>= shiftNum;
				
				//Remove channel at mask, insert other channel at mask
				reorderedPixel = (reorderedPixel & ~masks[mI]) | channel;
				
			}
			
			pixels[i] = reorderedPixel;
		}
		
	}
	
	@Override
	public String toString() {
		return "Reorder";
	}
	
	/**
	 * Gets number of bits required for a bitshift for the specified channel mask.
	 * @param mask
	 * @return
	 */
	private int getShift(int mask){
		switch(mask){
		case 0xff000000:
			return 24;
		case 0x00ff0000:
			return 16;
		case 0x0000ff00:
			return 8;
		default:
			return 0;
		}
	}
}