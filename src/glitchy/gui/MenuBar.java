package glitchy.gui;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * Handles the gui and events of the items
 * @author Rasmus and Mikkel
 *
 */

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar{
	
	private JMenu file, effects, help, edit, window;
	
	/**
	 * These array contains all the different menu items. With these names the methods below, and the listener, switches between actions. 
	 */
	private String[] fileItems = {"New Project", "Open Project", "Save Project", "Import RAW", "Import Image", "Export Image", "Exit"};
	//"Save as"
	private String[] effectsItems = {"Bitshift","Invert","Smear","Sort","Shuffle","Reorder"};
	//"Echo", "Phaser", "High/Low Pass"
	private String[] helpItems = {"About Glitchy"};
	//"Help content"
	private String[] editItems = {"Undo","Duplicate selection into new layer","Extract selection into new layer", "Zoom In", "Zoom Out", "Reset Zoom"};
	//"Render", "Render with...", "Remove Image"
	private String[] windowItems = {"Show action history", "Dock left", "Dock right"};
	
	/**
	 * The button needed to hold to activate a shortcut
	 * CTRL in windows, command on MAC
	 */
	private final int M_SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	
	/**
	 * The menu item listener reference added to each menuitem
	 */
	private MenuItemListener listener;
	
	/**
	 * Sets up the initial menu file headers
	 * and adds the menu items to each with the addItems() method
	 * @param listener
	 */
	public MenuBar(MenuItemListener listener) {
		this.listener = listener;
		
		file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		
		effects = new JMenu("Effects");		
		effects.setMnemonic(KeyEvent.VK_E);
		
		help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		
		edit = new JMenu("Edit");
		edit.setMnemonic(KeyEvent.VK_D);
		
		window = new JMenu("Window");
		window.setMnemonic(KeyEvent.VK_W);
		
		add(file);
		add(effects);
		add(edit);
		add(window);
		add(help);
		
		addItems();
		
	}
	
	/**
	 * Adds the different menu items to the JMenu by using the genericItemAdd() method.
	 */
	private void addItems() {
		
		genericItemAdd(fileItems, file);
		genericItemAdd(effectsItems, effects);
		genericItemAdd(helpItems, help);
		genericItemAdd(editItems, edit);
		genericItemAdd(windowItems, window);
		
	}
	
	/**
	 * Adds menu items to a JMenu by looping over all the names in the arrays above.
	 * @param items
	 * @param menu
	 */
	private void genericItemAdd(String[] items, JMenu menu) {

		for (String s : items) {
			JMenuItem item = new JMenuItem(s);
			item.setName(s);
			setHotKey(item, s);
			item.addActionListener(listener);
			menu.add(item);
		}
	}
	
	/**
	 * This method adds the hot key to the item, by switching between the names. 
	 * @param item
	 * @param s
	 */
	private void setHotKey(JMenuItem item, String s) {		
		s = s.toLowerCase();
		
		switch(s) {
		case "new project":
	        //shortcut CTRL+N
	        KeyStroke ctrlN = KeyStroke.getKeyStroke(KeyEvent.VK_N, M_SHORTCUT_MASK);
	        item.setAccelerator(ctrlN);
			break;
		
		case "open project":
	        //shortcut CTRL+O
	        KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, M_SHORTCUT_MASK);
	        item.setAccelerator(ctrlO);
			break;
			
		case "save project":
	        //shortcut CTRL+S
	        KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S, M_SHORTCUT_MASK);
	        item.setAccelerator(ctrlS);
			break;

		case "import image":
	        //shortcut CTRL+I
	        KeyStroke ctrlI = KeyStroke.getKeyStroke(KeyEvent.VK_I, M_SHORTCUT_MASK);
	        item.setAccelerator(ctrlI);
			break;
			
		case "export image":
	        //shortcut CTRL+E
	        KeyStroke ctrlE = KeyStroke.getKeyStroke(KeyEvent.VK_E, M_SHORTCUT_MASK);
	        item.setAccelerator(ctrlE);
			break;
			
		case "import raw":
			//shortcut CTRL+SHIFT+I
			KeyStroke ctrlShiftI = KeyStroke.getKeyStroke(KeyEvent.VK_I, M_SHORTCUT_MASK | InputEvent.SHIFT_DOWN_MASK);
			item.setAccelerator(ctrlShiftI);
			break;
				
		case "show action history":
			//shortcut CTRL+SHIFT+I
			KeyStroke ctrlH = KeyStroke.getKeyStroke(KeyEvent.VK_H, M_SHORTCUT_MASK);
			item.setAccelerator(ctrlH);
			break;
		
		case "undo":
			//shortcut CTRL+SHIFT+I
			KeyStroke ctrlZ = KeyStroke.getKeyStroke(KeyEvent.VK_Z, M_SHORTCUT_MASK);
			item.setAccelerator(ctrlZ);
			break;
			
		case "duplicate selection into new layer":
			//shortcut CTRL+SHIFT+I
			KeyStroke ctrlC = KeyStroke.getKeyStroke(KeyEvent.VK_C, M_SHORTCUT_MASK);
			item.setAccelerator(ctrlC);
			break;
			
		case "extract selection into new layer":
			//shortcut CTRL+SHIFT+I
			KeyStroke ctrlX = KeyStroke.getKeyStroke(KeyEvent.VK_X, M_SHORTCUT_MASK);
			item.setAccelerator(ctrlX);
			break;
			
		//Effects
			
		case "bitshift":
			KeyStroke ctrl1 = KeyStroke.getKeyStroke(KeyEvent.VK_1, M_SHORTCUT_MASK);
			item.setAccelerator(ctrl1);
			break;
			
		case "invert":
			KeyStroke ctrl2 = KeyStroke.getKeyStroke(KeyEvent.VK_2, M_SHORTCUT_MASK);
			item.setAccelerator(ctrl2);
			break;
			
		case "smear":
			KeyStroke ctrl3 = KeyStroke.getKeyStroke(KeyEvent.VK_3, M_SHORTCUT_MASK);
			item.setAccelerator(ctrl3);
			break;
			
		case "sort":
			KeyStroke ctrl4 = KeyStroke.getKeyStroke(KeyEvent.VK_4, M_SHORTCUT_MASK);
			item.setAccelerator(ctrl4);
			break;
			
		case "shuffle":
			KeyStroke ctrl5 = KeyStroke.getKeyStroke(KeyEvent.VK_5, M_SHORTCUT_MASK);
			item.setAccelerator(ctrl5);
			break;
			
		case "reorder":
			KeyStroke ctrl6 = KeyStroke.getKeyStroke(KeyEvent.VK_6, M_SHORTCUT_MASK);
			item.setAccelerator(ctrl6);
			break;
		}
	}

}
