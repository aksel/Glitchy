package glitchy.gui.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import glitchy.core.effects.Effect;
import glitchy.core.imageProcessing.PixelStream;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
/**
 * Popup used for the bitshift effect
 * @author Rasmus and Aksel
 *
 */
public class BitshiftPopup extends EffectPopup{
	private static final long serialVersionUID = -2570482798128907050L;
	
	/**
	 * The slider indicating how much the bits should be shifted
	 */
	private JSlider slider;
	/**
	 * The int indicating how much the bits should be shifted
	 */
	private int numberOfBits = 24;
	
	/**
	 * The JTextField containing and indicating how many bits should be shifted
	 */
	private JTextField input;
	
	/**
	 * Construcs the Bitshift popup
	 * @param popupController
	 * @param pixelStream
	 */
	public BitshiftPopup(PopupController popupController, PixelStream pixelStream) {
		super(popupController,pixelStream);
		setTitle("BitShift");
		setEffect(Effect.BITSHIFT);
		
		if(pixelStream.hasAlpha())
			numberOfBits = 32;
		
		populateModifiersPanel();
		
		createEffectText("bitshift");
		
		pack();
	}

	@Override
	protected void populateModifiersPanel() {
		slider = new JSlider(0,numberOfBits,1);
		input = new JTextField(2);
		
		slider.setMajorTickSpacing(8);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setSnapToTicks(true);
		slider.setFocusable(false);
		
		slider.addPropertyChangeListener(evt -> input.setText(Integer.toString(slider.getValue())));
		
		input.addActionListener(arg0 -> slider.setValue(Integer.parseInt(input.getText())));
		
		modifiersPanel.add(new JLabel("Number of bits"));
		modifiersPanel.add(input);
		modifiersPanel.add(slider);
	}

	@Override
	protected int[] getModifiers() {

		int[] modifiers = new int[2];
		modifiers[0] = numberOfBits;
		modifiers[1] = Integer.parseInt(input.getText());
		
		if(modifiers[1]==0)
			return null;
		
		return modifiers;
	}

}
