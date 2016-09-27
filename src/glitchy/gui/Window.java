package glitchy.gui;

import glitchy.core.imageProcessing.PixelStream;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

@SuppressWarnings("serial")
/**
 * This is the Window of the Program
 * Overrides and inherits the standard functionality of a JFrame
 * @author Rasmus and Aksel
 *
 */
public class Window extends JFrame{

	/**
	 * A reference to the workspace class
	 */
	private Workspace workspace;
	/**
	 * A reference to the GuiController
	 */
	private GuiController gui;

	/**
	 * A Loadingbar JDialog shown when program is loading
	 */
	private JDialog loadingDialog;
	
	/**
	 * Setting up the frame, panels and listeners
	 * @param gui
	 * @param listener
	 */
	public Window(GuiController gui, MenuItemListener listener) {		
		this.gui = gui;

		setLookAndFeel();
		setIconImage(Styling.ICON);

		setTitle(Styling.TITLE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setPreferredSize(Styling.SCREEN);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener(gui));

		setJMenuBar(new MenuBar(listener));

		addPanels(gui);

		pack();

		setVisible(true);

		requestFocus();
	}
	
	/**
	 * sets the look and feel for the program
	 */
	private void setLookAndFeel() {

		try
		{
			UIManager.setLookAndFeel(Styling.LAF);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	/**
	 * adds panels to the frame
	 * @param gui
	 */
	private void addPanels(GuiController gui) {	
		workspace = new Workspace(Styling.SCREEN, this);
		add(workspace);
	}
	
	public void updateEffects(String message) {
		gui.updateEffects(message);
	}

	/**
	 * sets the render options by the setRenderProperties in the guicontroller
	 * @param options
	 */
	public void renderOptions(String[] options) {
		gui.setRenderProperties(options[0], options[1], Integer.parseInt(options[2]));
	}
	
	/**
	 * Sets the renderoptions to show in the propertypanel
	 * @param width
	 * @param height
	 * @param pt
	 */
	public void setRenderingOptions(String width, String height, int pt) {
		workspace.setRenderingOptions(width, height, pt);
	}
	
	/**
	 * Sets an image in the canvas
	 * @param img
	 */
	public void setImage(BufferedImage img){
		workspace.setImage(img);
	}
	
	/**
	 * Clears the workspace and makes it ready for new project
	 */
	public void newProject() {
		workspace.clear();
	}
	
	/**
	 * Adds a layer in the layerpanel
	 * @param pixelStream
	 */
	public void addLayer(PixelStream pixelStream) {
		workspace.addLayer(pixelStream);
	}
	
	/**
	 * Method used to check whether a key is pressed. 
	 * @param keyCode
	 * @return Boolean
	 */
	public boolean isKeyPressed(int keyCode) {
		return gui.isKeyPressed(keyCode);
	}
	
	/**
	 * Zooms the image based on a direction
	 * @param dir
	 */
	public void zoom(int dir) {
		workspace.zoom(dir);
	}
	
	/**
	 * Adds the pixelstreams as layers
	 * @param pixelStreams
	 */
	public void setPixelStreams(ArrayList<PixelStream> pixelStreams) {
		workspace.setPixelStreams(pixelStreams);
	}
	
	/**
	 * Removes the layer with the given pixelstream
	 * @param pixelStream
	 */
	public void removeLayer(PixelStream pixelStream) {
		gui.removeLayer(pixelStream);
	}

	/**
	 * @return PixelStream - the currently selected layer
	 */
	public PixelStream getSelectedLayer() {
		return workspace.getSelectedLayer();
	}
	
	/**
	 * Requests the GuiController to update the image
	 */
	public void updateImage() {
		gui.updateImage();
	}
	
	/**
	 * Shows a loadingbar with the given message
	 * @param message
	 */
	public void startLoading(String message) {
		loadingDialog = new JDialog();
		loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		loadingDialog.setUndecorated(true);
		loadingDialog.setLayout(new BorderLayout());
		loadingDialog.setModalityType(ModalityType.APPLICATION_MODAL);

		JPanel contentPane = (JPanel) loadingDialog.getContentPane();
		contentPane.setBorder(Styling.RAISED_BEVEL);

		loadingDialog.add(new JLabel(message,
				JLabel.CENTER), BorderLayout.NORTH);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		loadingDialog.add(progressBar, BorderLayout.CENTER);

		loadingDialog.pack();		
		loadingDialog.setLocationRelativeTo(this);
		loadingDialog.setVisible(true);
	}
	
	/**
	 * Disposes of the loadingbar
	 */
	public void stopLoading() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(loadingDialog!=null){				
					loadingDialog.setVisible(false);
					loadingDialog.dispose();
				}
			}
		});
	}
	
	/**
	 * Docks the propertypanel based on a direction
	 * @param dir
	 */
	public void dockPanel(String dir) {
		workspace.dockPPanel(dir);
	}
	
	/**
	 * Returns a FontMetrics object
	 * @return
	 */
	public FontMetrics getMetrics() {
		return workspace.getMetrics();
	}
	
	/**
	 * Populates the propertypanel with the information from the given PixelStream
	 * @param pixelStream
	 */
	public void populateProperties(PixelStream pixelStream) {
		workspace.populateProperties(pixelStream);
	}
	
	/**
	 * Tells the gui to check an Integer value.
	 * @param number
	 * @return Boolean - false if not a number
	 */
	public boolean checkInt(String number) {
		return gui.checkInt(number);
	}
	
	/**
	 * Select a layer based on the integer parameter
	 * @param i
	 */
	public void layerSelect(int i) {
		workspace.layerSelect(i);
	}

	public void effectAction(String effect, PixelStream pixelStream) {
		gui.effectAction(effect, pixelStream);
	}
}
