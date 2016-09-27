package glitchy.core.effects;

import java.util.Arrays;
import glitchy.core.imageProcessing.PixelStream;

/**
 * This effect can sort a PixelStream's pixels. Can both sort 
 * by pixel integer values, and each colorchannel independently.
 * @author Aksel and Mikkel
 */
public class SortEffect extends AbstractEffect{
	private static final long serialVersionUID = 3986405714868712920L;

	public SortEffect(int[] modifiers, int[] range) {
		super(modifiers, range);
	}

	/**
	 * Sorts the PixelStream, either by pixels, or each channel independently.
	 */
	@Override
	public void applyEffect(PixelStream pixelStream) {
		if(modifiers[0] == -1)
			sortPixels(pixelStream);

		else
			sortChannels(pixelStream);
	}

	@Override
	public String toString() {
		return "Sort";
	}
	
	/**
	 * Sorts each channel independently.
	 * @param pixelStream PixelStream pixelStream.
	 */
	private void sortChannels(PixelStream pixelStream){
		int[] pixels = pixelStream.getPixels();
		
		//For each color channel mask
		for(int mask : modifiers){
			int[] channelValues = new int[range[1]-range[0]];
			
			//Grab all the channel values
			for(int i = 0; i < channelValues.length; i++){
				int pixelIndex = i + range[0];
				channelValues[i] = (pixels[pixelIndex] & mask);
			}
			
			Arrays.sort(channelValues);
			
			//Insert the channel values
			for(int i = 0; i < channelValues.length; i++){
				int pixelIndex = i + range[0];
				pixels[pixelIndex] = (pixels[pixelIndex] & ~mask) | (channelValues[i]);
			}
		}
	}

	/**
	 * Sorts a PixelStream's pixels by their integer value.
	 * @param pixelStream PixelStream to sort.
	 */
	private void sortPixels(PixelStream pixelStream){
		//Get subarray, determined by selection range.
		int[] pixels = pixelStream.getPixels();
		int[] sorted = Arrays.copyOfRange(pixels, range[0], range[1]+1);

		Arrays.sort(sorted);
			
		//Insert the sorted subarray.
		for(int i = 0; i < sorted.length; i++){
			pixels[i + range[0]] = sorted[i];
		}
	}
}