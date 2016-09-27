package glitchy.gui.popup;

import glitchy.core.imageProcessing.PixelStream;
import glitchy.core.effects.Effect;
/**
 * Pop up used for the sort effect
 * @author Aksel
 *
 */
public class SortPopup extends CheckBoxPopup{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs the SortPopup
	 * @param popupController
	 * @param pixelStream
	 */
	public SortPopup(PopupController popupController, PixelStream pixelStream) {
		super(popupController,pixelStream);
		setTitle("Sort");
		setEffect(Effect.SORT);
		createEffectText("sort");
		createPixelOrChannelCheckBox("Sort");
		pack();
	}
	
	
}