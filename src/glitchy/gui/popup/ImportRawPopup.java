package glitchy.gui.popup;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import glitchy.gui.FileChooser;
import glitchy.gui.HelpContent;

/**
 * With this popup, the user can import raw files, and customize some parameters.
 * @author Aksel
 *
 */
@SuppressWarnings("serial")
public class ImportRawPopup extends Popup{

	/**
	 * Reference to the PopupController
	 */
	PopupController popupController;
	
	/**
	 * A FileChooser
	 */
	FileChooser fileChooser;

	/**
	 * Radiobutton, if selected file imported will be converted into an ARGB image.
	 */
	JRadioButton argb;
	
	/**
	 * Radiobutton, if selected file imported will be converted into an RGB image.
	 */
	JRadioButton rgb;
	
	/**
	 * Path to file to be imported.
	 */
	JTextField filePath;
	
	/**
	 * OK button, closes the popup and tells popupcontroller to begin import.
	 */
	JButton ok;
	
	/**
	 * Cancel button, closes the popup.
	 */
	JButton cancel;
	
	/**
	 * The last path that import raw dialog was on
	 */
	private String lastpath;
	
	/**
	 * Constructs the ImportRawPopup. 
	 * Takes in a filechooser to use for the path selection.
	 * @param popupController
	 * @param fileChooser
	 */
	public ImportRawPopup(PopupController popupController, FileChooser fileChooser, String lastpath){
		super();

		this.fileChooser = fileChooser;
		this.popupController = popupController;
		this.lastpath = lastpath;
		
		String description = "<html>";
		description += new HelpContent().getHelpDescription("import raw");
		description += "</html>"; 
		JLabel descriptionLabel = new JLabel(description);

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(createRadioPanel(),BorderLayout.NORTH);
		centerPanel.add(createFileChooserPanel(),BorderLayout.CENTER);

		add(descriptionLabel,BorderLayout.NORTH);
		add(centerPanel,BorderLayout.CENTER);		
		add(createButtonPanel(),BorderLayout.SOUTH);

		pack();
	}

	/**
	 * Creates and returns a JPanel, containing the OK and Cancel buttons.
	 * @return JPanel
	 */
	private JPanel createButtonPanel(){
		JPanel panel = new JPanel();

		ok = new JButton("OK");
		ok.setEnabled(false);
		ok.addActionListener(arg0 -> close());

		cancel = new JButton("Cancel");
		cancel.addActionListener(arg0 -> cancel());

		panel.add(ok);
		panel.add(cancel);

		return panel;
	}

	/**
	 * Creates and returns a JPanel containing the radiobuttons
	 * @return
	 */
	private JPanel createRadioPanel(){
		JPanel panel = new JPanel();

		panel.add(new JLabel("Transparency: "));

		ButtonGroup bGroup = new ButtonGroup();

		argb = new JRadioButton();
		argb.setText("Yes");
		argb.setSelected(true);
		argb.setFocusable(false);

		rgb = new JRadioButton();
		rgb.setText("No ");
		rgb.setFocusable(false);

		bGroup.add(argb);
		bGroup.add(rgb);

		panel.add(argb);
		panel.add(rgb);

		return panel;
	}

	/**
	 * Creates and returns a JPanel containing the filepath textfield, 
	 * and the file selection button.
	 * @return JPanel
	 */
	private JPanel createFileChooserPanel(){
		JPanel panel = new JPanel();

		JButton b = new JButton("Select file");
		b.addActionListener(arg0 -> chooseFile());

		filePath = new JTextField();
		filePath.setColumns(25);
		filePath.setEditable(false);
		panel.add(filePath);
		panel.add(b);
		return panel;
	}

	/**
	 * Opens the JFileChooser. If the user selects a file, the filepath textfield will 
	 * be set to the bath, and the ok button will be enabled.
	 */
	private void chooseFile() {
		String path = fileChooser.getPath("importraw", this, lastpath, "Import Raw");

		if(path!=null){
			filePath.setText(path);
			ok.setEnabled(true);
		}
	}

	/**
	 * Closes the popup, and tells the popupcontroller to begin importing raw.
	 */
	protected void close(){
		super.close();
		boolean alpha = argb.isSelected();
		popupController.importRaw(filePath.getText(),alpha);
	}

	/**
	 * Closes the popup.
	 */
	private void cancel(){
		super.close();
	}
}