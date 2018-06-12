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

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import net.mitnet.tools.pdf.book.io.FileExtensionConstants;
import net.mitnet.tools.pdf.book.openoffice.converter.OpenOfficeDocConverter;
import net.mitnet.tools.pdf.book.openoffice.net.OpenOfficeServerContext;
import net.mitnet.tools.pdf.book.pdf.util.PdfPageSizeHelper;
import net.mitnet.tools.pdf.book.publisher.BookPublisher;
import net.mitnet.tools.pdf.book.publisher.BookPublisherConfig;
import net.mitnet.tools.pdf.book.ui.gui.ProgressBarMonitor;
import net.mitnet.tools.pdf.book.util.ProgressMonitor;

import org.apache.commons.io.FilenameUtils;

import com.lowagie.text.Rectangle;


/**
 * Book Publisher GUI 2.
 * 
 * This program publishes a bundle of OpenOffice documents (*.odt) 
 * into a single 2-up PDF document.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see com.lowagie.toolbox.plugins.Handouts
 * @see com.lowagie.toolbox.plugins.NUp
 * @see BookPublisher
 * 
 * TODO - refactor GUI widget layout
 */
public class BookPublisherGUI2 extends BaseBookPublisherGUI {
	
	private static final long serialVersionUID = -7359553392594050434L;
	
	private static final String FRAME_TITLE = "PDF Book Publisher 2";
	
    public static final int FILE_TEXTFIELD_WIDTH = 40;
	
	private JFrame frame;
	
	private JButton publishButton;
	private JButton exitButton;
	
	private JLabel inputDirLabel;
	private JTextField inputDirField;	
	private JButton inputDirButton;
	
	private JLabel outputDirLabel;
	private JTextField outputDirField;
	private JButton outputDirButton;

	private JPanel docControlPanel;
	
	private JProgressBar progressBar;
	
	private JLabel statusMessageLabel;


	/**
	 * Default constructor.
	 */
	public BookPublisherGUI2() {
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

	
	public ProgressMonitor getProgressMonitor() {
		ProgressMonitor progressMonitor = new ProgressBarMonitor(getProgressBar());
		return progressMonitor;
	}


	/**
	 * Initialise widgets.
	 */
	protected void initComponents() {
		
		// frame
		this.frame = new JFrame();
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setTitle(FRAME_TITLE);
		

        // input dir panel
        
        inputDirLabel = new JLabel();
        inputDirLabel.setText("Input Folder:");
        
        inputDirField = new JTextField();
        inputDirField.setColumns(FILE_TEXTFIELD_WIDTH);
        
        inputDirButton = new JButton();
        inputDirButton.setText("Browse ...");
        inputDirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                inputDirButtonActionHandler(event);
            }
        });
        
        JPanel inputDirPanel = new JPanel();
        // inputDirPanel.setLayout( new BoxLayout( inputDirPanel, BoxLayout.X_AXIS ) );
        inputDirPanel.setLayout( new FlowLayout( FlowLayout.LEFT ) );
        inputDirPanel.add(inputDirLabel);
        inputDirPanel.add(inputDirField);
        inputDirPanel.add(inputDirButton);

		
        // output dir panel
        
        outputDirLabel = new JLabel();
        outputDirLabel.setText("Output Folder:");
        
        outputDirField = new JTextField();
        outputDirField.setColumns(FILE_TEXTFIELD_WIDTH);
        
        outputDirButton = new JButton();
        outputDirButton.setText("Browse ...");
        outputDirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                outputDirButtonActionHandler(event);
            }
        });
        
        JPanel outputDirPanel = new JPanel();
        // outputDirPanel.setLayout( new BoxLayout( outputDirPanel, BoxLayout.X_AXIS ) );
        outputDirPanel.setLayout( new FlowLayout( FlowLayout.LEFT ) );
        outputDirPanel.add(outputDirLabel);
        outputDirPanel.add(outputDirField);
        outputDirPanel.add(outputDirButton);
        

        // document control panel
        docControlPanel = new JPanel();
        docControlPanel.setLayout( new BoxLayout( docControlPanel, BoxLayout.Y_AXIS ) );
        docControlPanel.add(inputDirPanel);
        docControlPanel.add(outputDirPanel);
        // Border docControlPanelBorder = BorderFactory.createTitledBorder("Documents");
        Border docControlPanelBorder = BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Documents"), BorderFactory.createEmptyBorder(5, 5, 5, 5)); 
        docControlPanel.setBorder(docControlPanelBorder);
        
        
        // publish button
        publishButton = new JButton();
        publishButton.setText("Publish");
        publishButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                publishButtonActionHandler(event);
            }
        });
        
        
        // status message label
        statusMessageLabel = new JLabel();
        // String statusMessageText = "Press " + publishButton.getText() + " to start.";
        String statusMessageText = "";
        statusMessageLabel.setText(statusMessageText);
        // statusMessageLabel.setVisible(false);
        

		// progress bar
        progressBar = new JProgressBar();


        // exit button
        exitButton = new JButton();
        exitButton.setText("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                exitButtonActionHandler(event);
            }
        });

        
        // build main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout( new BoxLayout( mainPanel, BoxLayout.Y_AXIS ) );
        mainPanel.add(docControlPanel);
        // mainPanel.add(new Spacer());
        mainPanel.add(publishButton);
        // mainPanel.add(new Spacer());
        mainPanel.add(progressBar);
        // mainPanel.add(new Spacer());
        mainPanel.add(statusMessageLabel);
        // mainPanel.add(new Spacer());
        mainPanel.add(exitButton);


        // add main panel to frame
        this.frame.getContentPane().setLayout( new BoxLayout( this.frame.getContentPane(), BoxLayout.Y_AXIS ) );
        this.frame.getContentPane().add(mainPanel);
        // this.frame.setSize(800,600);
        this.frame.pack();

    }

	private void inputDirButtonActionHandler(ActionEvent event) {
		browseDir(inputDirField);
	}
	
	private void outputDirButtonActionHandler(ActionEvent event) {
		browseDir(outputDirField);
	}
	
	private void exitButtonActionHandler(ActionEvent event) {
		this.frame.setVisible(false);
		System.exit(0);
	}
	
	private void publishButtonActionHandler(ActionEvent event) {
		// convertOpenOfficeDocumentsToPdf();
		publishBook();
	}
	

	public void setStatusMessage(String msg) {
		statusMessageLabel.setText(msg);
	}

	private void updateProgressBar(int value) {
		progressBar.setValue(value);
	}
	
	private JProgressBar getProgressBar() {
		return this.progressBar;
	}
	
	public String getInputDirName() {
		String dirName = inputDirField.getText(); 
		if (dirName != null) {
			dirName = dirName.trim();
		}
		return dirName;
	}
	
	public File getInputDir() {
		return new File( getInputDirName() );
	}
	
	public String getOutputDirName() {
		String dirName = outputDirField.getText(); 
		if (dirName != null) {
			dirName = dirName.trim();
		}
		return dirName;
	}
	
	public File getOutputDir() {
		return new File( getOutputDirName() );
	}
	
	
	/*
	public BookPublisherConfig buildBookPublisherConfig() {
		
		BookPublisherConfig config = new BookPublisherConfig();
		
		ProgressMonitor progressMonitor = new ProgressBarMonitor(getProgressBar());
		config.setProgressMonitor(progressMonitor);
		
		OpenOfficeServerContext serverContext = new OpenOfficeServerContext();
		config.setServerContext(serverContext);
		
		Rectangle pageSize = PdfPageSizeHelper.getDefaultPageSizeByLocale();
		config.setPageSize(pageSize);

		// config.setMetaTitle(metaTitle);

		// String metaAuthor = System.getProperty( "user.name" );
		// config.setMetaAuthor(metaAuthor);
		
		config.setNup(BookPublisherConfig.DEFAULT_NUP);

		boolean verbose = true;
		config.setVerboseEnabled(verbose);
		
		boolean debug = true;
		config.setDebugEnabled(debug);
		
		return config;
	}
	*/

	
	public static void main( String[] args ) {
		SwingUtilities.invokeLater( new GuiRunner() );
	}
	
	
	public static class GuiRunner implements Runnable {
		public void run() {
			BookPublisherGUI2 gui = new BookPublisherGUI2();
			gui.show();
		}		
	}

}
