package glitchy.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * This class handles the events of the menu items
 * @author Rasmus
 *
 */
public class MenuItemListener implements ActionListener {
	
	/**
	 * This is a reference to the GuiController
	 */
	private GuiController gui;
	
	/**
	 * Creates the MenuItemListener
	 * @param gui
	 */
	public MenuItemListener(GuiController gui) {
		this.gui = gui;
	}
	
	@Override
	/**
	 * Only one MenuItemListener exist. Therefore this method differentiates between the items by casting the source to a component and getting the name. 
	 */
	public void actionPerformed(ActionEvent e) {
		Component c = (Component) e.getSource();
		String item = c.getName().toLowerCase();

		switch (item) {
		
		case "new project":
			gui.newProject();
			break;
		
		case "open project":
			gui.loadProject();
			break;
			
		case "save project":
			gui.saveProject();
			break;

		case "import image":
			gui.loadImage();
			break;
			
		case "export image":
			gui.saveImage();
			break;
			
		case "import raw":
			gui.requestImportRawPopup();
			break;

		case "exit":
			gui.exit();
			break;
			
		case "zoom in":
			gui.zoom(-1);
			break;
			
		case "zoom out":
			gui.zoom(1);
			break;
			
		case "reset zoom":
			gui.zoom(0);
			break;
			
		case "invert": case "bitshift": case "smear": case "sort": case "shuffle": case "reorder":
			gui.requestPopup(item);
			break;
		case "about glitchy":
			gui.requestAboutPopup();
			break;
			
		case "show action history":
			gui.showHistory();
			break;
			
		case "dock left":
			gui.dockPanel("left");
			break;
			
		case "dock right":
			gui.dockPanel("right");
			break;
			
		case "undo":
			gui.undoAction();
			break;
			
		case "duplicate selection into new layer":
			gui.copySelection();
			break;
			
		case "extract selection into new layer":
			gui.cutSelection();
			break;

		}
	}
	
}
