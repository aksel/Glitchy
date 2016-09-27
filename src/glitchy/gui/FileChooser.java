package glitchy.gui;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 
 * This class handles all the filechoosing windows. Focus on reusability. 
 * 
 * @author Rasmus
 *
 */

public class FileChooser {
	
	/**
	 * This field is the current filechooser or the filechooser most recently used
	 * This is used to get the selectedFileFilter if the user has chosen one
	 */
	private JFileChooser currentChooser = new JFileChooser();
	
	/**
	 * @return The selected file filter
	 */
	public String getSelectedFilter() {
		if (currentChooser.getFileFilter() != null)
			return currentChooser.getFileFilter().getDescription();
		else {
			return "png";
		}
	}
	
	/**
	 * This method opens up a filechooser and returns the path selected
	 * @param type This is the type used to set up the filechooser with the right filters
	 * @param parent Parentcomponent of the filechooser
	 * @param path Filechooser opens up showing this directory
	 * @param button The text of the "ok" button
	 * @return The selected path
	 */
	public String getPath(String type, Component parent, String path, String button) {	
		JFileChooser chooser = getChooser(type, path);
		chooser.setDialogTitle(button);
		
		currentChooser = chooser;

		if (chooser.showDialog(parent, button) == JFileChooser.APPROVE_OPTION)
			return chooser.getSelectedFile().getAbsolutePath();
		
		return null;	
	}
	
	/**
	 * This private method is used by getPath() to get the appropiate chooser
	 * @param type
	 * @param path
	 * @return a JFileChooser
	 */
	private JFileChooser getChooser(String type, String path) {
		JFileChooser chooser = new JFileChooser();
		
		if (path != null)
			chooser.setCurrentDirectory(new File(path).getParentFile());
		
		FileFilter filter = null;
		String check = type.toLowerCase();
		
		switch (check) {
		
		case "image":
			filter = getImageFilter();
			chooser.setAcceptAllFileFilterUsed(false);
			break;
		case "project":
			filter = getProjectFilter();
			chooser.setAcceptAllFileFilterUsed(false);
			break;
		case "export":
			addImageFiltersExport(chooser);
			chooser.setAcceptAllFileFilterUsed(false);		
		case "importraw":
			chooser.setAcceptAllFileFilterUsed(true);
			break;
		}
		
		chooser.setFileFilter(filter);

		return chooser;
	}
	
	/**
	 * Image filters used for export, allows the user to pick one
	 * @param chooser
	 */
	private static void addImageFiltersExport(JFileChooser chooser) {
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("png", "png"));		
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("jpg", "jpg"));		
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("gif", "gif"));	
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("bmp", "bmp"));
	}
	
	/**
	 * Image filters used for import, it will only show "Image files"
	 * @return a filefilter
	 */
	private FileFilter getImageFilter() {
		FileFilter filter = new FileNameExtensionFilter
				("Image files",
				"png", "jpg", "jpeg", "bmp", "gif");
	
		return filter;
	}
	
	/**
	 * Image filters used for projects, works with .glp files 
	 * shows "Project files"
	 * @return a filefilter
	 */
	private FileFilter getProjectFilter() {
		FileFilter filter = new FileNameExtensionFilter
				("Project files",
				"glp");
		
		return filter;
	}
	
}
