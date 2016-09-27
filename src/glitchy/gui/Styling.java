package glitchy.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * This class contains all the static Gui elements.
 * Such as image resources, borders and colors.
 * @author Rasmus
 *
 */
public class Styling {
	
	/**
	 * The title of the program
	 */
	public static String TITLE = "Glitchy";

	/**
	 * This computers screen dimension
	 */
	public static Dimension SCREEN = Toolkit.getDefaultToolkit().getScreenSize();
	/**
	 * The dimension of popups
	 */
	public static Dimension POPUP = new Dimension(500,400);
	
	/**
	 * The LookAndFeel used
	 */
	public static String LAF = UIManager.getSystemLookAndFeelClassName();
	
	/**
	 * Layout with elements starting on the left with no gap
	 */
	public static FlowLayout FLOW_NOGAP_LEFT = new FlowLayout(FlowLayout.LEFT,0,0);
	/**
	 * Layout with elements starting on the left with a 5 pixel gap
	 */
	public static FlowLayout FLOW_SMALLGAP_LEFT = new FlowLayout(FlowLayout.LEFT, 5, 5);
	
	/**
	 * Icon of the program
	 */
	public static BufferedImage ICON;
	/**
	 * The canvas texture background, seen when image has trasparency
	 * or when canvas has dimension but no image
	 */
	public static BufferedImage CANVAS_BACKGROUND;
	/**
	 * The icon that indicates that a layer is shown
	 */
	public static ImageIcon SHOW_ICON;
	/**
	 * The icon that indicates that a layer is hidden
	 */
	public static ImageIcon HIDE_ICON;
	/**
	 * Questionmark icon used in HelpButton
	 */
	public static ImageIcon QUESTIONMARK;
	/**
	 * Logo of the program and website
	 */
	public static ImageIcon LOGO;
	
	/**
	 * A border with an outer raised border and inner lowered border.
	 */
	public static final Border COMPOUND_BORDER = BorderFactory.createCompoundBorder(
			BorderFactory.createRaisedBevelBorder(),
			BorderFactory.createLoweredBevelBorder());
	
	/**
	 * A raised border
	 */
	public static final Border RAISED_BEVEL = BorderFactory.createRaisedBevelBorder();
	/**
	 * A lowered border
	 */
	public static final Border LOWERED_BEVEL = BorderFactory.createLoweredBevelBorder();
	/**
	 * An etched border 
	 */
	public static final Border ETCHED = BorderFactory.createEtchedBorder();
	
	/**
	 * Color used when layer is selected
	 * Darkens the layer
	 */
	public static final Color LAYER_DARKENING = new Color(0,0,0, 75);
	/**
	 * Color used when a selection has been made on the layer
	 * transparent blue
	 */
	public static final Color LAYER_HIGHLIGHT = new Color(0,0,255, 100);
	
	/**
	 * Initialises the image fields
	 */
	public static void initialize(){
		
		try {
			ICON = ImageIO.read(new File(("res/icon.png")));
			CANVAS_BACKGROUND = ImageIO.read(new File(("res/canvasBackground.png")));
			SHOW_ICON = new ImageIcon(ImageIO.read(new File(("res/showing.png"))));
			HIDE_ICON = new ImageIcon(ImageIO.read(new File(("res/hidden.png"))));
			QUESTIONMARK = new ImageIcon(ImageIO.read(new File(("res/questionmark.png"))));
			LOGO = new ImageIcon(ImageIO.read(new File("res/logo.png")));
			} catch (IOException e) {
			
		}
	}
}
