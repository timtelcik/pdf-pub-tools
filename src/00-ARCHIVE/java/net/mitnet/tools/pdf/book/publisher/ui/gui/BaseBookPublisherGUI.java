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

import java.io.File;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

import org.apache.commons.io.FilenameUtils;

import com.lowagie.text.Rectangle;


/**
 * Base Book Publisher GUI.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see com.lowagie.toolbox.plugins.Handouts
 * @see com.lowagie.toolbox.plugins.NUp
 * @see BookPublisher
 */
public abstract class BaseBookPublisherGUI {
	
	protected BaseBookPublisherGUI() {
		// do nothing (yet)
	}

	
	protected void browseDir(JTextField dirField) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			String selectedFilePath = selectedFile.getAbsolutePath();
			dirField.setText(selectedFilePath);
		}
	}

	
	protected void browseFile(JTextField dirField) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			String selectedFilePath = selectedFile.getAbsolutePath();
			dirField.setText(selectedFilePath);
		}
	}
	
	
	protected abstract void setStatusMessage( String msg );

	
	protected void convertOpenOfficeDocumentsToPdf() {
		
		File inputDir = getInputDir();
		File outputDir = getOutputDir();
		System.out.println( "Input dir is " + inputDir );
		System.out.println( "Output dir is " + outputDir );
		
		if (inputDir.isDirectory() && outputDir.isDirectory()) {
			try {
				setStatusMessage("Converting OpenOffice docs ...");
				// ProgressMonitor progressMonitor = new ProgressBarMonitor(getProgressBar());
				ProgressMonitor progressMonitor = getProgressMonitor();
				System.out.println( "Converting OpenOffice docs in dir " + inputDir + " to PDF ..." );
				OpenOfficeServerContext serverContext = new OpenOfficeServerContext();
				OpenOfficeDocConverter docConverter = new OpenOfficeDocConverter(serverContext);
				boolean verbose = true;
				docConverter.setVerboseEnabled(verbose);
				docConverter.setProgressMonitor(progressMonitor);
				docConverter.convertDocuments( inputDir, outputDir, OpenOfficeDocConverter.OUTPUT_FORMAT_PDF );
				setStatusMessage("Finished converting documents.");
			} catch (Exception ex) {
				setStatusMessage("Error publishing book: " + ex.getMessage());
			}
		} else {
			setStatusMessage("Source or output folder is invalid.");
		}
	}

	
	protected void publishBook() {
		
		File inputDir = getInputDir();
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
		
		System.out.println( "Input dir is " + inputDir );
		System.out.println( "Output dir is " + outputDir );
		System.out.println( "Output book file is " + outputBookFile );
		
		if (inputDir.isDirectory() && outputDir.isDirectory()) {
			try {
				setStatusMessage("Publishing book ...");
				System.out.println( "Processing files in input dir " + inputDir + " ..." );
	
				BookPublisherConfig config = buildBookPublisherConfig();
				config.setMetaTitle(metaTitle);
				config.setMetaVersionId(metaVersionId);
				BookPublisher bookPublisher = new BookPublisher();
				bookPublisher.setConfig( config );
				bookPublisher.publish( inputDir, outputDir, outputBookFile );
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

	
	public File getInputDir() {
		return new File( getInputDirName() );
	}
	
	
	public abstract String getInputDirName();

	
	public File getOutputDir() {
		return new File( getOutputDirName() );
	}
	
	
	public abstract String getOutputDirName();

	
	protected abstract ProgressMonitor getProgressMonitor();

	
	protected BookPublisherConfig buildBookPublisherConfig() {
		
		BookPublisherConfig config = new BookPublisherConfig();
		
		// ProgressMonitor progressMonitor = new ProgressBarMonitor(getProgressBar());
		ProgressMonitor progressMonitor = getProgressMonitor();
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

}
