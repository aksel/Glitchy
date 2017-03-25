package glitchy.gui;

import glitchy.core.Action;
import glitchy.core.Config;
import glitchy.core.CoreController;
import glitchy.core.effects.Effect;
import glitchy.core.imageProcessing.PixelStream;
import glitchy.core.imageProcessing.RenderProperties;
import glitchy.gui.popup.PopupController;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Controller for GUI layer. Acts as a link between GUI classes, and links to the Core Controller.
 * @author Rasmus, Aksel and Mikkel
 *
 */
public class GuiController {

	/**
	 * Reference to the CoreController
	 */
	private CoreController coreController;
	/**
	 * PopupController
	 */
	private PopupController popupController;
	/**
	 * The window (JFrame)
	 */
	private Window window;

	/**
	 * Config
	 */
	private Config config;

	/**
	 * This is the object containing the RenderProperties
	 */
	private RenderProperties renderProperties;

	/**
	 * The class handling FileChoosing. This class can popup a filechooser and return the selected path.
	 */
	private FileChooser fileChooser;

	/**
	 * Class used for KeyListener for certain hot keys, such as zooming.
	 */
	private Keys keys;

	/**
	 * Initializes the fields, the Styling class and sets up listeners
	 * @param config
	 * @param core
	 */
	public GuiController(Config config, CoreController core){
		Styling.initialize();
		this.config = config;
		coreController = core;

		MenuItemListener listener = new MenuItemListener(this);
		window = new Window(this, listener);

		//Needs metrics to calculate size of certain popups
		popupController = new PopupController(this, window.getMetrics());

		renderProperties = new RenderProperties(0, 0, RenderProperties.SUM);

		fileChooser = new FileChooser();

		keys = new Keys(this);
		window.addKeyListener(keys);

		window.setFocusable(true);
	}

	/**
	 * Copies a selection into a new layer
	 */
	public void copySelection(){
		PixelStream selected = window.getSelectedLayer();
		if(selected != null)
			coreController.copySelection(window.getSelectedLayer());
		else
			errorMessage("No selection.");
	}

	/**
	 * Cuts out a selection from a layer and adds it into a new
	 */
	public void cutSelection() {
		PixelStream selected = window.getSelectedLayer();
		if(selected != null)
			coreController.cutSelection(window.getSelectedLayer());
		else
			errorMessage("No selection.");
	}

	/**
	 * Adds the given PixelStream to a layer
	 * @param pixelStream
	 */
	public void addLayer(PixelStream pixelStream){
		window.addLayer(pixelStream);
	}

	/**
	 * Updates the Canvas with the given BufferedImage
	 * @param img
	 */
	public void updateCanvas(BufferedImage img){
		window.setImage(img);
		window.repaint();
	}

	/**
	 * The the CoreController to update and rerender the image
	 */
	public void updateImage() {
		coreController.updateImage();
	}

	/**
	 * Pops up an error message with the given message
	 * @param msg
	 */
	public void errorMessage(String msg){
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(window, msg, "Error", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Tells the corecontroller to load a raw from the given path
	 * @param path
	 * @param alpha
	 */
	public void importRaw(String path,boolean alpha) {
		coreController.loadRaw(path, alpha);
	}

	/**
	 * Requests an import raw popup from the popupcontroller
	 */
	public void requestImportRawPopup() {
		popupController.requestImportRawPopup(fileChooser, config.getParamaters().get("lastpathraw"));
	}

	/**
	 * Imports an image
	 * Pops up a filechooser and loads the image with the given path (if not cancelled)
	 */
	public void loadImage(){
		String path;
		String lastpath = config.getParamaters().get("lastpathimport");

		path = fileChooser.getPath("image", window, lastpath, "Import image");

		if (path != null)
			coreController.loadImage(path);
	}

	/**
	 * Loads a project
	 * Pops up a filechooser and loads the project with the given path (if not cancelled)
	 */
	public void loadProject() {
		String path;
		String lastpath = config.getParamaters().get("lastpathload");

		path = fileChooser.getPath("project", window, lastpath, "Open project");

		if (path != null) {
			newProject();
			coreController.loadProject(path);
		}
	}

	/**
	 * Requests a new project
	 * Clears corecontroller and window
	 */
	public void newProject() {
		if (coreController.isUnsavedChanges()) {
			int option = JOptionPane.showConfirmDialog(window, "Do you want to save your project?");
			if (option == JOptionPane.YES_OPTION){
				if(saveProject()){
					coreController.newProject();
					window.newProject();
				}
			} else if (option != JOptionPane.CANCEL_OPTION) {
				coreController.newProject();
				window.newProject();
			}
		}
	}

	/**
	 * Exports an image
	 * Pops up a filechooser and exports to the given path (if not cancelled)
	 */
	public void saveImage() {
		String path;
		String lastpath = config.getParamaters().get("lastpathexport");

		path = fileChooser.getPath("export", window, lastpath, "Export");

		if (path != null){
			coreController.saveImage(path , fileChooser.getSelectedFilter());
			config.saveParameter("lastpathexport", path);
			config.saveConfig();
		}
	}

	/**
	 * Saves the current project
	 * Pops up filechooser and saves to give path. (If not cancelled)
	 * @return Project was saved.
	 */
	public boolean saveProject() {
		String path;
		String lastpath = config.getParamaters().get("lastpathsave");

		path = fileChooser.getPath("project", window, lastpath, "Save project");

		if (path != null){
			coreController.saveProject(path, getRenderProperties());
			return true;
		}

		else
			return false;
	}

	/**
	 * shows an optionpane which lets the user cancel the close of the program.
	 * If unsaved changed exists he will be asked whether he wants to save.
	 */
	public void exit() {
		if (coreController.isUnsavedChanges()) {
			int option = JOptionPane.showConfirmDialog(window, "Do you want to save your project?");
			if (option == JOptionPane.YES_OPTION){
				if(saveProject()){
					closeProgram();
				}
			}

			else if(option == JOptionPane.NO_OPTION){
				closeProgram();
			}
		}

		else
			closeProgram();
	}

	/**
	 * closes the program
	 */
	private void closeProgram() {
		window.setVisible(false);
		window.dispose();
		System.exit(0);
	}

	/**
	 * Sets the initial render properties in corecontroller and in the property panel
	 * @param w
	 * @param h
	 * @param pt
	 */
	public void setInitialRenderProperties(int w, int h, int pt){
		setRenderProperties(w,h,pt,false);
		window.setRenderingOptions(Integer.toString(w),
				Integer.toString(h),
				pt);
	}

	public void updateEffects(String message) {
		coreController.updateEffects(message);
	}

	/**
	 * Sets the render properties and updates the image
	 * Used when setting renderproperties in the propertypanel
	 * @param width
	 * @param height
	 * @param rt
	 */
	public void setRenderProperties(String width, String height, int rt) {

		if (!checkInt(width))
			return;
		if (!checkInt(height))
			return;

		int w = Integer.parseInt(width);
		int h = Integer.parseInt(height);

		coreController.saveAction(w, h, rt, renderProperties);

		renderProperties.w = w;
		renderProperties.h = h;
		renderProperties.renderType = rt;

		coreController.updateImage();
	}

	/**
	 * method used to set renderproperties when loading an image and when loading a project
	 * @param width
	 * @param height
	 * @param rt
	 * @param update - Whether the method also should re-render the image and update canvas.
	 */
	public void setRenderProperties(int width, int height, int rt, boolean update) {
		if (width != 0)
			renderProperties.w = width;
		if (height != 0)
			renderProperties.h = height;
		if (rt < 4)
			renderProperties.renderType = rt;

		if (update)
			coreController.updateImage();
	}

	/**
	 * this method checks a string variable from property that should be made to an int
	 * @param var
	 * @return
	 */
	public boolean checkInt(String var) {
		try {
			Integer.parseInt(var);
		} catch (NumberFormatException e) {
			errorMessage("Please input a number");
			return false;
		}
		return true;
	}

	/**
	 * tells the window to zoom
	 * @param dir (either in or out)
	 */
	public void zoom(int dir) {
		window.zoom(dir);
	}

	/**
	 * @return the current renderproperties
	 */
	public RenderProperties getRenderProperties() {
		return renderProperties;
	}

	/**
	 * Tells if this key is pressed.
	 * @param keyCode
	 * @return boolean
	 */
	public boolean isKeyPressed(int keyCode) {
		return keys.keys.get(keyCode);
	}

	/**
	 * Sets the pixelstreams. Used when loading project.
	 * @param pixelStreams
	 */
	public void setPixelStreams(ArrayList<PixelStream> pixelStreams) {
		window.setPixelStreams(pixelStreams);
	}

	/**
	 * Requests an effect to be made in the corecontroller
	 * @param pixelStream
	 * @param effect
	 * @param modifiers
	 */
	public void requestEffect(PixelStream pixelStream, Effect effect, int[] modifiers) {
		coreController.requestEffect(window.getSelectedLayer(), effect, modifiers);
	}

	public void requestPopup(String popupName) {
		PixelStream selectedPixelStream = window.getSelectedLayer();

		if(selectedPixelStream == null)
			errorMessage("No layer selected.");
		else
			popupController.requestPopup(popupName,selectedPixelStream,window);
	}

	/**
	 * Removes a layer based on the given PixelStream
	 * @param pixelStream
	 */
	public void removeLayer(PixelStream pixelStream) {
		coreController.removePixelStream(pixelStream);
	}

	/**
	 * Method called everytime a key is pressed in the Keys class
	 * Handles certain hotkeys such as zoom and layer selecting with arrow keys
	 * @param e
	 */
	public void hotKeys(KeyEvent e) {

		if (e.isControlDown()) {
			if (isKeyPressed(Keys.PLUS) || isKeyPressed(Keys.NUM_PLUS)) {
				zoom(-1);
				return;
			}
			if (isKeyPressed(Keys.MINUS) || isKeyPressed(Keys.NUM_MINUS)) {
				zoom(1);
				return;
			}
			if (isKeyPressed(Keys.ZERO) || isKeyPressed(Keys.NUM_ZERO)) {
				zoom(0);
				return;
			}
		}

		if (isKeyPressed(Keys.ARROW_UP))
			window.layerSelect(0);

		if (isKeyPressed(Keys.ARROW_DOWN))
			window.layerSelect(1);

	}

	/**
	 * Requests an about popup from the PopupController
	 */
	public void requestAboutPopup() {
		popupController.requestAboutPopup();
	}

	/**
	 * Tells the window to show a loadingbar
	 * @param message
	 */
	public void startLoading(String message){
		SwingUtilities.invokeLater(() -> window.startLoading(message));
	}

	/**
	 * Tells the window to hide the loadingbar
	 */
	public void stopLoading(){
		window.stopLoading();
	}

	/**
	 * Dock propertypanel based on a given direction
	 * @param dir
	 */
	public void dockPanel(String dir) {
		window.dockPanel(dir);
	}

	/**
	 * Requests the ActionHistory popup from the PopupController
	 */
	public void showHistory() {
		popupController.requestHistoryPopup(coreController.getActionHistory());
		window.requestFocus();
	}

	/**
	 * Updates the ActionHistory
	 */
	public void updateHistory() {
		popupController.updateHistoryPopup(coreController.getActionHistory());
	}

	/**
	 * Undos the latest action
	 */
	public void undoAction() {
		coreController.undoAction();
	}

	/**
	 * Populate the propertyPanel with the information from the given PixelStream
	 * @param pixelStream
	 */
	public void populateProperties(PixelStream pixelStream) {
		window.populateProperties(pixelStream);
	}

	public void effectAction(String effect, PixelStream pixelStream) {
		coreController.saveActionUndo(effect, Action.CHANGE_STREAM, pixelStream);
	}
}
