package glitchy.core;

import glitchy.core.imageProcessing.PixelStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used as a datastore for saving and loading projects.
 * 
 * @author Mikkel, Rasmus and Aksel
 */
public class Project implements Serializable{
	private static final long serialVersionUID = -3749481032197240478L;
	
	/**
	 * A project's pixelstreams.
	 */
	private ArrayList<PixelStream> pixelStreams;
	
	/**
	 * A project's action history. Refer to the ActionHistory class for more info.  
	 */
	private String[] actions;
	
	/**
	 * Canvas width. 
	 */
	private int w;
	
	/**
	 * Canvas height.
	 */
	private int h;
	
	/**
	 * Selected render type. Refer to RenderTypes class for more info.
	 */
	private int renderType;

	/**
	 * Constructs a Project, that can be serialized into a file.
	 * @param pixelStreams A project's pixelstreams, its loaded images.
	 * @param actions A project's action history.
	 * @param w Canvas width.
	 * @param h Canvas height.
	 * @param renderType Render type.
	 */
	public Project(ArrayList<PixelStream> pixelStreams, String[] actions, int w, int h, int renderType) {
		this.pixelStreams = pixelStreams;
		this.w = w;
		this.h = h;
		this.renderType = renderType;
		this.actions = actions;
	}
	
	/**
	 * Gets action history.
	 * @return action history
	 */
	public String[] getActions() {
		return actions;
	}

	/**
	 * Gets pixelstreams.
	 * @return pixelstreams
	 */
	public ArrayList<PixelStream> getPixelStreams() {
		return this.pixelStreams;
	}
	
	/**
	 * Gets Canvas width.
	 * @return width
	 */
	public int getWidth() {
		return w;
	}
	
	/**
	 * Gets Canvas height.
	 * @return height
	 */
	public int getHeight() {
		return h;
	}
	
	/**
	 * Gets selected RenderType.
	 * @return RenderType
	 */
	public int getRenderType() {
		return renderType;
	}
}