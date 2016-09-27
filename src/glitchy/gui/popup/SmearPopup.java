package glitchy.gui.popup;

import glitchy.core.effects.Effect;
import glitchy.core.imageProcessing.PixelStream;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
/**
 * The pop up used for the Smear effect
 * @author Rasmus and Aksel
 *
 */
public class SmearPopup extends EffectPopup{
	private static final long serialVersionUID = -1503979264548885689L;
	
	/**
	 * Sliders for intensity and length
	 */
	private JSlider lengthSlider, intensitySlider;
	
	/**
	 * JTextFields used for intensity and length
	 */
	private JTextField inputLength, inputIntensity;
	
	/**
	 * A label with a warning - shown when above 200 length
	 */
	private JLabel warning;
	
	/**
	 * Constructs Smear popup
	 * @param popupController
	 * @param pixelStream
	 */
	protected SmearPopup(PopupController popupController, PixelStream pixelStream) {
		super(popupController, pixelStream);

		setTitle("Smear");
		setEffect(Effect.SMEAR);
		populateModifiersPanel();
		createEffectText("smear");
		pack();
	}

	@Override
	protected void populateModifiersPanel() {
		modifiersPanel.setLayout(new BorderLayout());
		
		warning = new JLabel("This may take a while...");
		warning.setForeground(Color.red);
		warning.setVisible(false);
		modifiersPanel.add(warning, BorderLayout.NORTH);
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		
		bottom.add(createLengthSlider(), BorderLayout.NORTH);	
		bottom.add(createIntensitySlider(), BorderLayout.SOUTH);
		modifiersPanel.add(bottom, BorderLayout.CENTER);
	}
	
	/**
	 * Creates and adds the slider for the length of the smear
	 * @return
	 */
	private JPanel createLengthSlider() {
		int maxLength = 300;
		
		JPanel content = new JPanel();
		lengthSlider = new JSlider(0, maxLength, 1);
		inputLength = new JTextField(4);
		inputLength.setText("100");
		lengthSlider.setValue(100);
		
		lengthSlider.setMajorTickSpacing(50);
		lengthSlider.setMinorTickSpacing(5);
		lengthSlider.setPaintTicks(true);
		lengthSlider.setPaintLabels(true);
		lengthSlider.setSnapToTicks(true);
		lengthSlider.setPaintLabels(true);
		lengthSlider.setFocusable(false);
		
		lengthSlider.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				inputLength.setText(Integer.toString(lengthSlider.getValue()));
				if (lengthSlider.getValue() >= 200) {
					warning.setVisible(true);
				} else {
					warning.setVisible(false);
				}
					
			}
		});
		
		inputLength.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lengthSlider.setValue(Integer.parseInt(inputLength.getText()));
			}
		});
		
		content.add(new JLabel("Length"));	
		content.add(inputLength);
		content.add(lengthSlider);
		
		return content;
	}
	
	/**
	 * Creates and adds the intensity slider
	 * @return
	 */
	private JPanel createIntensitySlider() {
		int maxLength = 100;
		
		JPanel content = new JPanel();
		intensitySlider = new JSlider(0, maxLength, 1);
		inputIntensity = new JTextField(4);
		inputIntensity.setText("100");
		intensitySlider.setValue(100);
		
		intensitySlider.setMajorTickSpacing(10);
		intensitySlider.setPaintTicks(true);
		intensitySlider.setPaintLabels(true);
		intensitySlider.setSnapToTicks(true);
		intensitySlider.setFocusable(false);
		
		intensitySlider.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				inputIntensity.setText(Integer.toString(intensitySlider.getValue()));
			}
		});
		
		inputIntensity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				intensitySlider.setValue(Integer.parseInt(inputIntensity.getText()));
			}
		});
		
		content.add(new JLabel("Intensity"));
		content.add(inputIntensity);
		content.add(new JLabel("%"));
		content.add(intensitySlider);
		
		return content;
	}

	@Override
	protected int[] getModifiers() {

		int[] modifiers = new int[2];
		modifiers[0] = Integer.parseInt(inputLength.getText());
		modifiers[1] = Integer.parseInt(inputIntensity.getText()) / 10;
		
		return modifiers;
	}

}
