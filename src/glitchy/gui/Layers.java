package glitchy.gui;

import glitchy.core.imageProcessing.PixelStream;
import glitchy.gui.popup.HelpPopup;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

/**
 * Contains all LayerPanels. Creates new ones based on recently imported PixelStreams.
 * 
 * @author Aksel, Rasmus
 *
 */
public class Layers extends JPanel{
	private static final long serialVersionUID = -3555839091193784699L;

	/**
	 * Contains all the layers.
	 */
	private ArrayList<LayerPanel> layers;

	/**
	 * Contains all the layers.
	 */
	private JPanel layerWrapper;
	
	/**
	 * JScrollPane with the layerWrapper as its viewport.
	 */
	private JScrollPane scrollPane;
	
	/**
	 * The layerWrapper's dimension. Changes height based on number of layers.
	 */
	private Dimension layerWrapperDim;
	
	/**
	 * Dimension used by layer info panels.
	 */
	private Dimension layerInfoDim;
	
	/**
	 * Dimension used by layers.
	 */
	private Dimension layerDim;
	
	/**
	 * The canvas' total number of pixels.
	 */
	private int numberOfPixels;
	
	/**
	 * The listener used for events on the layer panels
	 */
	private LayerPanelListener layerPanelListener;
	
	/**
	 * A reference to the workspace
	 */
	private Workspace workspace;
	
	/**
	 * Right click pop up
	 */
	private JPopupMenu popUp;
	
	/**
	 * The id of the layers 
	 * Incremented everytime a layer is added
	 */
	private int layerIdentifier;
	
	/**
	 * layerHeight and borderInsets field are used for size the layers and this "LayersPanel" properly according to different screens
	 */
	private int layerHeight;
	private Insets borderInsets;
	
	/**
	 * Metrics used for sizing
	 */
	private FontMetrics metrics;
	
	/**
	 * The constructor sets up the metrics used for sizing
	 * and creates the initial wrapper panel and listeners
	 * @param w
	 * @param workspace
	 */
	public Layers(int w, Workspace workspace){
		this.workspace = workspace;
		
		setBorder(Styling.COMPOUND_BORDER);
		borderInsets = getBorder().getBorderInsets(this);
		
		metrics = getFontMetrics(getFont());
		layerHeight = metrics.getHeight()*2;
		
		setPreferredSize(new Dimension(w,
				layerHeight*5 +
				borderInsets.top +
				borderInsets.bottom));
		
		setLayout(new BorderLayout(0,0));
		
		createLayerWrapper();
		
		int layerInfoWidth = metrics.stringWidth("MMMMMMMMMMMMMMMMMMMMMM");
		layerInfoDim = new Dimension(layerInfoWidth,layerHeight);
		layerDim = new Dimension(layerWrapperDim.width - layerInfoDim.width,layerHeight);

		layers = new ArrayList<>();
		
		layerPanelListener = new LayerPanelListener();
		
		add(scrollPane,BorderLayout.CENTER);
	}
	
	/**
	 * Creates the layer wrapper panel, containing the different layers
	 */
	private void createLayerWrapper(){
		
		scrollPane = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		int scrollBarWidth = scrollPane.getVerticalScrollBar()
				.getPreferredSize()
				.width;
		
		int layerWrapperWidth = getPreferredSize().width -
				(borderInsets.left+borderInsets.right+scrollBarWidth);
		
		layerWrapperDim = new Dimension(layerWrapperWidth,0);
		layerWrapper = new JPanel();
		layerWrapper.setPreferredSize(layerWrapperDim);
		layerWrapper.setLayout(Styling.FLOW_NOGAP_LEFT);
		scrollPane.setViewportView(layerWrapper);
	}
	
	/**
	 * Resets the Layers panel.
	 */
	public void reset(){
		
		layerWrapperDim.height = 0;
		layerIdentifier = 0;

		layerWrapper.removeAll();
		
		layers.clear();
		
	}
	
	/**
	 * Fills the layers panel with PixelStreams from an existing project. 
	 * @param pixelStreams PixelStreams.
	 */
	public void setPixelStreams(ArrayList<PixelStream> pixelStreams){

		for(PixelStream pixelStream : pixelStreams){
			addLayer(pixelStream);
		}
	}
	
	/**
	 * Constructs and adds a LayerPanel, from given PixelStream.
	 * @param pixelStream
	 */
	public void addLayer(PixelStream pixelStream){
		
		LayerPanel layerPanel = new LayerPanel(this, pixelStream, layerIdentifier);
		layerIdentifier++;
		
		layerPanel.setPreferredSize(layerDim);
		layerPanel.setRelativeLength(numberOfPixels, layerDim.width);
		layerPanel.addMouseListener(layerPanelListener);
		createMenuBar(layerPanel);
		
		layerWrapper.add(createLayerInfoPanel(layerPanel));
		layerWrapper.add(layerPanel);
		
		layerWrapperDim.height += layerHeight;
		
		layers.add(layerPanel);
		
		if (layers.size() == 1)
			clickLayer(layerPanel);
		
		revalidate();
	}
	
	/**
	 * adds a layer to the wrapperpanel by a given LayerPanel
	 * Used when repopulating after removal
	 * @param layer
	 */
	private void addLayer(LayerPanel layer) {
		layerWrapper.add(createLayerInfoPanel(layer));
		layerWrapper.add(layer);
		
		layerWrapperDim.height += layerHeight;
		
		revalidate();
	}

	/**
	 * Constructs and returns a layer info panel with a PixelStream's title.
	 * @return panel info panel.
	 */
	private JPanel createLayerInfoPanel(LayerPanel panel){
		
		JPanel layerInfo = new JPanel();
		layerInfo.setPreferredSize(layerInfoDim);
		layerInfo.setBorder(Styling.RAISED_BEVEL);
		SpringLayout layout = new SpringLayout();
		layerInfo.setLayout(layout);
		
		PixelStream pixelStream = panel.getPixelStream();
		
		Insets i = layerInfo.getInsets();
		
		ImageIcon thumb = new ImageIcon(createThumbnail(layerHeight, 
														layerHeight - i.top,
														pixelStream.getWidth(), pixelStream.getHeight(), pixelStream.getPixels()));
		JLabel thumbLabel = new JLabel(thumb);
	
		layout.putConstraint(SpringLayout.WEST, thumbLabel, -i.left, SpringLayout.WEST, layerInfo);
		layout.putConstraint(SpringLayout.NORTH, thumbLabel, -i.top, SpringLayout.NORTH, layerInfo);
		
		layerInfo.add(thumbLabel);
		
		//Create hide and show icon		
		JButton hs = new JButton();
		if (panel.getPixelStream().isVisible())
			hs.setIcon(Styling.SHOW_ICON);
		else
			hs.setIcon(Styling.HIDE_ICON);
		
		hs.setPreferredSize(new Dimension(layerHeight, layerHeight));
		hs.setName(Integer.toString(panel.number));
		hs.setBorder(null);
		hs.setFocusPainted(false);
		hs.setContentAreaFilled(false);
		hs.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		hs.addActionListener(e -> showOrHide(hs));
		
		layout.putConstraint(SpringLayout.WEST, hs, 0, SpringLayout.EAST, thumbLabel);
		layout.putConstraint(SpringLayout.NORTH, hs, - (i.top), SpringLayout.NORTH, layerInfo);
		
		layerInfo.add(hs);
		addChangableTitle(hs, layerInfo, panel, layout);
		
		return layerInfo;
		
	}
	
	/**
	 * This method creates a thumbnail for the layer
	 * Creates a new scaled bufferedImage with the pixels from PixelStream
	 * @param scaledWidth
	 * @param scaledHeight
	 * @param imageWidth
	 * @param imageHeight
	 * @param pixels This is the pixels in the image, used to create the thumbnail
	 * @return the thumbnail Image
	 */
	private Image createThumbnail(int scaledWidth,
			int scaledHeight,
			int imageWidth,
			int imageHeight,
			int[] pixels){
		
		BufferedImage img = new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_ARGB);
		img.setRGB(0, 0, imageWidth, imageHeight, pixels, 0, imageWidth);
		
		return img.getScaledInstance(scaledWidth, scaledHeight, BufferedImage.SCALE_FAST);
	}
	
	/**
	 * This method adds a button and a textfield to the infopanel for the given layer.
	 * This makes it possible for the user to click the name and change it if he wishes. 
	 * @param layerInfo
	 * @param panel
	 */
	public void addChangableTitle(JButton button, JPanel layerInfo, LayerPanel panel, SpringLayout layout) {
		JButton title = new JButton(panel.getPixelStream().getTitle());
		title.setName(Integer.toString(panel.number));
		title.setFocusPainted(false);
		title.setBorder(null);
		title.setContentAreaFilled(false);
		title.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		
		JTextField nameField = new JTextField(panel.getPixelStream().getTitle());
		nameField.setVisible(false);
		nameField.addActionListener(e -> {
            title.setText(nameField.getText());
            panel.getPixelStream().setTitle(nameField.getText());
            nameField.setVisible(false);
            title.setVisible(true);
        });
		
		nameField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				title.setText(nameField.getText());
				panel.getPixelStream().setTitle(nameField.getText());
				nameField.setVisible(false);
				title.setVisible(true);
			}
		});
		
		title.addActionListener(arg0 -> {
            title.setVisible(false);
            nameField.setText(title.getText());
            nameField.setVisible(true);
            nameField.requestFocus();
            nameField.setSelectionStart(0);
            nameField.setSelectionEnd(nameField.getText().length());
        });
		
		layout.putConstraint(SpringLayout.WEST, title, 2, SpringLayout.EAST, button);      
		layout.putConstraint(SpringLayout.WEST, nameField, 2, SpringLayout.EAST, button);
		
		layout.putConstraint(SpringLayout.NORTH, title, 4, SpringLayout.NORTH, layerInfo);      
		layout.putConstraint(SpringLayout.NORTH, nameField, 1, SpringLayout.NORTH, layerInfo);

		layerInfo.add(title);
		layerInfo.add(nameField);
	}
	
	/**
	 * Show or hides a button by checking what icon is shown now
	 * @param button
	 */
	private void showOrHide(JButton button) {
		if (button.getIcon().equals(Styling.SHOW_ICON)){		
			button.setIcon(Styling.HIDE_ICON);
			hideOrShowLayer(Integer.parseInt(button.getName()), false);
		}
		else {		
			button.setIcon(Styling.SHOW_ICON);
			hideOrShowLayer(Integer.parseInt(button.getName()), true);
		}	
	}
	
	/**
	 * Gets a LayerPanel from a given number
	 * @param number
	 * @return
	 */
	private LayerPanel getLayer(int number) {
		for (LayerPanel p : layers) {
			if (p.number == number) {
				return p;
			}
		}
		System.out.println("no layer");
		return null;
	}
	
	/**
	 * Gets a layer by number and changes its pixelstream "toBeRendered" property
	 * @param number
	 * @param tof
	 */
	private void hideOrShowLayer(int number, boolean tof) {
		LayerPanel layer = getLayer(number);
		if (layer != null) {
			layer.getPixelStream().setVisible(tof);
		}

		workspace.updateImage();
	}

	/**
	 * Create a menu pop up used for right click
	 * adds the layers number to the item, enables us to easily identify the layer in the actionlistener
	 */
	private void createMenuBar(LayerPanel panel) {
		popUp = new JPopupMenu();

		JMenuItem item = new JMenuItem("Remove");
		JMenuItem item2 = new JMenuItem("About");
		
		item.setName(Integer.toString(panel.number));
		
		item.addActionListener(e -> {
            JMenuItem item1 = (JMenuItem) e.getSource();
            removeLayer(Integer.parseInt(item1.getName()));
        });
		
		item2.addActionListener(e -> new HelpPopup(HelpContent.getLayerDescription()).popup(workspace));

		popUp.add(item);
		popUp.add(item2);

		panel.setComponentPopupMenu(popUp);
	}
	
	/**
	 * Sets total number of pixels in Canvas. 
	 * Used to calculate a LayerPanel's relative width. 
	 * Will recalculate if updated.
	 * @param numberOfPixels Number of pixels.
	 */
	public void setNumberOfPixels(int numberOfPixels){
		this.numberOfPixels = numberOfPixels;
		recalculateLayerPanelLengths();
	}
	
	/**
	 * Recalculates all LayerPanel relative sizes.
	 */
	private void recalculateLayerPanelLengths(){
		for(LayerPanel lP : layers){
			lP.setRelativeLength(numberOfPixels, layerDim.width);
		}
	}
	
	/**
	 * Repopulates the layers after a layer has been removed
	 */
	private void repopulate() {
		layerWrapperDim.height = 0;
		layerWrapper.removeAll();
		
		for (LayerPanel layer : layers) {
			addLayer(layer);
		}
		
		repaint();
	}
	
	/**
	 * Removes a layer
	 * @param number
	 */
	private void removeLayer(int number) {		
		LayerPanel removeLayer = getLayer(number);
		
		layers.remove(removeLayer);
		if (removeLayer != null) {
			workspace.removeLayer(removeLayer.getPixelStream());
		}

		repopulate();
	}
	
	/**
	 * Clicks a layer
	 * Handles that only one layer can be clicked at a time
	 * @param layer
	 */
	private void clickLayer(LayerPanel layer) {
		layer.clicked(true);

		if (!layer.isClicked())
			workspace.populateProperties(null);
		else
			workspace.populateProperties(layer.getPixelStream());
		
		for (LayerPanel panel : layers) {
			if (panel.number != layer.number && panel.isClicked())
				panel.clicked(false);
		}	
	}
	
	/**
	 * @return the PixelStream of the selected layer
	 */
	public PixelStream getSelectedPixelstream() {
		for (LayerPanel panel : layers) {
			if (panel.isClicked())
				return panel.getPixelStream();
		}
		return null;
	}
	
	/**
	 * @return the currently selected layer
	 */
	public LayerPanel getSelectedLayer() {
		for (LayerPanel panel : layers) {
			if (panel.isClicked())
				return panel;
		}
		return null;
	}
	
	/**
	 * Selects the next layer by direction (i) 1 = go down, 0 = go up
	 * used when the user is selecting with the arrow keys. 
	 * @param i
	 */
	public void layerSelect(int i) {
		int index;
		if (i > 0) {
			index = layers.indexOf(getSelectedLayer());
			index++;
			
			if (index >= layers.size())
				index = 0;
		} else {
			index = layers.indexOf(getSelectedLayer());
			index--;
			
			if (index < 0)
				index = layers.size() - 1;
		}
		
		clickLayer(layers.get(index));

	}
	
	/**
	 * Removes the selections from all the other layers than the one that has selections
	 * @param number
	 */
	public void removeSelections(int number) {
		for (LayerPanel panel : layers) {
			if (panel.number != number) {
				panel.removeSelection();
			}
		}
	}
	
	/**
	 * Listener used to click a certain layer
	 * @author Rasmus
	 *
	 */
	public class LayerPanelListener extends MouseAdapter{
		
		public void mouseReleased(MouseEvent e) {
			LayerPanel layer = (LayerPanel) e.getSource();

			if (SwingUtilities.isLeftMouseButton(e))
				clickLayer(layer);
			
		}
		
	}
}
