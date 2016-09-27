package glitchy.gui.popup;

import glitchy.core.imageProcessing.PixelStream;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * The popup used for effect popups with posibility of checking properties
 * @author Aksel
 *
 */
public class CheckBoxPopup extends EffectPopup{
	private static final long serialVersionUID = 1L;
	/**
	 * Checkboxes for the alpha, red, green, blue channel
	 */
	protected JCheckBox a,r,g,b;
	/**
	 * Checkbox for selecting either a general pixel select og seperate channel select
	 */
	protected JCheckBox pixelOrChannel;
	
	/**
	 * Constructs the checkboxpopup
	 * @param popupController
	 * @param pixelStream
	 */
	public CheckBoxPopup(PopupController popupController, PixelStream pixelStream) {
		super(popupController,pixelStream);
		populateModifiersPanel();
	}

	@Override
	protected void populateModifiersPanel() {

		if(pixelStream.hasAlpha())
			a = createAndAddCheckbox("Alpha");
		r = createAndAddCheckbox("Red");
		g = createAndAddCheckbox("Green");
		b = createAndAddCheckbox("Blue");
	}
	
	/**
	 * This method creates and adds a certain checkbox with the requested text
	 * @param text
	 * @return the checkbox added
	 */
	private JCheckBox createAndAddCheckbox(String text){
		JCheckBox c = new JCheckBox();
		c.setText(text);
		c.setFocusable(false);
		c.setSelected(true);
		modifiersPanel.add(c);
		return c;
	}

	@Override
	protected int[] getModifiers() {

		//User wants effect applied pixel-by-pixel
		if(pixelOrChannel != null && pixelOrChannel.isSelected()){
			int[] modifiers = {-1};
			return modifiers;
		}
		
		ArrayList<Integer> modifiersList = new ArrayList<Integer>();
		
		//If PixelStream has Alpha, and it is selected, add Alpha Mask
		if(pixelStream.hasAlpha() && a.isSelected())
			modifiersList.add(0xff000000);
		
		//If Red is selected, add Red Mask
		if(r.isSelected())
			modifiersList.add(0xff0000);
		
		//If Green is selected, add Green Mask
		if(g.isSelected())
			modifiersList.add(0xff00);
		
		//If Blue is selected, add Blue Mask
		if(b.isSelected())
			modifiersList.add(0xff);
		
		int size = modifiersList.size();
		
		if(size == 0)
			return null;
		
		int[] modifiers = new int[size];
		
		for(int i = 0; i < size; i++)
			modifiers[i] = modifiersList.get(i);
		
		return modifiers;
	}
	
	/**
	 * This method creates the "pixel or channel" checkboxes and sets up their listener with actions
	 * @param effect
	 */
	protected void createPixelOrChannelCheckBox(String effect){
		
		pixelOrChannel = new JCheckBox();
		pixelOrChannel.setText(effect + " by Pixel");
		pixelOrChannel.setFocusable(false);
		pixelOrChannel.setSelected(true);
		
		if(pixelStream.hasAlpha())
			a.setEnabled(false);
		
		r.setEnabled(false);
		g.setEnabled(false);
		b.setEnabled(false);
		
		pixelOrChannel.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				
				boolean isSelected = pixelOrChannel.isSelected();
				
				if(pixelStream.hasAlpha()){
					a.setEnabled(!isSelected);
					a.setSelected(true);
				}
				
				r.setEnabled(!isSelected);
				g.setEnabled(!isSelected);
				b.setEnabled(!isSelected);
				r.setSelected(true);
				g.setSelected(true);
				b.setSelected(true);
			}
		});
		
		add(pixelOrChannel, BorderLayout.WEST);
		
	}

}
