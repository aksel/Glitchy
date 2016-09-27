package glitchy.gui;

import glitchy.core.effects.AbstractEffect;
import glitchy.core.imageProcessing.PixelStream;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * This panel contains the properties. 
 * @author Rasmus and Aksel
 */

@SuppressWarnings("serial")
public class Properties extends JPanel{
	
	/**
	 * Reference to the workspace
	 */
	private Workspace workspace;
	
	/**
	 * PopupMenu shown with rightclick
	 */
	private JPopupMenu popUp;
	
	/**
	 * The property listener added to each property and apply button
	 */
	private PropertyListener listener;

	/**
	 * The width of the canvas
	 */
	private JTextField canvasWidth;
	/**
	 * The height of the canvas
	 */
	private JTextField canvasHeight;
	
	/**
	 * The textfields for the different properties
	 */
	private JTextField layerWidth, layerHeight, layerPixelCount, layerPos;
	
	/**
	 * Combobox containing the different rendertypes
	 */
	private JComboBox<String> renderTypesBox;
	/**
	 * The apply button
	 */
	private JButton apply;
	
	/**
	 * Width of 22 characters in the default font.
	 */
	private int panelWidth;

	/**
	 * The different panels
	 */
	private JPanel canvasProperties, layerProperties, wrapperPanel, effectsPanel;

	/**
	 * Selected layer (PixelStream)
	 */
	private PixelStream pixelStream;
	
	/**
	 * FontMetrics object used for sizing
	 */
	private FontMetrics metrics;
	
	/**
	 * Focuslistener added to properties to autoselect and apply properties
	 */
	private FocusAdapter propertyFocusListener;
	
	/**
	 * Creates the initial Panels, properties and listeners.
	 * Adds scroll to properties in case of too small a screen.
	 * @param workspace
	 */
	public Properties(Workspace workspace) {	
		this.workspace = workspace;
		
	    setBorder(Styling.COMPOUND_BORDER);
	    setLayout(new BorderLayout());
	    
	    //Controls listener - same class used for all listeners
	    listener = new PropertyListener();
	    
	    propertyFocusListener = new FocusAdapter() {
	    	public void focusGained(FocusEvent e) {
	    		JTextField field = (JTextField) e.getSource();
	    		field.setSelectionStart(0);
	    		field.setSelectionEnd(field.getText().length());
	    	}
	    	
	    	public void focusLost(FocusEvent e) {
	    		applyRenderOptions(false);
	    	}
	    };

		metrics = this.getFontMetrics(this.getFont());
		
		panelWidth = metrics.stringWidth("MMMMMMMMMMMMMMMMMMMMMM");
		
		wrapperPanel = new JPanel();
		wrapperPanel.add(new JLabel("Properties", JLabel.CENTER));

		createCanvasProperties();
	    createLayerProperties();
	    
	    JPopupMenu popup = createMenuBar();
	    setComponentPopupMenu(popup);
	    
	    apply = new JButton("Apply");
	    apply.setName("apply");
	    apply.addActionListener(listener);
	    apply.setFocusable(false);
	    
	    wrapperPanel.add(apply);
	    
	    Dimension dim = calculatePanelDimension(wrapperPanel);
	    wrapperPanel.setPreferredSize(dim);
	    
	    JScrollPane scroll = new JScrollPane(wrapperPanel,
	    		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	    		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    
	    scroll.setBorder(null);
	    scroll.setComponentPopupMenu(popup);
	    
	    add(scroll, BorderLayout.CENTER);
	}
	
	/**
	 * Creates and adds the properties for the canvas
	 */
	private void createCanvasProperties() {
		
		canvasProperties = new JPanel();
		canvasProperties.setBorder(BorderFactory.createTitledBorder("Canvas"));
		
		canvasWidth = createPropertiesField();
		
		canvasHeight = createPropertiesField();
		
		String[] renderTypesNames = {"Sum", "Bitwise OR", "Difference", "Average"};
	    renderTypesBox = new JComboBox<>(renderTypesNames);
	    renderTypesBox.setEnabled(false);
	    renderTypesBox.addActionListener(listener);
	    
	    //Adding components
	    HelpButton dimHelp = new HelpButton("","dimension",true);
	    canvasProperties.add(Box.createRigidArea(dimHelp.getPreferredSize()));
	    canvasProperties.add(new JLabel("Width" , JLabel.CENTER));
	    canvasProperties.add(dimHelp);
		canvasProperties.add(canvasWidth);
		
	    canvasProperties.add(new JLabel("Height" , JLabel.CENTER));
		canvasProperties.add(canvasHeight);
		
		JPanel renderContainer = new JPanel();
		renderContainer.add(new JLabel("Render" , JLabel.CENTER));
		renderContainer.add(renderTypesBox);
		renderContainer.add(new HelpButton("render","rendertypes",true));
		canvasProperties.add(renderContainer);
	    
	    //Get and set dimension
	    Dimension dim = calculatePanelDimension(canvasProperties);
	    canvasProperties.setPreferredSize(dim);
	    
	    wrapperPanel.add(canvasProperties);
	}
	
	/**
	 * Creates and adds the properties for the selected layer
	 */
	private void createLayerProperties() {
		
		layerProperties = new JPanel();
		layerProperties.setBorder(BorderFactory.createTitledBorder("Layer"));
		
		layerWidth = createPropertiesField();
		layerHeight = createPropertiesField();
		layerPixelCount = createPropertiesField();
		layerPixelCount.setEditable(false);
		layerPos = createPropertiesField();
		
		effectsPanel = new JPanel();
		effectsPanel.setLayout(Styling.FLOW_NOGAP_LEFT);
	    JScrollPane scroll = new JScrollPane(effectsPanel,
	    		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	    		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setPreferredSize(new Dimension(layerPos.getPreferredSize().width, metrics.getHeight() * 5));

		int effectPanelWidth = layerPos.getPreferredSize().width - scroll.getVerticalScrollBar().getPreferredSize().width;
		effectsPanel.setPreferredSize(new Dimension(effectPanelWidth,0));
		
		//Add components
		layerProperties.add(new JLabel("Width " , JLabel.CENTER));
		layerProperties.add(layerWidth);
		
		layerProperties.add(new JLabel("Height " , JLabel.CENTER));
		layerProperties.add(layerHeight);
		
		layerProperties.add(new JLabel("Pixel count " , JLabel.CENTER));
		layerProperties.add(layerPixelCount);
		
		layerProperties.add(new JLabel("Starting Position" , JLabel.CENTER));
		layerProperties.add(layerPos);
		
		layerProperties.add(new JLabel("Applied effects", JLabel.CENTER));
		layerProperties.add(scroll);
		
		//Get and set dimension
		Dimension dim = calculatePanelDimension(layerProperties);
		layerProperties.setPreferredSize(dim);
	    
		wrapperPanel.add(layerProperties);
	}
	
	/**
	 * Creates a property field
	 * @return JTextField used as a propertyfield
	 */
	private JTextField createPropertiesField() {
		JTextField propertiesField = new JTextField();
		propertiesField.setHorizontalAlignment(JTextField.RIGHT);
		propertiesField.setColumns(15);
		propertiesField.addActionListener(listener);
		propertiesField.setEnabled(false);
		propertiesField.setFocusTraversalKeysEnabled(true);
		propertiesField.addFocusListener(propertyFocusListener);
		return propertiesField;
	}

	/**
	 * Calculates the panel dimension based on panelWidth from FontMetrics and a height based on the amount of components
	 * @param panel
	 * @return the calculated dimension
	 */
	private Dimension calculatePanelDimension(JPanel panel){
		
		int panelHeight = 0;
		
		//If the panel has a border, add it to the height.
		Border b = panel.getBorder();
		if(b!=null){
			Insets bInsets = b.getBorderInsets(panel);
			panelHeight += bInsets.top + bInsets.bottom;
		}
		
		Component[] components = panel.getComponents();
	    
		//Add the combined component height
	    for(Component c : components)
	    	panelHeight += c.getPreferredSize().height;
	    
	    //Add the flowlayout's vGap to the height
	    FlowLayout fl = (FlowLayout) panel.getLayout();
	    panelHeight += fl.getVgap()* components.length+1;
	    
	    return new Dimension(panelWidth,panelHeight);
	}

	/**
	 * Creates a menubar shown when right click
	 * used for docking left or right
	 * @return JPopupMenu
	 */
	private JPopupMenu createMenuBar() {
		popUp = new JPopupMenu();
		JMenuItem item = new JMenuItem("Dock left");
		JMenuItem item2 = new JMenuItem("Dock right");
		
		item.addActionListener(arg0 -> workspace.dockPPanel("left"));
		item2.addActionListener(arg0 -> workspace.dockPPanel("right"));
		
		popUp.add(item);
		popUp.add(item2);
		
		return popUp;
	}

	/**
	 * this method fills the parameters in renderingoptions
	 * @param width
	 * @param height
	 * @param type 
	 */
	public void setRenderingOptions(String width, String height, int type) {
		canvasWidth.setText(width);
		canvasWidth.setEnabled(true);
		canvasHeight.setText(height);
		canvasHeight.setEnabled(true);
		renderTypesBox.setSelectedIndex(type);
		renderTypesBox.setEnabled(true);
	}
	
	/**
	 * this is used to get the rendering options when Apply button is clicked
	 * @return
	 */
	public String[] getRenderingOptions() {	
		String[] options = new String[3];
		options[0] = canvasWidth.getText();
		options[1] = canvasHeight.getText();
		options[2] = Integer.toString(renderTypesBox.getSelectedIndex());
		
		return options;	
	}
	
	/**
	 * @return the current starting position of the selected PixelStream
	 */
	public int getPos() {
		if(workspace.checkInt(layerPos.getText()))
			return Integer.parseInt(layerPos.getText());
					
		return pixelStream.getPos();
	}
	
	/**
	 * this method sets the rendering options and repaints the canvas
	 * @param focusWindow - If the workspace should return focus to the window and the canvas
	 */
	public void applyRenderOptions(boolean focusWindow) {
		workspace.renderOptions(getRenderingOptions());
		
		if(focusWindow)
			workspace.windowFocus();
	}
	
	/**
	 * Sets the currently selected pixelstream
	 * @param pixelStream
	 */
	public void setPixelStream(PixelStream pixelStream){
		this.pixelStream = pixelStream;
		
		if(pixelStream==null){
			resetLayer();
		}
		
		else{
			TitledBorder b = (TitledBorder) layerProperties.getBorder();
			b.setTitle("Layer: " + pixelStream.getTitle());
			layerWidth.setText(Integer.toString(pixelStream.getWidth()));
			layerHeight.setText(Integer.toString(pixelStream.getHeight()));
			layerPos.setText(Integer.toString(pixelStream.getPos()));
			layerPos.setEnabled(true);
			layerPixelCount.setText(Integer.toString(pixelStream.getPixels().length));
			fillEffects(pixelStream.getEffects());
		}
		
		revalidate();
		repaint();
	}
	
	/**
	 * Fills the added effects to the applied effects text area
	 * @param effects
	 */
	private void fillEffects(ArrayList<AbstractEffect> effects) {	
		effectsPanel.removeAll();
		
		int buttonWidth = effectsPanel.getPreferredSize().width - JScrollPane.WIDTH;
		for(AbstractEffect effect : effects){
			
			ImageIcon icon;
			if(effect.isEnabled())
				icon = Styling.SHOW_ICON;
			
			else
				icon = Styling.HIDE_ICON;
			
			JButton effectButton = new JButton(effect.toString(),icon);
			Dimension dim = effectButton.getPreferredSize();
			dim.width = buttonWidth;
			effectButton.setPreferredSize(dim);
			effectButton.setHorizontalAlignment(JButton.LEFT);
			effectButton.setBorder(null);
			effectButton.setFocusPainted(false);
			effectButton.setContentAreaFilled(false);
			effectButton.addActionListener(e -> {
                workspace.effectAction("Toggling " + effect.toString() + " to " + !effect.isEnabled(), pixelStream);

                effect.toggleEnabled();
                if(effect.isEnabled())
                    effectButton.setIcon(Styling.SHOW_ICON);

                else
                    effectButton.setIcon(Styling.HIDE_ICON);

                pixelStream.setRendered(false);
                workspace.updateEffects("Toggling " + effect.toString() + " to " + effect.isEnabled());
            });
			
//			effectButton.setComponentPopupMenu(createEffectButtonMenu(pixelStream, effect, effectButton));

			effectsPanel.add(effectButton);
		}
		
		Dimension dim = calculatePanelDimension(effectsPanel);
		dim.width = effectsPanel.getPreferredSize().width;
		effectsPanel.setPreferredSize(dim);
	}
	
//	public JPopupMenu createEffectButtonMenu(PixelStream pixelStream, AbstractEffect effect, JButton button) {
//		popUp = new JPopupMenu();
//
//		JMenuItem item = new JMenuItem("Remove");
//
//		item.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				workspace.effectAction("Removing " + effect.toString(), pixelStream);
//				
//				pixelStream.removeEffect(effect);
//				pixelStream.setRendered(false);
//				
//				workspace.updateEffects("Removing " + effect.toString());
//				
//				effectsPanel.remove(button);
//				revalidate();
//				repaint();
//			}	
//		});
//
//		popUp.add(item);
//		
//		return popUp;
//	}
	
	/**
	 * Resets the propertypanel
	 */
	public void reset(){
		canvasWidth.setEnabled(false);
		canvasHeight.setEnabled(false);
		renderTypesBox.setEnabled(false);
		resetLayer();
	}
	
	/**
	 * Resets the selected layer information
	 */
	public void resetLayer() {
		pixelStream = null;
		TitledBorder b = (TitledBorder) layerProperties.getBorder();
		b.setTitle("Layer");
		
		layerWidth.setText("");
		layerHeight.setText("");
		layerPixelCount.setText("");
		layerPos.setText("");
		layerPos.setEnabled(false);
		effectsPanel.removeAll();
		
		revalidate();
		repaint();	
	}
	
	/**
	 * @return FontMetrics object
	 */
	public FontMetrics getMetrics() {
		return metrics;
	}
	
	/**
	 * This listener calls the applyRenderOptions() method. 
	 * If there is a Pixelstream the starting position of this is set to the one from the propertypanel
	 * @author Rasmus
	 *
	 */
	private class PropertyListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(pixelStream!=null){
				pixelStream.setPos(getPos());
			}

			applyRenderOptions(true);
		}
	}
}