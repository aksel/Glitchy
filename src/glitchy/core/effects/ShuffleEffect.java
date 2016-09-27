package glitchy.core.effects;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import glitchy.core.imageProcessing.PixelStream;

/**
 * Shuffles a PixelStream's pixels with the Durstenfeld shuffle. 
 * Can both shuffle pixels, and shuffle each channel independently.
 * @author Aksel and Mikkel
 *
 */
public class ShuffleEffect extends AbstractEffect{
	private static final long serialVersionUID = 7714560755578789462L;

	public ShuffleEffect(int[] modifiers, int[] range) {
		super(modifiers, range);
	}

	/**
	 * Shuffles, either by pixels, or each channel independently.
	 */
	@Override
	public void applyEffect(PixelStream pixelStream) {
	
		if(modifiers[0] == -1)
			shuffle(pixelStream.getPixels());

		else
			shuffleChannels(pixelStream);
	}
	
	@Override
	public String toString() {
		return "Shuffle";
	}
	
	/**
	 * Durstenfeld shuffle (from StackOverflow) 
	 * http://stackoverflow.com/a/1520212
	 * @param ar Array to shuffle
	 */
	private void shuffle(int[] ar){
		Random rnd = ThreadLocalRandom.current();
		
		for (int i = range[1]; i >= range[0]; i--)
		{
			//Random, offset by range start
			int index = rnd.nextInt(i+1 - range[0]) + range[0];
			// Simple swap
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}
	
	/**
	 * Shuffles each channel independently.
	 * @param pixelStream PixelStream to shuffle
	 * @param pixelStream PixelStream pixelStream
	 */
	private void shuffleChannels(PixelStream pixelStream){
		int[] pixels = pixelStream.getPixels();

		for(int mask : modifiers){
			int[] channels = new int[pixels.length];

			//Grab all the channels
			for(int i = 0; i < pixels.length; i++){
				channels[i] = (pixels[i] & mask);
			}

			shuffle(channels);

			//Inserts the channels again
			for(int i = 0; i < pixels.length; i++){
				pixels[i] = (pixels[i] & ~mask) | (channels[i]);
			}
		}
	}
}
