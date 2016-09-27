package glitchy.core.effects;

import java.util.Arrays;

import glitchy.core.imageProcessing.PixelStream;

/**
 * Applies a reverb-inspired Smear effect to a PixelStream. 
 * Pixels slowly fade out to the righ.
 * @author Aksel and Mikkel
 */
public class SmearEffect extends AbstractEffect{
	private static final long serialVersionUID = 7496330009228186679L;

	public SmearEffect(int[] modifiers, int[] range){
		super(modifiers, range);
	}

	/**
	 * Applies a Smear effect to the PixelStream.
	 */
	@Override
	public void applyEffect(PixelStream pixelStream){
		int[][] kernel = createKernel(modifiers[0] , modifiers[1]);
		smear(pixelStream, kernel);
	}
	
	/**
	 * Creates a kernel of the specified length, with precalculated intensity values.
	 * @param length Effect's length.
	 * @param intensity Starting intensity.
	 * @return Kernel.
	 */
	private int[][] createKernel(int length , double intensity){
		
		int[][] kernel = new int[length][256];

		intensity /= 10;

		//How much the intensity decreases per kernel position
		double fallOff = intensity / length;
		
		//Fill kernel with slowly decreasing intensity values.
		for(int i = 1; i < kernel.length; i++){
			for(int j = 0; j < 256; j++){
				kernel[i][j] = (int) (j * intensity);
			}
			
			intensity -= fallOff;
		}
		
		return kernel;
	}

	@Override
	public String toString() {
		return "Smear";
	}
	
	/**
	 * Smears the given PixelStream's pixels.
	 * @param pixelStream PixelStream.
	 * @param kernel A kernel with precalculated intensities.
	 */
	private void smear(PixelStream pixelStream , int[][] kernel){
		int[] pixels = pixelStream.getPixels();
		int[] pixelsRange = Arrays.copyOfRange(pixels, range[0], range[1]+1);
		
		int[] intensities;
		
		//Summed channels.
		int aSum,rSum,gSum,bSum;
		
		for(int pI = 0; pI < pixelsRange.length; pI++){
			//If unmodified pixel is already white, the effect will have no effect
			if(pixelsRange[pI] == 0xffffffff)
				continue;
			
			//Reset sums
			aSum = rSum = gSum = bSum = 0;
			
			int pixel;
			for(int kI = 1; kI < kernel.length; kI++){
				
				//Relative Index = pixel index - kernel index.
				int rI = pI - kI;
				
				if(rI < 0)
					break;
				
				pixel = pixelsRange[rI];
				
				intensities = kernel[kI];
				
				//OR channels of current pixel (modified by intensity) with sum.
				aSum |= intensities[ (pixel >> 24) & 0xff];
				rSum |= intensities[ (pixel >> 16) & 0xff];
				gSum |= intensities[ (pixel >>  8) & 0xff];
				bSum |= intensities[ (pixel)       & 0xff];
			}
			
			//Insert modified pixel into original pixels
			pixels[pI+range[0]] |= (aSum<<24) | (rSum<<16) | (gSum<<8) | (bSum);
		}
	}
}