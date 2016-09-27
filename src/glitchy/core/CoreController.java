package glitchy.core;

import glitchy.core.imageProcessing.ImageController;
import glitchy.core.imageProcessing.PixelStream;
import glitchy.core.imageProcessing.RenderProperties;
import glitchy.core.io.IOController;
import glitchy.core.effects.Effect;
import glitchy.gui.GuiController;

import java.awt.image.BufferedImage;

/**
 * This class acts as a controller for the entire Core layer. Also controls requests 
 * between the Core and GUI layer.
 * @author Aksel, Mikkel and Rasmus
 */
public class CoreController {

	/**
	 * A reference to the GuiController.
	 */
	private GuiController guiController;
	
	/**
	 * A reference to the ImageController.
	 */
	private ImageController imageController;
	
	/**
	 * A reference to the IOController.
	 */
	private IOController ioController;

	/**
	 * Config data, read from the glitchy.ini file.
	 */
	private Config config;
	
	/**
	 * Contains all the actions performed in the current project.
	 */
	private ActionHistory history;

	/**
	 * Flag, determines whether an image is the first to be loaded.
	 */
	private boolean first = true;

	/**
	 * Instantiates the various controllers used in the program.
	 */
	public CoreController(){		
		imageController = new ImageController();
		ioController = new IOController();
		config = new Config();
		guiController = new GuiController(config, this);
		history = new ActionHistory(this);
	}
	
	/**
	 * Tells given PixelStream to copy a selection, 
	 * tells ImageController to store the selection as a PixelStream, 
	 * and tells GuiController to add it to the Layers.
	 * @param pixelStream PixelStream to copy from.
	 */
	public void copySelection(PixelStream pixelStream){
		String message = "Duplicate selection: " + pixelStream.getSelectionS() + " to " + pixelStream.getSelectionE() + " from " + pixelStream.getTitle();
		saveActionUndo(message, Action.CHANGE_STREAM, pixelStream);
		
		PixelStream copy = imageController.copySelectionToNewLayer(pixelStream);
		
		guiController.addLayer(copy);
		updateImage();
	}
	
	/**
	 * Tells given PixelStream to cut a selection (deleting the selection from itself), 
	 * tells ImageController to store the selection as a PixelStream, 
	 * and tells GuiController to add it to the Layers.
	 * @param pixelStream PixelStream to cut from.
	 */
	public void cutSelection(PixelStream pixelStream){
		String message = "Extracting selection: " + pixelStream.getSelectionS() + " to " + pixelStream.getSelectionE() + " from " + pixelStream.getTitle();
		saveActionUndo(message, Action.CHANGE_STREAM, pixelStream);
		
		PixelStream copy = imageController.cutSelectionToNewLayer(pixelStream);
		
		guiController.addLayer(copy);
		updateImage();
	}
	
	/**
	 * Sets the rendering properties. 
	 * This only happens when the first image is loaded.
	 * @param pixelStream PixelStream of first loaded image.
	 */
	private void firstImage(PixelStream pixelStream){
		int width  = pixelStream.getWidth();
		int height = pixelStream.getHeight();
		guiController.setInitialRenderProperties(width,height, 0);
		first = false;
	}
	
	/**
	 * Returns ActionHistory.
	 * @return Action history.
	 */
	public String[] getActionHistory() {
		return history.getLatestActions();
	}
	
	/**
	 * Returns a boolean from actionHistory, indicating whether unsaved
	 * changes has been made.
	 * @return boolean indicating unsaved changes
	 */
	public boolean isUnsavedChanges() {
		return history.isUnsavedChanges();
	}
	
	/**
	 * Starts a thread, that loads a BufferedImage, 
	 * and converts it into a PixelStream. The PixelStream is then passed 
	 * to the GuiController, to be added as a Layer.
	 * @param path to image. 
	 */
	public void loadImage(String path){	
		config.saveParameter("lastpathimport", path);
		config.saveConfig();

		new Thread(){
			@Override
			public void run() {
				BufferedImage img = ioController.getImage(path);

				if(img == null){
					guiController.stopLoading();
					guiController.errorMessage("Could not load image: " + path);
					return;
				}

				String title = path.substring(path.lastIndexOf('\\')+1);
				PixelStream pixelStream = imageController.addImage(title, img);

				if(first)
					firstImage(pixelStream);

				guiController.stopLoading();
				guiController.addLayer(pixelStream);
				updateImage();

				saveAction("Imported image: " + title);
				
				//dispose of this thread when done
				try {
					join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}.start();

		guiController.startLoading("Importing image...");
	}
	
	/**
	 * Loads a serialized Project object, 
	 * and initializes the program with the saved data.
	 * @param path Path to Project file.
	 */
	public void loadProject(String path) {	
		config.saveParameter("lastpathload", path);
		config.saveConfig();

		String title = path.substring(path.lastIndexOf('\\')+1);
		
		Project project = ioController.loadProject(path);

		if (project.getPixelStreams().size() > 0)
			first = false;

		imageController.setPixelStreams(project.getPixelStreams());
		guiController.setPixelStreams(project.getPixelStreams());
		guiController.setInitialRenderProperties(
				project.getWidth(),
				project.getHeight(),
				project.getRenderType());
		saveAction("Loaded project: " + title);
		history.setHistory(project.getActions());

		updateImage();
	}

	/**
	 * Starts a thread, that imports any file, 
	 * and converts its raw data into pixels, that are then 
	 * added to a PixelStream.
	 * @param path Filepath.
	 * @param alpha whether the user wants alpha or not
	 */
	public void loadRaw(String path,boolean alpha){

		config.saveParameter("lastpathraw", path);
		config.saveConfig();

		new Thread(){
			@Override
			public void run() {
				String title = path.substring(path.lastIndexOf('\\')+1);				

				int[] convertedRaw = ioController.readRaw(path,alpha);

				PixelStream pixelStream = imageController.addRaw(title,convertedRaw,alpha);

				if(first)
					firstImage(pixelStream);

				guiController.stopLoading();
				guiController.addLayer(pixelStream);
				updateImage();

				saveAction("Imported raw: " + title);
				
				//Dispose of this thread when done
				try {
					join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();

		guiController.startLoading("Importing raw...");
	}
	
	/**
	 * Resets the program, i.e. clears current data.
	 */
	public void newProject() {
		imageController.clear();
		imageController = new ImageController();
		first = true;
		history.clear();
		guiController.updateHistory();
		System.gc();
	}
	
	/**
	 * Tells GuiController to populate the Properties panel, with a PixelStream's 
	 * properties.
	 * @param stream The Pixelstream
	 */
	public void populateProperties(PixelStream stream) {
		guiController.populateProperties(stream);
	}
	
	/**
	 * Starts a thread, that tells the ImageController to apply the 
	 * desired effect to the desired PixelStream.
	 * @param pixelStream The PixelStream.
	 * @param effect The desired Effect
	 * @param modifiers Modifiers for the Effect.
	 */
	public void requestEffect(PixelStream pixelStream, Effect effect, int[] modifiers) {
		String message = "Applying " + effect.toString() + " to " + pixelStream.getTitle();
		saveActionUndo(message, Action.CHANGE_STREAM, pixelStream);
		new Thread(){
			@Override
			public void run() {
				imageController.applyEffect(pixelStream, effect, modifiers);
				
				guiController.stopLoading();
				guiController.populateProperties(pixelStream);				
				
				updateImage();
				
				//Dispose of this thread when done
				try {
					join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
		guiController.startLoading(message);
	}

	/**
	 * Tells the ImageController to remove the given PixelStream, 
	 * and then updates the image.
	 * @param pixelStream The PixelStream to be removed.
	 */
	public void removePixelStream(PixelStream pixelStream){
		saveAction("Removed image: " + pixelStream.getTitle());
		imageController.removePixelStream(pixelStream);
		updateImage();
	}
	
	/**
	 * This methods saves a string describing an action to the ActionHistory
	 * This action cannot be undone
	 * @param action The description of the action
	 */
	public void saveAction(String action) {
		history.saveAction(action);
		guiController.updateHistory();
	}
	
	/**
	 * This method saves a property change action to the ActionHistory
	 * The method will check which property has been changed
	 * @param w width 
	 * @param h height
	 * @param rt rendertype
	 * @param renderProperties renderproperties
	 */
	public void saveAction(int w, int h, int rt, RenderProperties renderProperties) {
		if (w != renderProperties.w)
			saveActionUndo("Changed width from " + renderProperties.w + " to: " + w, Action.CHANGE_W, renderProperties.w);

		if (h != renderProperties.h)
			saveActionUndo("Changed height from " + renderProperties.h + " to: " + h, Action.CHANGE_H, renderProperties.h);

		if (rt != renderProperties.renderType) {
			saveActionUndo("Changed type from " + renderProperties.renderTypeToString() + " to " + renderProperties.renderTypeToString(rt), Action.CHANGE_RT, renderProperties.renderType);
		}
	}
	
	/**
	 * This method saves a description of an action.
	 * This action CAN be undone.
	 * @param action The description of the action
	 * @param a an Action enum indicating the action
	 * @param o the Object that needs to be reset to in case of undo
	 */
	public void saveActionUndo(String action, Action a, Object o) {
		history.saveAction(action, a, o);
		guiController.updateHistory();
	}
	
	/**
	 * Starts a thread, that first tells the ImageController to render 
	 * and image, based on renderingproperties and specified imageformat.  
	 * Then tells the IOController to save the image to the specified filepath.
	 * @param path Filepath
	 * @param extension Imageformat, e.g. PNG, JPG, BMP, etc.
	 */
	public void saveImage(String path, String extension){
		String title = path.substring(path.lastIndexOf('\\')+1);
		String message = "Exporting image: " + title;
		saveAction(message);
		
		new Thread() {
			
			public void run() {
				BufferedImage img = imageController.render(guiController.getRenderProperties(),extension);
				boolean success = ioController.exportImage(img , path, extension);
				if(!success)
					guiController.errorMessage("Failed to save image: " + path);
				
				guiController.stopLoading();
				
				//Dispose of this thread when done
				try {
					join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}.start();
		
		guiController.startLoading(message);
	}
	
	/**
	 * Saves the current data into a serialized Project object.
	 * @param path Where to save the project.
	 * @param rP RenderingProperties.
	 */
	public void saveProject(String path, RenderProperties rP) {	
		config.saveParameter("lastpathsave", path);
		config.saveConfig();

		String title = path.substring(path.lastIndexOf('\\')+1);
		saveAction("Saved project: " + title);

		Project project = new Project(imageController.getPixelStreams(), history.getLatestActions(),
				rP.w,
				rP.h,
				rP.renderType);

		ioController.saveProject(project, path);		
		
		history.setUnsavedChanges(false);
	}

	/**
	 * Tells GuiController to set the RenderProperties.
	 * @param w Canvas width.
	 * @param h Canvas height.
	 * @param rt RenderType.
	 * @param update Whether or not to update the image.
	 */
	public void setRenderProperties(int w, int h, int rt, boolean update) {
		guiController.setRenderProperties(w, h, rt, update);
	}
	
	/**
	 * This method undo's an action
	 * It will also update the ActionHistory window.
	 */
	public void undoAction() {
		String message = history.undoAction();
		guiController.updateHistory();
		if (message != "") 
			updateEffects(message);
	}
	
	/**
	 * This method checks for changes on the pixelstream and rerenders the image with a loadingbar
	 * @param message the message for the loadingbar 
	 */
	public void updateEffects(String message) {
		new Thread() {
			
			public void run() {
				
				imageController.checkPixelStreams();
				updateImage();
				
				guiController.stopLoading();
				
				//Dispose of this thread when done
				try {
					join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}.start();
		
		guiController.startLoading(message);
	}
	
	/**
	 * Tells ImageController to render an image, and passes it to the GuiController.
	 */
	public void updateImage() {
		BufferedImage rendered = imageController.render(guiController.getRenderProperties());
		guiController.updateCanvas(rendered);
		System.gc();
	}

}
