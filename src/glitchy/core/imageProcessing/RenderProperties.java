package glitchy.core.imageProcessing;

/**
 * Contains properties used for image rendering.
 * @author Aksel and Rasmus
 *
 */
public class RenderProperties {

	/**
	 * SUM render type. Adds pixels together, by decimal value.
	 */
	public static final int SUM = 0;
	
	/**
	 * OR render type. Bitwise OR used to merge images.
	 */
	public static final int OR  = 1;

	/**
	 * DIFFERENCE render type. Bitwise XOR used to merge images.
	 */
	public static final int DIFFERENCE = 2;
	
	/**
	 * AVERAGE render type. Calculates average for each pixel position.
	 */
	public static final int AVERAGE = 3;
	
	/**
	 * Image width.
	 */
	public int w;
	
	/**
	 * Image height.
	 */
	public int h;
	
	/**
	 * Render type.
	 */
	public int renderType;
	
	/**
	 * Constructs new RenderProperties with given parameters.
	 * @param w Image width.
	 * @param h Image height.
	 * @param renderType Render type.
	 */
	public RenderProperties(int w, int h, int renderType){
		this.w = w;
		this.h = h;
		this.renderType = renderType;
	}
	
	/**
	 * Returns given render type as a String.
	 * @param type Render type.
	 * @return Render type as String.
	 */
	public String renderTypeToString(int type) {
		switch (type) {
			case 0:
				return "Sum";
			case 1: 
				return "OR";
			case 2:
				return "Difference";
			case 3:
				return "Average";
			default:
				return "";
		}
	}

	/**
	 * Returns set render type as a String.
	 * @return Render type as String.
	 */
	public String renderTypeToString() {
		return renderTypeToString(renderType);
	}
}