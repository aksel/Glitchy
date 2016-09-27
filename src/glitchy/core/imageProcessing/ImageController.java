package glitchy.core.imageProcessing;

import glitchy.core.effects.Effect;
import glitchy.core.effects.EffectController;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Controls PixelStreams. 
 * Also acts as a link between Core and Effects packages.
 * @author Aksel
 */
public class ImageController implements Serializable{
	private static final long serialVersionUID = 6880768227268019348L;
	
	/**
	 * ArrayList of pixelstreams.
	 */
	private ArrayList<PixelStream> pixelStreams;
	
	/**
	 * Reference to EffectController.
	 */
	private EffectController effectController;
	
	/**
	 * Reference to ImageRenderer.
	 */
	private ImageRenderer renderer;
	
	/**
	 * Creates a new ImageController.
	 */
	public ImageController(){
		pixelStreams = new ArrayList<PixelStream>();
		effectController = new EffectController();
		renderer = new ImageRenderer();
	}

	/**
	 * Creates a PixelStream from a given BufferedImage, and returns it.
	 * @param title The name of the BufferedImage.
	 * @param img Image.
	 * @return PixelStream, created from the BufferedImage.
	 */
	public PixelStream addImage(String title, BufferedImage img){
		
		PixelStream pixelStream = new PixelStream(title, img);
		pixelStreams.add(pixelStream);
		
		return pixelStream;
	}
	
	/**
	 * Creates a PixelStream from converted raw data,
	 * and returns it.
	 * @param title The name of the file from which the raw data was loaded.
	 * @param convertedRaw int array, loaded from any file.
	 * @param alpha If converted image is to have alpha.
	 * @return new PixelStream.
	 */
	public PixelStream addRaw(String title , int[] convertedRaw, boolean alpha) {		
		PixelStream pixelStream = new PixelStream(title, convertedRaw, alpha);
		pixelStreams.add(pixelStream);
		return pixelStream;
	}
	
	/**
	 * Removes a pixelstream from the list
	 * @param stream The PixelStream
	 */
	public void removePixelStream(PixelStream stream){
		pixelStreams.remove(stream);
	}
	
	/**
	 * Requests that an effect be applied to a desired pixel stream.
	 * @param pixelStream The Pixelstream
	 * @param effect Desired effect.
	 * @param modifiers Effect modifiers.
	 */
	public void applyEffect(PixelStream pixelStream, Effect effect, int[] modifiers){
		effectController.applyEffect(pixelStream, effect, modifiers);
	}
	
	/**
	 * Checks if any pixelstream has changed, and needs to be rerendered. 
	 * If need be, a pixelstream is reset, and its effects are reapplied.
	 */
	public void checkPixelStreams(){
		for(PixelStream pixelStream : pixelStreams){
			if(!pixelStream.isRendered()){
				pixelStream.resetPixels();
				effectController.applyEffects(pixelStream);
				pixelStream.setRendered(true);
			}
		}
	}
	
	/**
	 * Tells ImageRenderer to render an image, based on given properties.
	 * @param properties Properties.
	 * @return BufferedImage the rendered image
	 */
	public BufferedImage render(RenderProperties properties){
		renderer.setRenderProperties(properties);
		return renderer.renderImage(pixelStreams);
	}
	
	/**
	 * Tells ImageRenderer to render an image of the specified type, based on given properties.
	 * @param properties Properties.
	 * @param type Desired image type.
	 * @return BufferedImage the rendered image
	 */
	public BufferedImage render(RenderProperties properties, String type){
		renderer.setRenderProperties(properties);
		return renderer.renderImageType(pixelStreams,determineImageType(type));
		
	}
	
	/**
	 * Sets pixelstream arraylist to given list. Usually used when 
	 * loading a project.
	 * @param streams ArrayList of PixelStreams to set.
	 */
	public void setPixelStreams(ArrayList<PixelStream> streams) {
		pixelStreams = streams;
	}
	
	/**
	 * Returns ArrayList of pixelstreams.
	 * @return ArrayList of pixelstreams.
	 */
	public ArrayList<PixelStream> getPixelStreams() {
		return pixelStreams;
	}

	/**
	 * Clears and resets the ImageController, including the loaded 
	 * pixelstreams.
	 */
	public void clear(){
		pixelStreams.clear();
		pixelStreams = null;
		renderer = null;
		effectController = null;
	}
	
	/**
	 * Returns the BufferedImage final int that represents the specified extension.
	 * @param extension Extension.
	 * @return int the type 
	 */
	private int determineImageType(String extension){
		
		switch(extension){
		
		case "jpg" : case "bmp" : 
			return BufferedImage.TYPE_3BYTE_BGR;
			
		case "png" :
			return BufferedImage.TYPE_4BYTE_ABGR;
			
		case "gif" :
			return BufferedImage.TYPE_BYTE_INDEXED;
			
		default :
			return BufferedImage.TYPE_INT_ARGB;
		}
		
	}
	
	/**
	 * Creates and adds a PixelStream from an int[].
	 * @param pixelStream PixelStream from which the int[] was copied.
	 * @param copyRange The copied range.
	 * @return The PixelStream.
	 */
	private PixelStream createPixelStreamFromCopy(PixelStream pixelStream, int[] copyRange){
		String title = "Layer " + (pixelStreams.size()+1);
		PixelStream copy = new PixelStream(title,copyRange,pixelStream.hasAlpha());
		
		copy.setPos(pixelStream.getSelectionS()+pixelStream.getPos());
		
		int width = pixelStream.getWidth();
		int height = (copy.getPixels().length)/width;
		
		copy.setWidth(width);
		copy.setHeight(height);
		
		pixelStreams.add(copy);
		
		return copy;
	}
	
	/**
	 * Copies the selected range from the PixelStream, 
	 * and returns a new PixelStream created from the copy.
	 * @param pixelStream The PixelStream.
	 * @return A PixelStream created from the copy.
	 */
	public PixelStream copySelectionToNewLayer(PixelStream pixelStream){
		return createPixelStreamFromCopy(pixelStream,pixelStream.copyRange());
	}
	
	/**
	 * Cuts the selected range from the PixelStream, 
	 * and returns a new PixelStream created from the copy.
	 * @param pixelStream The PixelStream.
	 * @return A PixelStream created from the copy.
	 */
	public PixelStream cutSelectionToNewLayer(PixelStream pixelStream){
		return createPixelStreamFromCopy(pixelStream,pixelStream.cutRange());
	}
}