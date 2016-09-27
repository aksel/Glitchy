package glitchy.gui;

import glitchy.core.imageProcessing.PixelStream;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * This class is the workspace. 
 * All panels like the canvas and property panels reside in here.
 * @author Rasmus and Aksel
 */
@SuppressWarnings("serial")
public class Workspace extends JPanel{

	/**
	 * Canvas
	 */
	private Canvas canvas;
	
	/**
	 * Properties panel
	 */
	private Properties properties;
	
	/**
	 * Layers panel
	 */
	private Layers layers;
	
	/**
	 * Window
	 */
	private Window window;

	/**
	 * Constructs a new Workspace with given size.
	 * @param dim
	 * @param window
	 */
	public Workspace(Dimension dim, Window window) {
		
		this.window = window;
		
		setSize(dim);
		setBackground(getBackground().darker());
		setLayout(new BorderLayout());
	
		addPanels();

		repaint();
	}
	
	/**
	 * Creates and adds the panels (Properties, Layers and Canvas)
	 */
	private void addPanels() {
		
		properties = new Properties(this);
		add(properties, BorderLayout.EAST);
		
		layers = new Layers(getWidth() , this);
		add(layers, BorderLayout.SOUTH);
		
		canvas = new Canvas(this);
		add(canvas, BorderLayout.CENTER);
	}
	
	/**
	 * Docks the propertypanel (dockingpanel) to either left or right side
	 * @param direction
	 */
	public void dockPPanel(String direction) {
		
		String dir = direction.toLowerCase();
		remove(properties);
		
		if (dir.equals("left"))
			add(properties, BorderLayout.WEST);
		else
			add(properties, BorderLayout.EAST);
		
		doLayout();
		
	}
	
	/**
	 * Sets the image in Canvas, and sets number of pixels in Layers panel.
	 * @param img Image to set.
	 */
	public void setImage(BufferedImage img){		
		canvas.setImage(img);
		
		layers.setNumberOfPixels(img.getWidth()*img.getHeight());
	}
	
	/**
	 * Tells Window that image is to be updated.
	 */
	public void updateImage() {
		window.updateImage();
	}
	
	/**
	 * Sets render options in Window. 
	 * @param options Options
	 */
	public void renderOptions(String[] options) {
		window.renderOptions(options);
	}
	
	/**
	 * fills the parameters in the propertypanel
	 * @param width
	 * @param height
	 * @param pt 
	 */
	public void setRenderingOptions(String width, String height, int pt) {
		properties.setRenderingOptions(width, height, pt);
	}
	
	/**
	 * Resets the panels.
	 */
	public void clear() {
		canvas.reset();
		layers.reset();
		properties.reset();

		repaint();		
	}
	
	/**
	 * Returns Properties panel.
	 * @return Properties
	 */
	public Properties getPropertiesPanel() {
		return properties;
	}
	
	/**
	 * Returns Layers panel
	 * @return Layers
	 */
	public JPanel getLayerPanel() {
		return layers;
	}
	
	/**
	 * Removes layer with specified pixelStream
	 * @param pixelStream
	 */
	public void removeLayer(PixelStream pixelStream) {
		window.removeLayer(pixelStream);
		properties.setPixelStream(null);
	}
	
	/**
	 * Returns whether or not key is pressed
	 * @param keycode
	 * @return is key pressed
	 */
	public boolean isKeyPressed(int keycode) {
		return window.isKeyPressed(keycode);
	}

	/**
	 * Adds a new layer with given pixelstream
	 * @param pixelStream
	 */
	public void addLayer(PixelStream pixelStream) {
		layers.addLayer(pixelStream);
	}
	
	/**
	 * Tells Canvas to zoom in direction
	 * @param dir
	 */
	public void zoom(int dir) {
		if (canvas.hasImage())
			canvas.zoom(dir);
	}

	/**
	 * Tells Layers panel to reset and create new layers for given arraylist
	 * @param pixelStreams Arraylist of pixelstreams
	 */
	public void setPixelStreams(ArrayList<PixelStream> pixelStreams) {
		layers.setPixelStreams(pixelStreams);
	}
	
	/**
	 * Gives focus to Window
	 */
	public void windowFocus() {
		window.requestFocus();
	}

	/**
	 * Passes the selected PixelStream to the properties
	 * @param pixelStream PixelStream
	 */
	public void populateProperties(PixelStream pixelStream) {
		properties.setPixelStream(pixelStream);
	}

	/**
	 * Gets selected layer's pixelstream from Layers
	 * @return
	 */
	public PixelStream getSelectedLayer() {
		return layers.getSelectedPixelstream();
	}

	/**
	 * Returns FontMetrics from properties panel
	 * @return FontMetrics
	 */
	public FontMetrics getMetrics() {
		return properties.getMetrics();
	}

	/**
	 * Tells Layers panel to select layer with given index
	 * @param i
	 */
	public void layerSelect(int i) {
		layers.layerSelect(i);
	}
	
	/**
	 * Tells the gui to check an Integer value by calling another method from Window
	 * @param number
	 * @return Boolean - false if not a number
	 */
	public boolean checkInt(String number) {
		return window.checkInt(number);
	}

	public void effectAction(String effect, PixelStream pixelStream) {
		window.effectAction(effect, pixelStream);
	}
	
	public void updateEffects(String message) {
		window.updateEffects(message);
	}
}