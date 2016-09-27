package glitchy.core.imageProcessing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * This class handles image rendering. It can render in four different ways: 
 * Sum, Bitwise OR, Difference and Average.
 * @author Aksel, Rasmus and Mikkel
 */
public class ImageRenderer{
	
	/**
	 * Pixel merger.
	 * @author Aksel
	 */
	interface PixelMerger{
		public int mergePixels(int p1, int p2, boolean alpha);
	}
	
	/**
	 * Properties, defining the image to be rendered, and how it is to be rendered.
	 */
	private RenderProperties properties;

	/**
	 * Rendered image.
	 */
	private BufferedImage img;
	
	/**
	 * Creates a BufferedImage from RenderProperties and a PixelStream.
	 * @param pixelStream A PixelStream with which the BufferedImage will be filled.
	 * @return BufferedImage The image.
	 */
	private BufferedImage pixelStreamToImage(PixelStream pixelStream){
		img.setRGB(0, 0, properties.w, properties.h, pixelStream.getPixels(), 0, properties.w);
		return img;
	}

	/**
	 * Renders a BufferedImage of type ARGB.
	 * @param pixelStreams the pixelstreams to be rendered
	 * @return BufferedImage.
	 */
	public BufferedImage renderImage(ArrayList<PixelStream> pixelStreams){
		if(img==null ||
				(properties.w != img.getWidth() || 
				properties.h != img.getHeight())
				){
			img = new BufferedImage(properties.w , properties.h , BufferedImage.TYPE_INT_ARGB);
		}
		return pixelStreamToImage(mergePixelStreams(pixelStreams));
	}

	/**
	 * Renders a BufferedImage of specified type.
	 * @param imageType Image type.
	 * @param pixelStreams The pixelStreams to be rendered
	 * @return BufferedImage of specified type.
	 */
	public BufferedImage renderImageType(ArrayList<PixelStream> pixelStreams, int imageType){
		img = new BufferedImage(properties.w , properties.h , imageType);
		return pixelStreamToImage(mergePixelStreams(pixelStreams));
	}

	/**
	 * Renders pixelstream.
	 * @param pixelStreams The pixelstreams to be rendered
	 * @return PixelStream the merged pixelstream
	 */
	private PixelStream mergePixelStreams(ArrayList<PixelStream> pixelStreams) {
		PixelMerger merger = constructPixelMerger();
		return render(pixelStreams,merger);
	}
	
	private PixelMerger constructPixelMerger(){
		switch(properties.renderType){
		
		case RenderProperties.SUM:
			return (p1,p2,alpha) -> {
				int result = p1+p2;
				if(!alpha)
					result|=0xff000000;
				
				return result;
			};

		case RenderProperties.OR:
			return (p1,p2,alpha) -> p1|p2;

		case RenderProperties.DIFFERENCE:
			return (p1,p2,alpha) -> {
				int result = p1^p2;
				if(!alpha)
					result|=0xff000000;
				
				return result;
			};

		case RenderProperties.AVERAGE:
			return (p1,p2,alpha) -> avg(p1,p2);

		default:
			throw new IllegalArgumentException("Illegal render type: " + properties.renderType);
		}
	}

	/**
	 * Merges all PixelStreams, with the bitwise XOR operator.
	 * @param pixelStreams the pixelstreams
	 * @param merger Pixel merger.
	 * @return PixelStream, created from merged PixelStreams
	 */
	private PixelStream render(ArrayList<PixelStream> pixelStreams, PixelMerger merger){

		//Output PixelStream
		PixelStream oP = new PixelStream(properties.w * properties.h);

		//PixelStream's starting position. Offsets pixel index.
		int pos;

		int maxPos = oP.getPixels().length;

		for(PixelStream pS : pixelStreams){
			if (!pS.isVisible())
				continue;

			pos = pS.getPos();
			
			for(int index = 0; pS.hasIndex(index) && (index+pos < maxPos); index++){
				if(index+pos<0)
					continue;
				
				//Pixel at index in PixelStream
				int p1 = pS.getPixel(index);

				//Pixel at index in output PixelStream
				int p2 = oP.getPixel(index+pos);

				int result = merger.mergePixels(p1, p2, pS.hasAlpha());

				//Merge pixels, and put into output PixelStream
				oP.setPixel(index+pos, result);
			}
		}

		return oP;
	}
	
	/**
	 * Calculates and returns the average for the two given pixels. 
	 * Each color channel is calculated separately.
	 * @param p1
	 * @param p2
	 * @return Pixel.
	 */
	private int avg(int p1, int p2) {
		int a1,r1,g1,b1;
		int a2,r2,g2,b2;

		a1 = (p1 >> 24) & 0xff;
		if(a1 == 0)
			return p2;

		a2 = (p2 >> 24) & 0xff;
		if(a2 == 0)
			return p1;

		r1 = (p1 >> 16) & 0xff;
		g1 = (p1 >>  8) & 0xff;
		b1 =  p1 	    & 0xff;

		r2 = (p2 >> 16) & 0xff;
		g2 = (p2 >>  8) & 0xff;
		b2 =  p2 	    & 0xff;

		a1 += a2;
		r1 += r2;
		g1 += g2;
		b1 += b2;

		int merged = ((a1/2) << 24) | ((r1/2) << 16) | ((g1/2) << 8) | (b1/2);

		return merged;
	}
	
	/**
	 * Set render properties.
	 * @param properties RenderProperties
	 */
	public void setRenderProperties(RenderProperties properties){
		this.properties = properties;
	}
}