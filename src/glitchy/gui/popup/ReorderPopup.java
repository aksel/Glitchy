package glitchy.gui.popup;

import glitchy.core.imageProcessing.PixelStream;
import glitchy.core.effects.Effect;
/**
 * This is the popup used for the reordering effect
 * @author Rasmus and Aksel
 *
 */
public class ReorderPopup extends EffectPopup{
	private static final long serialVersionUID = 1L;
	
	/**
	 * A Channel panel containing the functionality for drawing draggable colored boxes used to reorder
	 */
	private ChannelPanel channelPanel;
	
	/**
	 * Constructs the ReorderPopup
	 * @param popupController
	 * @param pixelStream
	 */
	public ReorderPopup(PopupController popupController, PixelStream pixelStream) {
		super(popupController,pixelStream);
		setTitle("Reorder");
		setEffect(Effect.REORDER);
		populateModifiersPanel();
		createEffectText("reorder");
		
		pack();
	}

	@Override
	protected int[] getModifiers() {

		int[] modifiers = new int[4];
		int[] masks = { 0xff000000,
						0x00ff0000,
						0x0000ff00,
						0x000000ff
		};
		int[] indexs = channelPanel.getIndexs();
				
		modifiers[0] = masks[indexs[0]];
		modifiers[1] = masks[indexs[1]];
		modifiers[2] = masks[indexs[2]];
		modifiers[3] = masks[indexs[3]];

		return modifiers;
	}

	@Override
	protected void populateModifiersPanel() {
		channelPanel = new ChannelPanel(pixelStream.hasAlpha());
		modifiersPanel.add(channelPanel);
	}
}
