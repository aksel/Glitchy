package glitchy.gui.popup;

import glitchy.gui.Styling;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * This popup contains general information about the program
 * @author Rasmus
 *
 */
public class AboutPopup extends Popup{
	private static final long serialVersionUID = -7707604195800342736L;
	
	/**
	 * Construcs the aboutpopup
	 */
	public AboutPopup(){
		setTitle("About Glitchy");
		Icon icon = Styling.LOGO;
        JLabel imageLabel = new JLabel("",JLabel.CENTER);
        imageLabel.setIcon(icon);
		imageLabel.setBorder(Styling.ETCHED);
        JPanel panel = new JPanel();
        panel.setBackground(new Color(40,42,46));
        panel.setLayout(new BorderLayout());
        add(imageLabel, BorderLayout.NORTH);
        
        
//        System.out.println(getBackground());
        
        String aboutText = "<html><b>Glitchy</b> is an image manipulator, made in 2015 as a finals project for the<br>"
						 + "AP Graduate in Computer Science, at the Copenhagen School of Design and Technology.<br><br>"
						 + "The purpose of this software is to give those who enjoy making glitch art a better<br>"
						 + "user experience, compared to working with software such as Audacity, while still<br>"
						 + "maintaining the idea of manipulating images on a byte-level.</html>";
        
        JLabel aboutLabel = new JLabel(aboutText,JLabel.CENTER);
        aboutLabel.setBorder(Styling.ETCHED);
        
        add(aboutLabel, BorderLayout.CENTER);
        
        pack();
		
        popup(null);
	}
}
