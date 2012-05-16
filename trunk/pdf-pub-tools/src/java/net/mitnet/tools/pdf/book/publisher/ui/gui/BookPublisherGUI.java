/*
    Copyright (C) 2010-2011  Tim Telcik <telcik@gmail.com>

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

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import net.mitnet.tools.pdf.book.io.FileExtensionConstants;
import net.mitnet.tools.pdf.book.openoffice.converter.OpenOfficeDocConverter;
import net.mitnet.tools.pdf.book.openoffice.net.OpenOfficeServerContext;
import net.mitnet.tools.pdf.book.publisher.BookPublisher;
import net.mitnet.tools.pdf.book.publisher.BookPublisherConfig;
import net.mitnet.tools.pdf.book.ui.gui.ProgressBarMonitor;
import net.mitnet.tools.pdf.book.util.ProgressMonitor;

import org.apache.commons.io.FilenameUtils;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

import com.lowagie.text.Rectangle;


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
 */
public class BookPublisherGUI extends JFrame {
	
	private static final long serialVersionUID = -7359553392594050434L;
	
	private JButton browseSourceButton;
	private JButton browseTargetButton;
	private JButton publishButton;
	private JButton exitButton;
	private JLabel sourceLabel;
	private JLabel targetLabel;
	private JPanel docControlPanel;
	private JProgressBar progressBar;
	private JLabel statusMessageLabel;
	private JTextField sourceDirField;
	private JTextField outputDirField;


	/**
	 * Default constructor.
	 */
	public BookPublisherGUI() {
		initComponents();
	}

	/**
	 * Initialise widgets.
	 */
	private void initComponents() {

        docControlPanel = new JPanel();
        sourceLabel = new JLabel();
        targetLabel = new JLabel();
        sourceDirField = new JTextField();
        outputDirField = new JTextField();
        browseSourceButton = new JButton();
        browseTargetButton = new JButton();
        publishButton = new JButton();
        progressBar = new JProgressBar();
        exitButton = new JButton();
        statusMessageLabel = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        setTitle("PDF Book Publisher");

        docControlPanel.setBorder(BorderFactory.createTitledBorder("Documents"));

        sourceLabel.setText("Source Folder:");
        browseSourceButton.setText("Browse ...");
        browseSourceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                browseSourceButtonActionHandler(event);
            }
        });

        targetLabel.setText("Target Folder:");
        browseTargetButton.setText("Browse ...");
        browseTargetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                browseTargetButtonActionHandler(event);
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
                        .add(sourceLabel)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(sourceDirField, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE))
                    .add(docControlPanelLayout.createSequentialGroup()
                        .add(targetLabel)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(outputDirField, GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE))
                    .add(GroupLayout.TRAILING, browseSourceButton)
                    .add(GroupLayout.TRAILING, browseTargetButton))
                .addContainerGap())
        );
        docControlPanelLayout.setVerticalGroup(
            docControlPanelLayout.createParallelGroup(GroupLayout.LEADING)
            .add(docControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(docControlPanelLayout.createParallelGroup(GroupLayout.BASELINE)
                    .add(sourceLabel)
                    .add(sourceDirField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(browseSourceButton)
                .add(14, 14, 14)
                .add(docControlPanelLayout.createParallelGroup(GroupLayout.BASELINE)
                    .add(targetLabel)
                    .add(outputDirField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(browseTargetButton)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        publishButton.setText("Publish");
        publishButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                publishButtonActionHandler(event);
            }
        });

        exitButton.setText("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                exitButtonActionHandler(event);
            }
        });

        String statusMessageText = "Press " + publishButton.getText() + " to start.";
        statusMessageLabel.setText(statusMessageText);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
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
                .add(statusMessageLabel, GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
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
                .add(statusMessageLabel)
                .add(43, 43, 43)
                .add(exitButton)
                .addContainerGap())
        );

        pack();
    }

	private void browseSourceButtonActionHandler(ActionEvent event) {
		browseDir(sourceDirField);
	}
	
	private void browseTargetButtonActionHandler(ActionEvent event) {
		browseDir(outputDirField);
	}
	
	private void browseDir(JTextField dirField) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			String selectedFilePath = selectedFile.getAbsolutePath();
			dirField.setText(selectedFilePath);
		}
	}

	private void exitButtonActionHandler(ActionEvent event) {
		this.setVisible(false);
		System.exit(0);
	}
	
	private void publishButtonActionHandler(ActionEvent event) {
		// convertOpenOfficeDocumentsToPdf();
		publishBook();
	}
	
	public void convertOpenOfficeDocumentsToPdf() {
		
		this.progressBar.setMinimum(0); 
		this.progressBar.setMaximum(100);
		
		File sourceDir = getSourceDir();
		File outputDir = getOutputDir();
		System.out.println( "Source dir is " + sourceDir );
		System.out.println( "Output dir is " + outputDir );
		
		if (sourceDir.isDirectory() && outputDir.isDirectory()) {
			try {
				setStatusMessage("Converting OpenOffice docs ...");
				ProgressMonitor progressMonitor = new ProgressBarMonitor(getProgressBar());
				System.out.println( "Converting OpenOffice docs in dir " + sourceDir + " to PDF ..." );
				OpenOfficeServerContext serverContext = new OpenOfficeServerContext();
				OpenOfficeDocConverter docConverter = new OpenOfficeDocConverter(serverContext);
				boolean verbose = true;
				docConverter.setVerboseEnabled(verbose);
				docConverter.setProgressMonitor(progressMonitor);
				docConverter.convertDocuments( sourceDir, outputDir, OpenOfficeDocConverter.OUTPUT_FORMAT_PDF );
				setStatusMessage("Finished converting documents.");
			} catch (Exception ex) {
				setStatusMessage("Error publishing book: " + ex.getMessage());
			}
		} else {
			setStatusMessage("Source or output folder is invalid.");
		}
	}
	
	public void publishBook() {
		
		this.progressBar.setMinimum(0); 
		this.progressBar.setMaximum(100);
		
		File sourceDir = getSourceDir();
		File outputDir = getOutputDir();
		File outputBookDir = getOutputDir().getParentFile();
		String outputBookName = FilenameUtils.getBaseName(outputDir.getName()) + "-book" + FileExtensionConstants.PDF_EXTENSION;
		File outputBookFile = new File( outputBookDir, outputBookName );
		String metaTitle = null;
		try {
			metaTitle = outputBookFile.getName();
			metaTitle = FilenameUtils.getBaseName(metaTitle);
		} catch (Exception e) {
			System.err.println("Error defining meta title");
		}
		String metaVersionId = "" + new Date().getTime();
		
		System.out.println( "Source dir is " + sourceDir );
		System.out.println( "Output dir is " + outputDir );
		System.out.println( "Output book file is " + outputBookFile );
		
		if (sourceDir.isDirectory() && outputDir.isDirectory()) {
			try {
				setStatusMessage("Publishing book ...");
				System.out.println( "Processing files in source dir " + sourceDir + " ..." );

				BookPublisherConfig config = buildBookPublisherConfig();
				config.setMetaTitle(metaTitle);
				config.setMetaVersionId(metaVersionId);
				BookPublisher bookPublisher = new BookPublisher();
				bookPublisher.setConfig( config );
				bookPublisher.publish( sourceDir, outputDir, outputBookFile );
				// setStatusMessage("Finished publishing book.");
				String fileName = outputBookFile.getName();
				setStatusMessage("Finished publishing book " + fileName);
				
			} catch (Exception ex) {
				setStatusMessage("Error publishing book: " + ex.getMessage());
			}
		} else {
			setStatusMessage("Source or output folder is invalid.");
		}
	}

	public void setStatusMessage(String msg) {
		statusMessageLabel.setText(msg);
	}

	public void updateProgressBar(int value) {
		progressBar.setValue(value);
	}
	
	private JProgressBar getProgressBar() {
		return this.progressBar;
	}
	
	private String getSourceDirName() {
		String dirName = sourceDirField.getText(); 
		if (dirName != null) {
			dirName = dirName.trim();
		}
		return dirName;
	}
	
	private File getSourceDir() {
		return new File( getSourceDirName() );
	}
	
	private String getOutputDirName() {
		String dirName = outputDirField.getText(); 
		if (dirName != null) {
			dirName = dirName.trim();
		}
		return dirName;
	}
	
	private File getOutputDir() {
		return new File( getOutputDirName() );
	}
	
	private BookPublisherConfig buildBookPublisherConfig() {
		
		BookPublisherConfig config = new BookPublisherConfig();
		
		ProgressMonitor progressMonitor = new ProgressBarMonitor(getProgressBar());
		config.setProgressMonitor(progressMonitor);
		
		OpenOfficeServerContext serverContext = new OpenOfficeServerContext();
		config.setServerContext(serverContext);
		
		Rectangle pageSize = BookPublisherConfig.DEFAULT_DOCUMENT_PAGE_SIZE;
		config.setPageSize(pageSize);

		// config.setMetaTitle(metaTitle);

		String metaAuthor = System.getProperty( "user.name" );
		config.setMetaAuthor(metaAuthor);

		boolean verbose = true;
		config.setVerboseEnabled(verbose);
		
		boolean debug = true;
		config.setDebugEnabled(debug);
		
		return config;
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new BookPublisherGUI().setVisible(true);
			}
		});
	}

}
