package glitchy.gui.popup;

import glitchy.core.imageProcessing.PixelStream;
import glitchy.core.effects.Effect;
/**
 * This the popup used for the invert effect
 * @author Aksel
 *
 */
public class InvertPopup extends CheckBoxPopup{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Construcs the InvertPopup
	 * @param popupController
	 * @param pixelStream
	 */
	public InvertPopup(PopupController popupController, PixelStream pixelStream) {
		super(popupController,pixelStream);
		setTitle("Invert");
		setEffect(Effect.INVERT);
		createEffectText("invert");
		pack();
	}
}