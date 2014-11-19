package metadataSecurity;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JPanel
                             implements ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static private final String newline = "\n";
    JButton splitButton, combineButton;
    JTextArea log;
    JFileChooser fc;

    public Main() {
        super(new BorderLayout());

        //Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        //Create a file chooser
        fc = new JFileChooser();

        //Uncomment one of the following lines to try a different
        //file selection mode.  The first allows just directories
        //to be selected (and, at least in the Java look and feel,
        //shown).  The second allows both files and directories
        //to be selected.  If you leave these lines commented out,
        //then the default mode (FILES_ONLY) will be used.
        //
        //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        //Create the split button.
        splitButton = new JButton("Split a File...");
        splitButton.addActionListener(this);

        //Create the combine button.
        combineButton = new JButton("Combine a File...");
        combineButton.addActionListener(this);

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(splitButton);
        buttonPanel.add(combineButton);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {

        //Handle split button action.
        if (e.getSource() == splitButton) {
            int returnVal = fc.showOpenDialog(Main.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                log.append("Splitting: " + file.getName() + "." + newline);
                try {
					Client client = new Client();
					client.sendFile(file);
					if (client.isFileSplited()){
						 log.append("File successfully splited!"+ newline);
					}
					else {
						 log.append("File splitting failed."+ newline);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            } else {
                log.append("Split command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());

        //Handle combine button action.
        } else if (e.getSource() == combineButton) {
        	try {
				Client client = new Client();
				client.getFile();
	            log.append("File combined." + newline);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            
            log.setCaretPosition(log.getDocument().getLength());
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("File Split/Combine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Add content to the window.
        frame.add(new Main());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE); 
                createAndShowGUI();
            }
        });
    }
}