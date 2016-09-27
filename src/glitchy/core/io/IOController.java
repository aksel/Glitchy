package glitchy.core.io;

import glitchy.core.Project;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;

/**
 * This class should be static and handles io events
 * @author Mikkel, Rasmus and Aksel
 */
public class IOController{
	
	/**
	 * Gets an image from the passed path
	 * @param path
	 * @return BufferedImage the image selected
	 */
	public BufferedImage getImage(String path) {	
		try {
			return ImageIO.read(new File(path));
		} catch (IOException e) {
			return null;
		}	
	}
	
	/**
	 * Exports a BufferedImage to an image file.
	 * @param image BufferedImage.
	 * @param imgPath Path for image location
	 * @param fileExtension FileExtension type for the exported image, retrieved from FileChooser
	 */
	public boolean exportImage(BufferedImage image , String imgPath, String fileExtension) {
		if (!imgPath.endsWith(fileExtension)) {
			imgPath += "." + fileExtension;
		}
		
		try {
			return ImageIO.write(image, fileExtension, new File(imgPath));
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * Passes a FileInputStream to a new RAWDataConverter. The file 
	 * is converted to an int[], which is then returned.
	 * @param path Path to file.
	 * @param alpha Whether image is to have alpha channel.
	 * @return File, converted to int[]
	 */
	public int[] readRaw(String path, boolean alpha) {
		FileInputStream fis = createFileInputStream(path);

		return new RAWDataConverter().readRaw(fis,alpha);
	}
	
	/**
	 * Creates a FileInputStream from given path 
	 * @param path the path selected
	 * @return FileInputStream
	 */
	public FileInputStream createFileInputStream(String path) {
		try {
			return new FileInputStream(path);
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Saves an instance of the project class to the harddrive.
	 * @param project Project class instance to be saved
	 * @param projectPath path of the project
	 */
	public void saveProject(Project project, String projectPath) {
		FileOutputStream fos;

		try {
			if (!projectPath.endsWith(".glp")) {
				projectPath += ".glp";
			}

			fos = new FileOutputStream(projectPath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(project);
			oos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads a serialized instance of the Project class.
	 * @param projectPath FilePath of the serialized file.
	 * @return Instance of the Project class.
	 */
	public Project loadProject(String projectPath) {
		try {
			FileInputStream fis = new FileInputStream(projectPath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Project result = (Project) ois.readObject();
			ois.close();

			return result;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
