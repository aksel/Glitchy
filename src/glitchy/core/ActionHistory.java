package glitchy.core;

import glitchy.core.imageProcessing.PixelStream;

import java.util.ArrayList;
/**
 * This class handles the ActionHistory
 * @author Rasmus
 *
 */
public class ActionHistory {
	
	/**
	 * This lists contains all the strings of actions that has been done
	 */
	private ArrayList<String> history;
	/**
	 * This list contains all undoable actions and there according history string. The appropiate item will be removed when undone.
	 */
	private ArrayList<String[]> undoHistory;
	/**
	 * This list contains all the previous pixelstreams where a change has been made
	 */
	private ArrayList<PixelStream> changedPixelStreams;
	/**
	 * This list contains all the properties that has been changed
	 */
	private ArrayList<Integer> changedProperties;
	
	/**
	 * Index's for the lists
	 */
	private int actionsDone = 0, streamIndex = 0, propertyIndex = 0;
	
	/**
	 * Flag indicating if a change has been made, used to warn the user when exiting the program without saving
	 */
	private boolean unsavedChanges = false;
	
	/**
	 * Reference to the core controller
	 */
	private CoreController core;
	
	/**
	 * Initializes the lists and creates the ActionHistory
	 * @param core The CoreController
	 */
	public ActionHistory(CoreController core) {
		this.core = core;
		
		undoHistory = new ArrayList<>();
		history = new ArrayList<>();
		changedPixelStreams = new ArrayList<>();
		changedProperties = new ArrayList<>();
	}
	
	/**
	 * Saves the action made in the string array of description - and adds the object to one of the lists
	 * This method is used when the action should be able to be undone. 
	 * @param action This parameter is the enum used to describe the action
	 * @param stringAction The description of the action
	 * @param o The object that needs to be reset to in case of undo
	 */
	public void saveAction(String stringAction, Action action, Object o) {
		String[] params = new String[2];
		params[0] = stringAction;
		params[1] = action.toString();
		
		undoHistory.add(params);
		history.add(stringAction);

		actionsDone++;
		addChange(action, o);
		setUnsavedChanges(true);
	}
	
	/**
	 * Saves only an action string parameter, this action cannot be undone
	 * @param stringAction The description of the action
	 */
	public void saveAction(String stringAction) {
		history.add(stringAction);
		setUnsavedChanges(true);
	}
	
	/**
	 * Undo's the latest action made
	 * Runs through the difference change scenarios and sets back the appropiate data
	 * if render type is 4 in setRenderType() it will be ignored, since there is only 4 render types (0-3)
	 */
	public String undoAction() {
		if (actionsDone <= 0)
			return "";
		
		String[] params = undoHistory.remove(actionsDone - 1);
		
		String a = params[1];
		saveAction("Undo: " + params[0]);
		
		switch(a) {
		case "CHANGE_W":
			int i = changedProperties.remove(--propertyIndex);
			core.setRenderProperties(i, 0, 4, true);
			break;
		case "CHANGE_H":
			core.setRenderProperties(0, changedProperties.remove(--propertyIndex), 4, true);
			break;
		case "CHANGE_RT":
			core.setRenderProperties(0, 0, changedProperties.remove(--propertyIndex), true);
			break;
		case "CHANGE_STREAM":
			PixelStream stream = changedPixelStreams.remove(--streamIndex);
			stream.undo();
			core.populateProperties(stream);
			break;				
		}
		
		actionsDone--;
		setUnsavedChanges(true);
		
		return "Undoing " + params[0] + "...";
	}
	
	/**
	 * This methods gets all the latest actions to be shown in the ActionHistory window popup
	 * @return a String array of the history
	 */
	public String[] getLatestActions() {	
		String[] retHistory = new String[history.size()];
		int index = 0;
		for (int i = history.size(); i > 0; i--) {
			
			String his = history.get(i - 1);
			if (his != null)
				retHistory[index] = his;
			
			index++;	
		}
		
		return retHistory;		
	}
	
	/**
	 * This method sets the current history -  used when loading in a project
	 * @param history The list of actions
	 */
	public void setHistory(String[] history) {
		this.history.clear();
		for (int i = history.length; i > 0; i--) {
			this.history.add(history[i - 1]);
		}
	}
	
	/**
	 * Clears all the lists, and resets indexs and flags. Used when creating or loading a new project
	 */
	public void clear() {
		setUnsavedChanges(true);
		undoHistory.clear();
		history.clear();
		changedPixelStreams.clear();
		changedProperties.clear();
		actionsDone = streamIndex = propertyIndex = 0;
	}

	/**
	 * adds the specific object to the appropiate list 
	 * @param a The Action enum describing the action made
	 * @param o The Object with the data that needs to be reset to in case of undo
	 */
	private void addChange(Action a, Object o) {
		
		switch(a) {
		case CHANGE_H: case CHANGE_W: case CHANGE_RT:
			int i = (int) o;
			changedProperties.add(i);
			propertyIndex++;
			break;
		case CHANGE_STREAM:
			PixelStream stream = (PixelStream) o;
			changedPixelStreams.add(stream);
			stream.action();			
			streamIndex++;
			break;
		}
	}
	
	/**
	 * @return boolean indicating whether any unsaved changes has been made
	 */
	public boolean isUnsavedChanges() {
		return unsavedChanges;
	}
	
	/**
	 * Used to set the unsavedChanged flag if the user saves the project
	 * @param unsavedChanges Boolean indicating whether there are unsaved changes
	 */
	public void setUnsavedChanges(boolean unsavedChanges) {
		this.unsavedChanges = unsavedChanges;
	}
	
}
