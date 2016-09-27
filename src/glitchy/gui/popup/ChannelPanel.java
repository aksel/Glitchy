package glitchy.gui.popup;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
/**
 * This panel is used for the reordering effect
 * Draws colored boxes that can be dragged
 * @author Rasmus
 *
 */
@SuppressWarnings("serial")
public class ChannelPanel extends JPanel{
	
	/**
	 * flags indicating whether dragging is being done and whether current image has alpha
	 */
	private boolean dragging, hasAlpha;
	
	/**
	 * List of colored boxes
	 */
	private ChannelPanelBox[] boxList;
	/**
	 * The box currently being dragged
	 */
	private ChannelPanelBox moveBox;
	/**
	 * Colors for hightlight
	 */
	private Color highlight = new Color (255, 255, 255, 100), mouseHighlight = new Color(255, 255, 255, 50);
	
	/**
	 * Creates the boxes. Only creates alpha box if needed.
	 * @param hasAlpha
	 */
	public ChannelPanel(boolean hasAlpha) {
		this.hasAlpha = hasAlpha;
		
		int boxCount = hasAlpha ? 4:3;		
		
		boxList = new ChannelPanelBox[boxCount];
	
		for (int i = 0; i < boxCount; i++) {
			boxList[i] = new ChannelPanelBox(i, hasAlpha);
		}
		
		moveBox = new ChannelPanelBox(4, hasAlpha);
		
		setPreferredSize(new Dimension(50 * boxCount, 50));
		
		ChannelPanelListener listener = new ChannelPanelListener();
		addMouseListener(listener);
		addMouseMotionListener(listener);
		
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	public void paintComponent(Graphics g) {		
		Graphics2D g2 = (Graphics2D) g.create();
		
		for (ChannelPanelBox box : boxList) {
			g2.setColor(box.getColor());
			g2.fill(box.getBounds());
			if (box.doHighlight()) {
				g2.setColor(mouseHighlight);
				g2.fill(box.getBounds());
			}
		}
		
		g.setColor(highlight);
		g.fillRect(0, 0, 200, 25);
		
		if (dragging) {
			g2.setColor(moveBox.getColor());
			g2.fill(moveBox.getBounds());
		}
	}
	
	/**
	 * @return Int array - the order of the channels
	 */
	public int[] getIndexs() {
		int[] ret = new int[4];
		
		int index = hasAlpha ? 0 : 1;
		for (ChannelPanelBox box : boxList) {
			ret[index] = box.getChannel();
			index++;
		}
		
		if (!hasAlpha)
			ret[0] = 0;
		
		return ret;	
	}
	
	/**
	 * This listener class is used for dragging and changing the order of the boxes
	 * @author Rasmus
	 *
	 */
	private class ChannelPanelListener extends MouseAdapter {
		
		/**
		 * checks whether a highlight should be drawn
		 * @param p
		 */
		private void checkHighlight(Point p) {
			for (ChannelPanelBox box : boxList) {
				if (box.contains(p))
					box.highlight(true);
				else
					box.highlight(false);
			}
		}
		
		public void mouseMoved(MouseEvent e) {
			checkHighlight(e.getPoint());
			repaint();
		}
		
		public void mousePressed(MouseEvent e) {			
			moveBox.getBounds().x = e.getX();
			moveBox.getBounds().y = e.getY();
			
			for (ChannelPanelBox box : boxList) {
				if (box.contains(e.getPoint())) {
					moveBox.setColor(box.getColor());
					moveBox.setId(box.id());
					break;
				}
			}
			
			setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));	
			dragging = true;
		}
		
		public void mouseReleased(MouseEvent e) {	
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			dragging = false;
			
			Point mousePoint = e.getPoint();
			
			for (ChannelPanelBox box : boxList) {
				if (box.contains(mousePoint)) {
					boxList[moveBox.id()].setColor(box.getColor());
					box.setColor(moveBox.getColor());	
					break;
				}
			}
			
			checkHighlight(mousePoint);
			repaint();
		}
		
		public void mouseDragged(MouseEvent e) {
			if (dragging) {
				moveBox.getBounds().x = e.getX();
				moveBox.getBounds().y = e.getY();
				
				repaint();
			}
		}	
	}	
}
