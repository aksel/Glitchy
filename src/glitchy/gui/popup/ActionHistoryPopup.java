package glitchy.gui.popup;

import glitchy.gui.Styling;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
/**
 * The popup of the ActionHistory
 * @author Rasmus
 *
 */
public class ActionHistoryPopup extends Popup {

	/**
	 * The dimension of the popup
	 */
	private Dimension size;
	
	/**
	 * The textarea where the history is printed
	 */
	private JTextArea area;
	/**
	 * The height of the "History" Label
	 */
	private int labelHeight;
	
	/**
	 * Flag indicating whether the ActionHistory is hidden or not
	 */
	private boolean hidden = true;
	
	/**
	 * Construcs the ActionHistory popup based on the given metrics.
	 * @param metrics
	 */
	public ActionHistoryPopup(FontMetrics metrics) {

		setModal(false);
		setAlwaysOnTop(true);
		setFocusableWindowState(false);
		
		setTitle("History");
		
		addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent e) {
				
				hidden = true;
				
			}
			
		});
	
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        JLabel label = new JLabel("History");  
        labelHeight = label.getPreferredSize().height;
        
        mainPanel.add(label, BorderLayout.NORTH);
        
        JPanel historyPanel = new JPanel();      
        mainPanel.add(historyPanel, BorderLayout.CENTER);
        
		calculateDimension(metrics);
        
        setupTextarea(mainPanel);
        
        add(mainPanel);
        pack();
	}
	
	/**
	 * Calculates the metrics of the Popup based on FontMetrics. 
	 * @param metrics
	 */
	private void calculateDimension(FontMetrics metrics) {
		
		int width = metrics.stringWidth("MMMMMMMMMMMMMMMMMMMMMM") * 2;	
		int height = metrics.getHeight() * 14;
		
		size = new Dimension(width, height);
	}
	
	/**
	 * Sets up the history textarea in a scrollpane and adds it to the popup
	 * @param panel
	 */
	private void setupTextarea(JPanel panel) {	
		area = new JTextArea();	
		area.setEditable(false);
		area.setFont(panel.getFont());
		
	    JScrollPane scroll = new JScrollPane(area,
	    		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	    		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    
		scroll.setPreferredSize(size);
	    
		panel.add(scroll);
	}
	
	/**
	 * Shows the ActionHistory popup
	 * @param history
	 */
	public void showHistory(String[] history) {
		if (hidden) {
			area.setText("");
			
			for (String s : history) {
				if (s != null)
					area.append(s + "\n");
			}
			area.setCaretPosition(0);
			
			popup(null);
			
			Insets insets = getInsets();
			setLocation(Styling.SCREEN.width - size.width - insets.right, Styling.SCREEN.height - size.height - (insets.top + labelHeight));
			
			hidden = false;
		} else {
			setVisible(false);
			hidden = true;
		}
	}
	
	/**
	 * Updates the Actionhistory
	 * @param history
	 */
	public void updateHistory(String[] history) {
		area.setText("");
		
		for (String s : history) {
			if (s != null)
				area.append(s + "\n");
		}
		area.setCaretPosition(0);
	}
	
}
