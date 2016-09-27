package glitchy.gui;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * 
 * This class contains the image currently being worked on.
 * 
 * @author Rasmus, Aksel and Mikkel
 */
@SuppressWarnings("serial")
public class Canvas extends JPanel{

	/**
	 * The image currently being painted on the canvas
	 */
	private BufferedImage image;
	
	/**
	 * The canvas background. Seen when an image are transparent
	 * or when the canvas has a dimension whithout an image
	 */
	private TexturePaint canvasBackground;
	
	/**
	 * The reference to the workspace
	 */
	private Workspace workspace;
	
	/**
	 * The coordinates where the image will be rendered at.
	 */
	private double x,y;
	
	/**
	 * The image's current dimensions.
	 */
	private double width,height;
	
	/**
	 * The image's minimum dimensions.
	 */
	private double minWidth,minHeight;
	
	/**
	 * How many percentages the images scales by, for each scaling step.
	 */
	private double scale;
	
	/**
	 * Points, used for dragging and positioning the image.
	 */
	private Point draggedPos,clickedPos;

	/**
	 * The image's bounds.
	 */
	private Rectangle bounds = new Rectangle(0,0,0,0);

	
	/**
	 * The constructor of the canvas
	 * Sets up the canvasbackground
	 * and the fields and listeners for the zoom and drag
	 * ComponentListener that centers the image on resize of window
	 * @param workspace
	 */
	public Canvas(Workspace workspace) {	
		this.workspace = workspace;
		
		BufferedImage canvasBackgroundImage = Styling.CANVAS_BACKGROUND;
		Rectangle canvasBackgroundRect = new Rectangle(
				0,
				0,
				canvasBackgroundImage.getWidth(),
				canvasBackgroundImage.getHeight());
		
		canvasBackground = new TexturePaint(canvasBackgroundImage,
				canvasBackgroundRect
		);
		
		setBackground(getBackground().darker());

		draggedPos = new Point(0,0);
		clickedPos = new Point(0,0);
		
		scale = 0.1;
		
		CanvasListener listener = new CanvasListener();	
		addMouseListener(listener);
		addMouseWheelListener(listener);
		addMouseMotionListener(listener);
		
		addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent e) {
		        centerImage();       
		    }
		});
	}

	/**
	 * @return a boolean that indicates whether the canvas has an image
	 */
	public boolean hasImage() {
		return image == null ? false:true;
	}

	/**
	 * Sets image, and calculated the various coordinates.
	 * @param newImage
	 */
	public void setImage(BufferedImage newImage){
		if(hasImage() && 
				(image.getWidth() ==newImage.getWidth() &&
				 image.getHeight()==newImage.getHeight())){
			image = newImage;
		}
		
		else{
			image = newImage;
			width 			= newImage.getWidth();
			height 			= newImage.getHeight();
			checkSize();
			minWidth 		= width/2;
			minHeight 		= height/2;
			bounds.width 	= (int) width;
			bounds.height 	= (int) height;
			centerImage();
		}
	}
	
	/**
	 * This methods resets the zoom to the original width, height and center position
	 */
	private void resetZoom(){
		
		width 			= image.getWidth();
		height 			= image.getHeight();
		checkSize();
		centerImage();
		bounds.width 	= (int) width;
		bounds.height 	= (int) height;
		
		repaint();
	}
	
	/**
	 * Centers the image.
	 */
	private void centerImage(){
		
		x = (getWidth() -width)/2;
		y = (getHeight()-height)/2;
		
		draggedPos.x = (int)x;
		draggedPos.y = (int)y;
		clickedPos.x = (int)x;
		clickedPos.y = (int)y;
		bounds.x 	 = (int)x;
		bounds.y 	 = (int)y;
		
	}
	
	/**
	 * Zooms into the canvas center.
	 * @param dir
	 */
	public void zoom(int dir) {
		double centerX = getWidth()/2;
		double centerY = getHeight()/2;
		Point imageCenter = new Point();

		imageCenter.setLocation(centerX, centerY);
		zoom(dir,imageCenter);
	}

	/**
	 * Zooms in on the image
	 * @param dir Which direction the image is to be zoomed.
	 * @param point 
	 */
	public void zoom(int dir, Point point) {

		if(dir == 0){
			resetZoom();
		}
		
		else{
			if(dir < -1)
				dir = -1;
			
			else if(dir > 1)
				dir = 1;
			
			double scaledWidth =  width -(width  * scale) * dir;
			double scaledHeight = height-(height * scale) * dir;
			
			//If the image is too small to scale, then don't.
			if(scaledWidth<minWidth || scaledHeight<minHeight){
				return;
			}
			
			width  = scaledWidth;
			height = scaledHeight;
			
			double[] offsets = calculateOffets(point);
			double xOffset = offsets[0]*dir;
			double yOffset = offsets[1]*dir;
			
			x+=xOffset;
			y+=yOffset;
			clickedPos.x+=xOffset;
			clickedPos.y+=yOffset;
			
			bounds.x = (int) x;
			bounds.y = (int) y;
			bounds.width  = (int) width;
			bounds.height = (int) height;
			
			repaint();
		}
	}
	
	/**
	 * Calculates and returns the offset, that the image has to move, 
	 * so that it appears to zoom into the position (either image center, or the mouse position).
	 * @param point Mouse position.
	 * @return Offsets[x,y]
	 */
	private double[] calculateOffets(Point point){
		
		double[] offsets = new double[2];

		double xOffset = point.x-x;
		double yOffset = point.y-y;
		
		offsets[0] = xOffset*scale;
		offsets[1] = yOffset*scale;
		
		return offsets;
	}
	
	/**
	 * If the image is too large for the canvas, it will be resized to fit.
	 */
	private void checkSize() {
		
		int canvasWidth  = getWidth();
		int canvasHeight = getHeight();
		double difference;
		
		if(width > canvasWidth){
			difference = canvasWidth/width;
			width  *= difference;
			height *= difference;
		}
		
		if(height > canvasHeight){
			difference = canvasHeight/height;
			width  *= difference;
			height *= difference;
		}
	}
	
	/**
	 * Sets the image's coordinates, based on where it has been dragged to.
	 */
	private void setImageCoordinates(){
		x = draggedPos.x - clickedPos.x;
		y = draggedPos.y - clickedPos.y;
		bounds.x = (int) x;
		bounds.y = (int) y;
		
		repaint();
	}

	/**
	 * Resets the canvas
	 */
	public void reset() {	
		image = null;
		width =
		height =
		minWidth =
		minHeight = 
		x =
		y =
		draggedPos.x =
		draggedPos.y =
		clickedPos.x =
		clickedPos.y =
		bounds.x =
		bounds.y = 
		bounds.width = 
		bounds.height = 0;
	}

	/**
	 * This method draws the actual graphics on the canvas. 
	 * @param g The graphics object from JPanel used to draw the graphics
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setPaint(canvasBackground);
		g2.fillRect((int)x, (int)y, (int)width, (int)height);
		
		if(image!=null){
			g2.drawImage(image,
					(int)x,
					(int)y,
					(int)(x+width),
					(int)(y+height),
					0,
					0,
					image.getWidth(),
					image.getHeight(),
					null);
			
			/*Draws a line from the image center to the canvas center.
			int cX = getWidth()/2;
			int cY = getHeight()/2;
			
			int icX = (int) (x + width/2);
			int icY = (int) (y + height/2);
			
			g2.setColor(Color.WHITE);
			g2.drawLine(icX, icY, cX, cY);*/
		}
	}

	/**
	 * This inner class handles the zooming and dragging events
	 * @author Rasmus
	 */
	public class CanvasListener extends MouseAdapter{
		
		boolean dragging = false;
		
		public void mouseEntered(MouseEvent e) {
			workspace.windowFocus();
		}
		
		/**
		 * This metod sets the mouse position when dragging is occuring
		 * @param currentPos
		 */
		public void setDraggingPosition(Point currentPos){
			draggedPos.x = currentPos.x;
			draggedPos.y = currentPos.y;
		}
		
		/**
		 * This method sets the mouse position when mouse is clicked
		 * @param pos
		 */
		public void setClickedPosition(Point pos){
			clickedPos.x = pos.x - clickedPos.x;
			clickedPos.y = pos.y - clickedPos.y;
		}

		public void mousePressed(MouseEvent e) {
			if (bounds.contains(e.getPoint())){
				dragging = true;
				setClickedPosition(e.getPoint());
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (dragging){
				setClickedPosition(e.getPoint());
				dragging = false;
			}
		}

		public void mouseDragged(MouseEvent e) {
			if (dragging){
				setDraggingPosition(e.getPoint());
				setImageCoordinates();
			}
		}

		public void mouseMoved(MouseEvent e) {
			if (bounds.contains(e.getPoint()))
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			else
				setCursor(Cursor.getDefaultCursor());
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			//Can only zoom if CTRL is pressed, and the mouse is in bounds of the image.
			if(e.isControlDown()
					&& bounds.contains(e.getPoint())) {
				zoom(e.getWheelRotation(),e.getPoint());
			}
		}		
	}
}
