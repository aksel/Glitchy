package glitchy.core.imageProcessing;

import glitchy.core.effects.AbstractEffect;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class contains an array of pixels, and various methods for 
 * handling said pixels.
 * @author Aksel, Mikkel and Rasmus
 */
public class PixelStream implements Serializable{
	private static final long serialVersionUID = 912596946632060796L;

	private int[] originalPixels;
	
	/**
	 * Array of pixels.
	 */
	private int[] pixels;
	
	private ArrayList<AbstractEffect> effects = new ArrayList<AbstractEffect>();
	
	private ArrayList<Boolean[]> effectActions = new ArrayList<Boolean[]>();
	
	/**
	 * The source image's width.
	 */
	private int w;
	
	/**
	 * The source image's height.
	 */
	private int h;
	
	/**
	 * Whether this PixelStream has alpha
	 */
	private boolean hasAlpha = true;
	
	/**
	 * Starting position.
	 */
	private int pos = 0;
	
	/**
	 * Selection start index.
	 */
	private int selectionS;
	
	/**
	 * Selection end index.
	 */
	private int selectionE;
	
	/**
	 * The source image's filename.
	 */
	private String title;
	
	/**
	 * Whether this image is visible or hidden.
	 */
	private boolean visible = true;
	
	private boolean rendered = true;

	/**
	 * Constructs an empty PixelStream, with the desired number of pixels.
	 * @param numOfPixels Desired number of pixels.
	 */
	public PixelStream(int numOfPixels){
		pixels = new int[numOfPixels];
		originalPixels = pixels.clone();
		title = "render";
	}
	
	/**
	 * Constructs a PixelStream from a BufferedImage.
	 * @param img
	 */
	public PixelStream(String title , BufferedImage img){
		this.title = title;
		
		w = img.getWidth();
		h = img.getHeight();
		
		setPixels(img.getRGB(0, 0, w, h, (int[]) null, 0, w));
		originalPixels = pixels.clone();
		
		hasAlpha = img.getColorModel().hasAlpha();
		selectionS = 0;
		selectionE = pixels.length-1;
	}
	
	/**
	 * Constructs a PixelStream from an array of packed integers.
	 * @param title Source filename.
	 * @param pixels Packed integers.
	 * @param alpha 
	 */
	public PixelStream(String title, int[] pixels, boolean alpha) {
		this.title = title;
		this.hasAlpha = alpha;
		
		w = h = (int) Math.sqrt(pixels.length);
		
		setPixels(pixels);
		originalPixels = pixels.clone();
		
		selectionS = 0;
		selectionE = pixels.length-1;
	}
	
	/**
	 * Rolls back the pixelstream back to a previous version.
	 */
	public void undo() {
		Boolean[] undoList = effectActions.remove(effectActions.size() - 1);
		if (effects.size() > undoList.length)
			effects.remove(effects.size() - 1);
		
		int index = 0;
		for (Boolean enabled : undoList) {
			effects.get(index).setEnabled(enabled);
			index++;
		}
		
		rendered = false;
	}
	
	/**
	 * Clones the pixelstream, and adds it to its arraylist.
	 */
	public void action() {
		Boolean[] undoList = new Boolean[effects.size()];
		
		int index = 0;
		for (AbstractEffect e : effects) {
			undoList[index] = e.isEnabled();
			index++;
		}
		
		effectActions.add(undoList);
	}

	/**
	 * Checks if index is in bounds.
	 * @param index the index
	 * @return True or false, whether or not the index is in bounds.
	 */
	public boolean hasIndex(int index){
		return index >= 0 & index < pixels.length;
	}
	
	/**
	 * Returns pixel at specified index.
	 * @param index
	 * @return Pixel at index.
	 */
	public int getPixel(int index){
		return pixels[index];
	}
	
	/**
	 * Returns pixel at specified index, converted to a Color
	 * @param index
	 * @return Color of pixel at specified index.
	 */
	public Color getPixelColor(int index){
		return new Color(pixels[index],true);
	}
	
	/**
	 * Sets pixel at index to given pixel.
	 * @param index Index to set.
	 * @param pixel Pixel to set.
	 */
	public void setPixel(int index , int pixel){
		pixels[index] = pixel;
	}
	
	/**
	 * @return Array of pixels (packed integers)
	 */
	public int[] getPixels(){
		return pixels;
	}

	/**
	 * @param pixels An array of packed integers, that can be interpreted as colors.
	 */
	public void setPixels(int[] pixels){
		this.pixels = pixels;
	}

	/**
	 * Get the starting position.
	 * @return Starting position.
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * Set where the PixelStream starts.
	 * @param pos Starting position.
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	/**
	 * sets the title on this pixelstream
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the title of this pixelstream
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Set whether PixelStream is visible.
	 * @param bool
	 */
	public void setVisible(boolean bool) {
		visible = bool;
	}
	
	/**
	 * Whether pixelstream is visible.
	 * @return PixelStream visibility. 
	 */
	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * Returns original image's width.
	 * @return
	 */
	public int getWidth(){
		return w;
	}
	
	/**
	 * Returns original image's height.
	 * @return
	 */
	public int getHeight(){
		return h;
	}
	
	/**
	 * Sets image width.
	 * @param w Width
	 */
	public void setWidth(int w){
		this.w = w;
	}
	
	/**
	 * Sets image height.
	 * @param h Height
	 */
	public void setHeight(int h){
		this.h = h;
	}
	
	/**
	 * Returns starting index of selection.
	 * @return Selection start index.
	 */
	public int getSelectionS(){
		return selectionS;
	}
	
	/**
	 * Returns end index selection.
	 * @return Selection end index.
	 */
	public int getSelectionE(){
		return selectionE;
	}

	/**
	 * Sets selection start index.
	 * @param selectionS Index.
	 */
	public void setSelectionS(int selectionS){
		if (selectionS < 0)
			selectionS = 0;
		
		this.selectionS = selectionS;
	}
	
	/**
	 * Sets selection end index.
	 * @param selectionE Index.
	 */
	public void setSelectionE(int selectionE){
		if (selectionE > pixels.length || selectionE < 0)
			selectionE = pixels.length - 1;
		
		this.selectionE = selectionE;
	}
	
	/**
	 * Returns selection range. If no selection, returns [0,length]
	 * @return Range[selection start, selection end]
	 */
	public int[] getSelectionRange(){
		int[] range = new int[2];
		//Either 0, or the start of the selection.
		range[0] = Math.max(0, getSelectionS());
		//Either pixels.length, or the end of the selection
		range[1] = getSelectionE();
		
		return range;
	}
	
	public void addEffect(AbstractEffect e){
		effects.add(e);
	}
	
	public void removeEffect(AbstractEffect e) {
		effects.remove(e);
	}
	
	/**
	 * Returns the pixelstream's effects
	 * @return
	 */
	public ArrayList<AbstractEffect> getEffects() {
		return effects;
	}
	
	/**
	 * Whether or not original image has alpha
	 * @return Has alpha
	 */
	public boolean hasAlpha() {
		return hasAlpha;
	}
	
	/**
	 * Sets whether or not original image has alpha
	 * @param b Has alpha
	 */
	public void setHasAlpha(boolean b){
		this.hasAlpha = b;
	}
	
	/**
	 * Copies the selection.
	 * @return Copy
	 */
	public int[] copyRange(){
		return Arrays.copyOfRange(pixels, selectionS, selectionE);
	}
	
	/**
	 * Cuts the selection.
	 * @return Copy
	 */
	public int[] cutRange(){
		int[] copyRange = Arrays.copyOfRange(pixels, selectionS, selectionE);
		
		Arrays.fill(pixels, selectionS, selectionE, 0);
		
		return copyRange;
	}

	public ArrayList<String> getAppliedEffects() {
		ArrayList<String> effectStrings = new ArrayList<String>();
		
		for(AbstractEffect effect : effects)
			effectStrings.add(effect.toString());
		
		return effectStrings;
	}
	
	public void resetPixels(){
		pixels = originalPixels.clone();
		setRendered(false);
	}

	public boolean isRendered() {
		return rendered;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}
}