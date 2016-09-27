package glitchy.gui.popup;

import java.awt.BorderLayout;
import java.awt.Component;

import glitchy.gui.Styling;

import javax.swing.JDialog;

/**
 * Abstract Popup class, that defines how basic popups behave and look in Glitchy.
 * @author Aksel
 */
public abstract class Popup extends JDialog{
	private static final long serialVersionUID = -1954345857787919753L;
	
	/**
	 * Main constructor for popups sets up the initian dialog
	 */
	protected Popup(){
		setResizable(false);
		setLayout(new BorderLayout());
		setIconImage(Styling.ICON);
		setModalityType(ModalityType.APPLICATION_MODAL);
	}
	
	/**
	 * Pops up a popup
	 * @param c
	 */
	public void popup(Component c){
		setLocationRelativeTo(c);
		setVisible(true);
	}
	
	/**
	 * Closes a popup
	 */
	protected void close(){
		setVisible(false);
		dispose();
	}
}