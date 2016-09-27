package glitchy.gui.popup;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import glitchy.core.imageProcessing.PixelStream;
import glitchy.core.effects.Effect;
import glitchy.gui.HelpContent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * This is the class contains the basic functionality for all effect popups
 * @author Aksel
 *
 */
public abstract class EffectPopup extends Popup {
	private static final long serialVersionUID = -963637151067732236L;

	/**
	 * Effect
	 */
	private Effect effect;
	
	/**
	 * The reference to the popup controller
	 */
	private PopupController popupController;
	
	/**
	 * PixelStream to which the effect is to be applied to.
	 */
	protected PixelStream pixelStream;
	/**
	 * Contains various modifiers for the effect.
	 */
	protected JPanel modifiersPanel;
	
	/**
	 * Contains the Apply and Cancel buttons.
	 */
	protected JPanel buttonsPanel;
	
	/**
	 * Sets up the dialog effect popup.
	 * @param popupController
	 * @param pixelStream
	 */
	protected EffectPopup(PopupController popupController, PixelStream pixelStream) {
		super();
		
		this.popupController = popupController;
		this.pixelStream = pixelStream;

		modifiersPanel = new JPanel();
		buttonsPanel = new JPanel();
		populateButtonsPanel();
		
		add(modifiersPanel, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Creates and adds the descriptive help text of the effect
	 * @param helpType
	 */
	protected void createEffectText(String helpType){
		
		String labelText = "<html>";
		labelText += new HelpContent().getHelpDescription(helpType);
		
		labelText += "<br><br>Selected layer: " + pixelStream.getTitle();
		
		int[] range = pixelStream.getSelectionRange();
		if(range[0]==0 && range[1] == pixelStream.getPixels().length-1)
			labelText += "<br>Effect will be applied to the entire layer.";
		else
			labelText += "<br>Affected range: " + range[0] + " to " + range[1];
		
		labelText += "</html>";
		
		add(new JLabel(labelText),BorderLayout.NORTH);
	}
	
	@Override
	protected void close(){
		super.close();
		this.pixelStream = null;
	}
	
	/**
	 * Closes the popup and applies the effect with the chosen modifiers
	 */
	private void closeApply(){
		super.close();

		Effect effect = getEffect();
		int[] modifiers = getModifiers();
		
		if(modifiers != null)
			popupController.requestEffect(pixelStream, effect, modifiers);
	}
	
	/**
	 * Gets the effect enum
	 * @return
	 */
	private Effect getEffect() {
		return effect;
	}
	
	/**
	 * Sets the effect enum
	 * @param effect
	 */
	protected void setEffect(Effect effect) {
		this.effect = effect;
	}
	
	/**
	 * Populates the buttons panel with the buttons Apply and Cancel.
	 */
	private void populateButtonsPanel(){
		
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				closeApply();
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		
		buttonsPanel.add(applyButton);
		buttonsPanel.add(cancelButton);
	}
	
	/**
	 * Should be overriden such that the modifiers panel is populated.
	 */
	protected abstract void populateModifiersPanel();
	
	/**
	 * Should be overriden such that all modifiers are returned in an int[]
	 */
	protected abstract int[] getModifiers();
}
