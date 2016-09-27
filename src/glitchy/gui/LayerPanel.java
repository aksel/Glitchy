package glitchy.gui;

import glitchy.core.imageProcessing.PixelStream;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.JPanel;

/**
 * Paints a PixelStream into a JPanel, based on its size and starting position in mind.
 * @author Aksel, Rasmus
 *
 */
public class LayerPanel extends JPanel implements Serializable{
	private static final long serialVersionUID = -6130106613427096855L;

	/**
	 * A reference to the pixelstream contained in this layer
	 */
	private PixelStream pixelStream;
	
	/**
	 * The starting position of the PixelStream. 
	 * Relative to number of pixels in Canvas and the width of the Layers panel.
	 */
	private double relPos;
	
	/**
	 * Distance between each pixel that is to be drawn. 
	 * Calculated as number of pixels, divided by width.
	 */
	private double step;
	
	/**
	 * Boolean specifying if layer is clicked
	 */
	private boolean clicked;
	
	/**
	 * This layers identifier
	 */
	public int number;

	/**
	 * hStart and hEnd is for the highlighted box. clickedX and releaseX used to calculate the actual pixelstart and end for the pixelstream
	 */
	private int hStart = 0, hEnd = 0, clickedX, releaseX;
	
	/**
	 * Reference to layers, used to remove selections from other layers when selecting
	 */
	private Layers layers;
	
	/**
	 * sets the fields and border - also applies the listener used for selecting a part of the layer
	 * @param layers
	 * @param pixelStream
	 * @param number
	 */
	public LayerPanel(Layers layers, PixelStream pixelStream, int number){
		this.number = number;
		this.pixelStream = pixelStream;
		this.layers = layers;
		
		setBorder(Styling.RAISED_BEVEL);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		LayerPanelListener listener = new LayerPanelListener();
		addMouseListener(listener);
		addMouseMotionListener(listener);
	}
	
	/**
	 * Sets the relative size of the layer, based on canvas size.
	 * @param numberOfPixels Number of pixels in canvas.
	 * @param width Width.
	 */
	public void setRelativeLength(int numberOfPixels, int width){
		relPos         = width * ((double)pixelStream.getPos() / numberOfPixels);
		step 		   = (double)numberOfPixels / width;
	}
	
	/**
	 * Methods used to click and unclick this layer
	 * @param tof - true or false
	 */
	public void clicked(boolean tof){
		clicked = tof;
		
		if (clicked)
			setBorder(Styling.LOWERED_BEVEL);
		else
			setBorder(Styling.RAISED_BEVEL);

		repaint();
	}
	
	/**
	 * @return a boolean indicating whether the layer is clicked
	 */
	public boolean isClicked() {
		return clicked;
	}
	
	/**
	 * @return the pixelstream of this layer
	 */
	public PixelStream getPixelStream() {
		return pixelStream;
	}
	
	/**
	 * Used to set the start and end position of the current selection on the layer
	 * also removes any other selections on different layers with the layers.removeSelections method
	 */
	private void setPosition() {
		//PixelStream class will handle it if selection is less than 0 or greater than actual pixelcount
		// - since the mouseDragged Event will keep increase/decrease x even if no longer inside layer.
		int start = clickedX > releaseX ? releaseX:clickedX;
		int end = start == clickedX ? releaseX:clickedX;

		if (start == end) {
			start = -1;
			end = -1;			
		} else {	
			start = (int) (step * (start - relPos));
			if (start < 0)
				start = 0;

			if (!clicked) {
				clicked = true;
				setBorder(Styling.LOWERED_BEVEL);
			}
		}
		
		pixelStream.setSelectionS(start);
		pixelStream.setSelectionE((int) (step * (end - relPos)));
		
		layers.removeSelections(number);
	}
	
	/**
	 * removes the current selection on this panel (if any)
	 */
	public void removeSelection() {
		hStart = 0;
		hEnd = 0;
		releaseX = 0;
		clickedX = 0;
		
		pixelStream.setSelectionS(-1);
		pixelStream.setSelectionE(-1);
		
		clicked = false;
		setBorder(Styling.RAISED_BEVEL);
		
		repaint();
	}
	
	@Override
	/**
	 * Handles the drawing of pixels of this layers pixelstream,
	 * also the drawing of the indication if this layer is clicked and the drawing of the selection box if any.
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		//Pixel Index.
		double pI = 0;
		
		int w = getWidth();
		int h = getHeight();
		
		for(int x = (int)relPos; x < w && pixelStream.hasIndex((int)pI); x++){
			g2.setColor(pixelStream.getPixelColor((int)pI));
			g2.drawLine(x, 0, x, h);
			
			pI += step;
		}
		
		if (clicked) {
			g2.setColor(Styling.LAYER_DARKENING);
			g2.fillRect(0, 0, w, h);
		}
		
		// Selection highlight
		g2.setColor(Styling.LAYER_HIGHLIGHT);		
		g2.fillRect(hStart, 0, hEnd, h);
	}
	
	/**
	 * Listener used for making selections on this layer
	 * @author Rasmus
	 *
	 */
	private class LayerPanelListener extends MouseAdapter {
		
		private boolean selecting;
		
		/**
		 * Sets the flag that tells the dragging method that a selection is being made
		 * also intialises all the appropriate fields used for selecting
		 * sets the cursor to a text cursor indicating that the user can select it like text
		 */
		public void mousePressed(MouseEvent e) {
			selecting = true;
			
			clickedX = e.getX();
			
			hStart = 0;
			hEnd = 0;
			releaseX = 0;
			
			setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		}
		
		/**
		 * Selecting is now false, this tells the dragging method to stop setting the selecting box bounds, that draws the blue highlight
		 * Also sets the position of the current selection with setPosition()
		 */
		public void mouseReleased(MouseEvent e) {
			selecting = false;
			
			releaseX = e.getX();
			
			setPosition();
			
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		
		/**
		 * This methods constantly updates and repaints the selection box used to indicate to the user what he has selected on the stream
		 */
		public void mouseDragged(MouseEvent e) {
			if (selecting) {				
				releaseX = e.getX();
				
				hStart = releaseX > clickedX ? clickedX:releaseX;
				hEnd = hStart == clickedX ? releaseX - clickedX:clickedX - releaseX;
				
				repaint();
			}
		}	
	}
}
