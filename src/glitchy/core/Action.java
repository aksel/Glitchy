package glitchy.core;

/**
 * This enum is used for undoing actions
 * Tells the undoAction method from ActionHistory which change to go back to
 * @author Rasmus
 *
 */
public enum Action {
	
	CHANGE_H,
	CHANGE_W,
	CHANGE_RT, 
	CHANGE_STREAM,
}
