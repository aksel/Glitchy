package glitchy.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * 
 * This class handles listening for window events
 * 
 * @author Rasmus
 *
 */
public class WindowListener extends WindowAdapter{
	
	/**
	 * A reference to the guicontroller
	 */
	private GuiController gui;
	
	public WindowListener(GuiController gui) {
		this.gui = gui;
	}
	
	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	/**
	 * Calls the exit method from the GuiController
	 */
	public void windowClosing(WindowEvent e) {
		
		gui.exit();
		
	}

}
