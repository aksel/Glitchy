package glitchy.gui.popup;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
/**
 * This class contains the funtionality of a single colored box in the ChannelPanel
 * @author Rasmus
 *
 */
public class ChannelPanelBox {
	
	/**
	 * The currently displayed color number and the id of this box
	 */
	private int colorNum, listId;
	
	/**
	 * The bounds of the box
	 */
	private Rectangle box;
	
	/**
	 * Flags indicating whether this image has alpha and whether to show a highlight (Highlight when mouseover box)
	 */
	private boolean hasAlpha, highlight;
	
	/**
	 * Sets up this box based on the colornum parameter
	 * @param colorNum
	 * @param hasAlpha
	 */
	public ChannelPanelBox(int colorNum, boolean hasAlpha) {
		this.colorNum = colorNum;
		this.hasAlpha = hasAlpha;
		
		switch(colorNum) {
		case 0:
			box = new Rectangle(0,0, 50, 50);
			break;
		case 1: 
			box = new Rectangle(50, 0, 50, 50);
			break;
		case 2: 
			box = new Rectangle(100, 0, 50, 50);
			break;
		case 3:
			box = new Rectangle(150, 0, 50, 50);
			break;
		case 4:
			box = new Rectangle(0, 0, 25, 25);
		}
		
		listId = colorNum;
	}
	
	/**
	 * Sets the color of this box
	 * @param c
	 */
	public void setColor(Color c) {
		if (c == Color.gray)
			colorNum = 0;
		else if (c == Color.red)
			colorNum = 1;
		else if (c == Color.green)
			colorNum = 2;
		else if (c == Color.blue)
			colorNum = 3;
		
		if (!hasAlpha)
			colorNum--;
	}
	
	/**
	 * Gets the color of this box
	 * @return
	 */
	public Color getColor() {
		
		int checkNumber = hasAlpha ? colorNum : colorNum + 1;
		
		switch(checkNumber) {
		case 0:
			return Color.gray;
		case 1: 
			return Color.red;
		case 2: 
			return Color.green;
		case 3:
			return Color.blue;
		default:
			return null;
		}
	}
	
	/**
	 * Sets whether the box should be hightlighted (Mouseover)
	 * @param highlight
	 */
	public void highlight(boolean highlight) {
		this.highlight = highlight;
	}
	
	/**
	 * @return boolean indicating whether hightlight should be drawn
	 */
	public boolean doHighlight() {
		return highlight;
	}
	
	/**
	 * Gets the channel of this box
	 * @return integer (Channel indicated in the form of the colornum field)
	 */
	public int getChannel() {
		return hasAlpha ? colorNum : colorNum + 1;
	}
	
	/**
	 * @return The id of this box
	 */
	public int id() {
		return listId;
	}
	
	/**
	 * Set the id of this box
	 * @param id
	 */
	public void setId(int id) {
		listId = id;
	}
	
	/**
	 * Checks whether this point (Mouse) is contained in the box
	 * @param p
	 * @return
	 */
	public boolean contains(Point p) {
		return box.contains(p);
	}
	
	/**
	 * Gets the bounds of this box
	 * @return Rectangle with the bounds
	 */
	public Rectangle getBounds() {
		return box;
	}
	
}
