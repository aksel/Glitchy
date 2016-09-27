package glitchy.gui.popup;

import glitchy.core.imageProcessing.PixelStream;
import glitchy.core.effects.Effect;
/**
 * This is the popup used for the Shuffle effect
 * @author Aksel
 *
 */
public class ShufflePopup extends CheckBoxPopup{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs ShufflePopup
	 * @param popupController
	 * @param pixelStream
	 */
	public ShufflePopup(PopupController popupController, PixelStream pixelStream) {
		super(popupController,pixelStream);
		setTitle("Shuffle");
		setEffect(Effect.SHUFFLE);
		createEffectText("shuffle");
		createPixelOrChannelCheckBox("Shuffle");
		pack();
	}
}