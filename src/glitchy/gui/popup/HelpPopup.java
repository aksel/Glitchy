package glitchy.gui.popup;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * This is the popup used for the help popup
 * @author Rasmus and Aksel
 */
@SuppressWarnings("serial")
public class HelpPopup extends Popup{
	
	/**
	 * Construcs the HelpPopup
	 * Adds an ok button that calls the closeAction() method
	 * @param additionalInfo
	 */
	public HelpPopup(String additionalInfo) {
		super();
		
		setTitle("Help");

		setAlwaysOnTop(true);
		
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				closeAction();
			}
			
		});
		
		JPanel mainPanel = new JPanel();
		JLabel label = new JLabel("<html>" + additionalInfo + "</html>");
		
		mainPanel.add(label);
		mainPanel.add(ok);
		
		add(mainPanel,BorderLayout.CENTER);
		pack();
	}
	
	/**
	 * Closes the popup
	 */
	private void closeAction() {
		close();
	}
	
}
