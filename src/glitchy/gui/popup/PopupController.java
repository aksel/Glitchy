package glitchy.gui.popup;

import glitchy.core.effects.Effect;
import glitchy.core.imageProcessing.PixelStream;
import glitchy.gui.FileChooser;
import glitchy.gui.GuiController;
import glitchy.gui.Window;

import java.awt.FontMetrics;
/**
 * This is the controller for the popups
 * @author Rasmus and Aksel
 *
 */
public class PopupController {
	
	/**
	 * This is a reference to the GuiController
	 */
	private GuiController guiController;
	/**
	 * This is a the ActionHistory popup and it should only be instantiated once
	 */
	private ActionHistoryPopup actionHistoryPopup;
	
	/**
	 * Sets up the reference to the GuiController and intializes the ActionHistoryPopup
	 * @param guiController
	 * @param metrics
	 */
	public PopupController(GuiController guiController, FontMetrics metrics){
		this.guiController = guiController;
		actionHistoryPopup = new ActionHistoryPopup(metrics);
	}
	
	/**
	 * Opens an aboutPopup
	 */
	public void requestAboutPopup(){
		new AboutPopup();
	}
	
	/**
	 * Performs a requested effect with the modifiers from the popup
	 * @param pixelStream
	 * @param effect
	 * @param modifiers
	 */
	protected void requestEffect(PixelStream pixelStream, Effect effect, int[] modifiers) {
		guiController.requestEffect(pixelStream, effect, modifiers);
	}
	
	/**
	 * Shows the ActionHistory popup
	 * @param history
	 */
	public void requestHistoryPopup(String[] history) {
		actionHistoryPopup.showHistory(history);
	}
	
	/**
	 * Request an import of raw file with the path selected in the popup
	 * @param path
	 * @param alpha
	 */
	public void importRaw(String path, boolean alpha) {
		guiController.importRaw(path,alpha);
	}
	
	/**
	 * opens an import raw popup
	 * @param fileChooser
	 */
	public void requestImportRawPopup(FileChooser fileChooser, String lastpath){
		ImportRawPopup p = new ImportRawPopup(this, fileChooser, lastpath);
		p.popup(null);
	}
	
	/**
	 * Request a certain popup based on a popup name
	 * @param popupName
	 * @param pixelStream
	 * @param window
	 */
	public void requestPopup(String popupName, PixelStream pixelStream, Window window){

		EffectPopup eP = null;

		switch(popupName){
		case "invert":
			eP = new InvertPopup(this, pixelStream);
			break;
			
		case "bitshift":
			eP = new BitshiftPopup(this, pixelStream);
			break;
			
		case "smear":
			eP = new SmearPopup(this, pixelStream);
			break;
		
		case "sort":
			eP = new SortPopup(this, pixelStream);
			break;
			
		case "shuffle":
			eP = new ShufflePopup(this, pixelStream);
			break;
			
		case "reorder":
			eP = new ReorderPopup(this, pixelStream);
			break;
		}
		
		eP.popup(window);
	}
	
	/**
	 * Updates the ActionHistory popup
	 * @param history
	 */
	public void updateHistoryPopup(String[] history) {
		actionHistoryPopup.updateHistory(history);
	}
}