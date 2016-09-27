package glitchy.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * KeyListener used for certain events such as zooming and layer selecting with arrows. 
 * Other shortcuts are made as accelerators on the menu items.
 * @author Rasmus
 *
 */
public class Keys extends KeyAdapter{
	
	/**
	 * A reference to the gui controller, used to call the hotKeys() methods when a key has been clicked
	 */
	private GuiController gui;
	
	/**
	 * Map of keys with a boolean value indicating whether the key is pushed down or not
	 */
	public HashMap<Integer, Boolean> keys;
	
	/**
	 * static final key code ints used to easily ask the listener if a key is pressed down
	 */
	public static final int PLUS = 521, MINUS = 45, SHIFT = 16, ZERO = 48, NUM_ZERO = 96, NUM_PLUS = 107, NUM_MINUS = 109, ARROW_UP = 38, ARROW_DOWN = 40;
	
	/**
	 * Instantiating the map and putting the keys inside with a boolean value
	 * @param gui
	 */
	public Keys(GuiController gui) {
		
		this.gui = gui;
		
		keys = new HashMap<>();
	
		//0
		keys.put(ZERO, false);
		keys.put(NUM_ZERO, false);
		//+
		keys.put(PLUS, false);
		keys.put(NUM_PLUS, false);
		//-
		keys.put(MINUS, false);
		keys.put(NUM_MINUS, false);
		//arrow up
		keys.put(ARROW_UP, false);
		//arrow down
		keys.put(ARROW_DOWN, false);
		
	}

	@Override
	/**
	 * Set the key's boolean to true in the map
	 */
	public void keyPressed(KeyEvent e) {
		keys.put(e.getKeyCode(), true);
		gui.hotKeys(e);
	}

	@Override
	/**
	 * Set the key's boolean to false in the map
	 */
	public void keyReleased(KeyEvent e) {
		keys.put(e.getKeyCode(), false);
	}

}
