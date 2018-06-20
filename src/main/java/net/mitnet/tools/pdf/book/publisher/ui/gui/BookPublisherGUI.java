/*
    Copyright (C) 2010 to present by Tim Telcik <telcik@gmail.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.mitnet.tools.pdf.book.publisher.ui.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

import net.mitnet.tools.pdf.book.publisher.BookPublisher;
import net.mitnet.tools.pdf.book.ui.gui.ProgressBarMonitor;
import net.mitnet.tools.pdf.book.util.ProgressMonitor;



/**
 * Book Publisher GUI.
 * 
 * This program publishes a bundle of OpenOffice documents (*.odt) 
 * into a single 2-up PDF document.
 * 
 * @author Rich Sezov <rich.sezov@liferay.com>
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see com.lowagie.toolbox.plugins.Handouts
 * @see com.lowagie.toolbox.plugins.NUp
 * @see BookPublisher
 * 
 * TODO - refactor GUI widget layout
 * TODO - use temp folder for output dir, allow user to browser output book file
 */
public class BookPublisherGUI extends BaseBookPublisherGUI {

	private static final String FRAME_TITLE = "PDF Book Publisher";
	
	private JFrame frame;
	private JProgressBar progressBar;
	private JTextArea statusMessageTextArea;
	private JTextField inputDirField;
	private JTextField outputDirField;


	/**
	 * Default constructor.
	 */
	public BookPublisherGUI() {
		super();
		initComponents();
	}
	
	
	public void setVisible( boolean visible ) {
		this.frame.setVisible( visible );
	}
	
	public void show() {
		this.frame.setVisible( true );
	}
	
	public void hide() {
		this.frame.setVisible( false );
	}
	
	

	/**
	 * Initialise widgets.
	 */
	private void initComponents() {

		this.frame = new JFrame();
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setTitle(FRAME_TITLE);

        
    	JPanel docControlPanel = new JPanel();
        docControlPanel.setBorder(BorderFactory.createTitledBorder("Documents"));

        
    	JLabel inputDirLabel = new JLabel();
        inputDirLabel.setText("Input Folder:");
        inputDirField = new JTextField();
        inputDirField.setEditable(true);
        inputDirField.setDragEnabled(true);
    	JButton browseInputDirButton = new JButton();
        browseInputDirButton.setText("Browse ...");
        browseInputDirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                browseInputDirButtonActionHandler(event);
            }
        });

        
    	JLabel outputDirLabel = new JLabel();
        outputDirField = new JTextField();
        outputDirField.setEditable(true);
        outputDirField.setDragEnabled(true);
        outputDirLabel.setText("Output Folder:");
    	JButton browseOutputDirButton = new JButton();
        browseOutputDirButton.setText("Browse ...");
        browseOutputDirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                browseOutputDirButtonActionHandler(event);
            }
        });

        
        GroupLayout docControlPanelLayout = new GroupLayout(docControlPanel);
        docControlPanel.setLayout(docControlPanelLayout);
        docControlPanelLayout.setHorizontalGroup(
            docControlPanelLayout.createParallelGroup(GroupLayout.LEADING)
            .add(docControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(docControlPanelLayout.createParallelGroup(GroupLayout.LEADING)
                    .add(docControlPanelLayout.createSequentialGroup()
                        .add(inputDirLabel)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(inputDirField, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE))
                    .add(docControlPanelLayout.createSequentialGroup()
                        .add(outputDirLabel)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(outputDirField, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE))
                    .add(GroupLayout.TRAILING, browseInputDirButton)
                    .add(GroupLayout.TRAILING, browseOutputDirButton))
                .addContainerGap())
        );
        docControlPanelLayout.setVerticalGroup(
            docControlPanelLayout.createParallelGroup(GroupLayout.LEADING)
            .add(docControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(docControlPanelLayout.createParallelGroup(GroupLayout.BASELINE)
                    .add(inputDirLabel)
                    .add(inputDirField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(browseInputDirButton)
                .add(14, 14, 14)
                .add(docControlPanelLayout.createParallelGroup(GroupLayout.BASELINE)
                    .add(outputDirLabel)
                    .add(outputDirField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(browseOutputDirButton)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        
    	JButton publishButton = new JButton();
        publishButton.setText("Publish");
        publishButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                publishButtonActionHandler(event);
            }
        });


    	JButton exitButton = new JButton();
        exitButton.setText("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                exitButtonActionHandler(event);
            }
        });

        
        statusMessageTextArea = new JTextArea();
        statusMessageTextArea.setSize(40, 10);
        String statusMessageText = "Click " + publishButton.getText() + " to start.";
        setStatusMessage(statusMessageText);
        
        
        progressBar = new JProgressBar();
        

        GroupLayout layout = new GroupLayout(this.frame.getContentPane());
        this.frame.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(37, 37, 37)
                .add(layout.createParallelGroup(GroupLayout.TRAILING)
                    .add(docControlPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .add(exitButton))
                .addContainerGap(33, Short.MAX_VALUE))
            .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(140, Short.MAX_VALUE)
                .add(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .add(118, 118, 118))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(statusMessageTextArea, GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .add(159, 159, 159)
                .add(publishButton)
                .addContainerGap(164, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(22, 22, 22)
                .add(docControlPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(publishButton)
                .add(18, 18, 18)
                .add(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(statusMessageTextArea)
                .add(43, 43, 43)
                .add(exitButton)
                .addContainerGap())
        );

        this.frame.pack();
    }

	
	private void browseInputDirButtonActionHandler(ActionEvent event) {
		browseDir(inputDirField);
	}
	
	
	private void browseOutputDirButtonActionHandler(ActionEvent event) {
		browseDir(outputDirField);
	}
	
	
	private void exitButtonActionHandler(ActionEvent event) {
		hide();
		System.exit(0);
	}

	
	private void publishButtonActionHandler(ActionEvent event) {
		publishBook();
	}
	
	
	public void setStatusMessage(String msg) {
		this.statusMessageTextArea.setText(msg);
	}

	
	public void updateProgressBar(int value) {
		this.progressBar.setValue(value);
	}
	
	
	public JProgressBar getProgressBar() {
		return this.progressBar;
	}
	
	
	public ProgressMonitor getProgressMonitor() {
		ProgressMonitor progressMonitor = new ProgressBarMonitor(getProgressBar());
		return progressMonitor;
	}

	
	public String getInputDirName() {
		String dirName = inputDirField.getText(); 
		if (dirName != null) {
			dirName = dirName.trim();
		}
		return dirName;
	}
	
	
	public String getOutputDirName() {
		String dirName = outputDirField.getText(); 
		if (dirName != null) {
			dirName = dirName.trim();
		}
		return dirName;
	}
	
	
	public static void main( String[] args ) {
		SwingUtilities.invokeLater( new GuiRunner() );
	}

	
	private static class GuiRunner implements Runnable {
		public void run() {
			BookPublisherGUI gui = new BookPublisherGUI();
			gui.show();
		}		
	}

}
