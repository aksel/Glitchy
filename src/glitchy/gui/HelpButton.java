package glitchy.gui;

import glitchy.gui.popup.HelpPopup;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
/**
 * This class contains the functionality for the HelpButton
 * @author Rasmus
 *
 */
public class HelpButton extends JButton{
	
	/**
	 * Additional info seen in the popup
	 */
	private String additional;
	
	/**
	 * A reference to the HelpContent class with all the descriptions
	 */
	private HelpContent help = new HelpContent();
	
	/**
	 * Sets up the button with the listener if clickable
	 * @param type
	 * @param additional
	 * @param clickable
	 */
	public HelpButton(String type, String additional, boolean clickable) {
		super(Styling.QUESTIONMARK);

		setFocusPainted(false);
		setBorder(null);
		setContentAreaFilled(false);
		
		this.additional = additional;
		
		this.setToolTipText(help.getHelpDescription(type));
		
		if (clickable) {
			addActionListener(e -> buttonClicked());
			
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
	}
	
	/**
	 * This method pops up a HelpPopup if button is clickable
	 */
	private void buttonClicked() {
		new HelpPopup(help.getHelpDescription(additional)).popup(null);
	}
	
}
