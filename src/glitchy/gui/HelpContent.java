package glitchy.gui;

/**
 * This class contains all text used for help messages.
 * @author Mikkel
 *
 */
public class HelpContent {

	/**
	 * This method gets the requested help description by type
	 * @param type Help type
	 * @return a descriptive text
	 */
	public String getHelpDescription(String type){
		
		switch (type.toLowerCase()) {
		case "render":
			return this.getRenderDescription();
		case "dimension":
			return this.getDimensionDescription();
		case "rendertypes":
			return this.getRenderTypeDescriptions();
		case "smear":
			return this.getSmearDescription();
		case "invert":
			return this.getInvertDescription();
		case "bitshift":
			return this.getBitShiftDescription();
		case "sort":
			return this.getSortDescription();
		case "shuffle":
			return this.getShuffleDescription();
		case "reorder":
			return this.getReorderDescription();
		case "import raw":
			return this.getImportRawDescription();
		default: 
			return "";
		}
	}
	
	/**
	 * This method gets help for import raw
	 * @return String descriptive text
	 */
	public String getImportRawDescription(){
		return "<b>Import Raw:</b><br>"
			 + "Choose any file to import, and Glitchy will convert it to an image.<br>"
			 + "Large files will be capped at 100mb.";
	}
	
	/**
	 * This method gets canvas dimension help
	 * @return String descriptive text
	 */
	public String getDimensionDescription(){
		return "<b>Canvas width and height:</b><br>"
			 + "Changing the width or the height of the canvas, does not resize the image.<br>"
			 + "Instead it changes the amount of pixels that are rendered on the canvas.<br><br>"
			 + "In Glitchy, imported images are independent from the canvas width and height.<br>"
			 + "Images are broken down into a linear series of pixels, which is then \"painted\"<br>"
			 + "onto the canvas, line by line, beginning in the upper left corner of the canvas.";
	}
	
	/**
	 * This method gets the layer description
	 * @return String with descriptive text
	 */
	public static String getLayerDescription(){
		return "A layer is a linear representation of all pixels in an image loaded into Glitchy.";
	}
	
	/**
	 * This get help used for rendering
	 * @return String with descriptive text
	 */
	private String getRenderDescription(){
		return "<html>The options in this dropdown menu,"
				+ "<br>are the 4 different ways active layers can be rendered"
				+ "<br>together to form one image.</html>";
	}
	
	/**
	 * This method gets help for rendertypes
	 * @return String with descriptive text
	 */
	private String getRenderTypeDescriptions(){
		return 	"<b>Image Rendering:</b> When an image is rendered, every active layer is merged together,<br>"
			  + "pixel by pixel. This means, a pixel is merged with all pixels in the same position.<br>"
			  + "The render type determines how the layers are merged.<br><br>"
			  
			  + "<b>Sum:</b> Adds the pixels from each layer together. This can cause overflow,<br>"
			  + "meaning that if a channel's color value goes above 255, the remainder will be<br>"
			  + "carried over into the next color channel.<br><br>"
			  
			  + "<b>Bitwise OR:</b> Merges the pixels with the bitwise OR operation. This is similar to<br>"
			  + "a double exposure, in that colors and light take precedence over black.<br><br>"
			  
			  + "<b>Difference:</b> Merges the pixels with the bitwise XOR operation. This finds the difference<br>"
			  + "between the color channels of every pixel.<br><br>"
			  
			  + "<b>Average:</b> Finds the average between the pixels.<br>";
	}
	
	/**
	 * This method gets help for bit shifting
	 * @return String with descriptive text
	 */
	private String getBitShiftDescription(){
		return "This effect allows you to shift the pixels by the specified number of bits.<br>"
			 + "For example, shifting by 1 bit moves all bits one position to the right.<br>"
			 + "This will also cause a color channel's <b>least significant bit</b> to<br>"
			 + "become the <b>most significant bit</b> in the proceeding color channel.";
	}
	
	/**
	 * This method gets gets help used for invert
	 * @return String with descriptive text
	 */
	private String getInvertDescription(){
		return "Inverts the specified color channels.";
	}

	/**
	 * This method gets help used for reordering effect
	 * @return String with descriptive text
	 */
	private String getReorderDescription() {
		return "This effect allows you to reorder the color channels into any order you want.<br>"
			 + "Drag the boxes into the order you want.";
	}
	
	/**
	 * This method gets the shuffle description
	 * @return String with descriptive text
	 */
	private String getShuffleDescription() {
		return "You can choose between shuffling the pixels, or shuffling color channels independently.";
	}
	
	/**
	 * This method gets the help used for smear effect
	 * @return String with descriptive text
	 */
	private String getSmearDescription(){
		return "This effect smears pixels to the right. Basically, each pixel affects the<br>"
			 + "color of its proceeding pixels, with decreasing intensity, determined by<br>"
			 + "the specified length.<br>"
			 + "The intensity determines how much a pixel affects its proceeding pixels.";
	}
	
	/**
	 * This method gets the help used for Sort effect
	 * @return String with descriptive text
	 */
	private String getSortDescription() {
		return "Sorts from lowest value to highest value.<br>"
			 + "You can choose to sort by pixel values, or to sort channels independent of pixels";
	}
}
