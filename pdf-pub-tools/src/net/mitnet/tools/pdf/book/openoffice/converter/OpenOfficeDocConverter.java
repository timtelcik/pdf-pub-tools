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

package net.mitnet.tools.pdf.book.openoffice.converter;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import net.mitnet.tools.pdf.book.io.FileHelper;
import net.mitnet.tools.pdf.book.openoffice.net.OpenOfficeConnectionFactory;
import net.mitnet.tools.pdf.book.openoffice.net.OpenOfficeServerContext;
import net.mitnet.tools.pdf.book.util.MathHelper;
import net.mitnet.tools.pdf.book.util.ProgressMonitor;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;


/**
 * Open Office Document Converter.
 * 
 * This class is a facade to the underlying "OpenOfficeDocumentConverter".
 * 
 * TODO - review tracing and debugging.
 * TODO - review convert document method signatures
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see com.artofsolving.jodconverter.cli.ConvertDocument
 * @see com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter
 */
public class OpenOfficeDocConverter {
	
	public static final String OUTPUT_FORMAT_PDF = "pdf";
	public static final String OUTPUT_FORMAT_OPEN_OFFICE = "odt";
	public static final String DEFAULT_OUTPUT_FORMAT = OUTPUT_FORMAT_PDF;

	private OpenOfficeServerContext serverContext = null;
	private boolean traceEnabled = false;
	private boolean debugEnabled = false;
	

	public OpenOfficeDocConverter( OpenOfficeServerContext serverContext ) throws Exception {
		this.serverContext = serverContext;
	}
	
	public OpenOfficeDocConverter( String openOfficeHost, int openOfficePort ) throws Exception {
		this.serverContext = new OpenOfficeServerContext( openOfficeHost, openOfficePort );
	}

	public void setTraceEnabled( boolean value ) {
		this.traceEnabled = value;
	}

	public boolean isTraceEnabled() {
		return this.traceEnabled;
	}
	
	public void setDebug( boolean value ) {
		this.debugEnabled = value;
	}
	
	public boolean isDebugEnabled() {
		return this.debugEnabled;
	}

	public void convertDocuments( File sourceDir, File outputDir, String outputFormat, ProgressMonitor progresMonitor ) throws Exception {

		if (isTraceEnabled()) {
			trace( "sourceDir: " + sourceDir);
			trace( "outputDir: " + outputDir);
			trace( "outputFormat: " + outputFormat);
		}
		
		List<File> sourceFileList = FileHelper.findOpenOfficeFiles(sourceDir,true);
		if (isDebugEnabled()) {
			trace( "sourceFileList.size: " + sourceFileList.size());
			trace( "sourceFileList: " + sourceFileList);
		}
		
		if (!sourceFileList.isEmpty()) {
			convertDocuments( sourceDir, sourceFileList, outputDir, outputFormat, progresMonitor );			
		}
	}

	public void convertDocuments( File sourceDir, List<File> sourceFileList, File outputDir, String outputFormat, ProgressMonitor progresMonitor ) throws Exception {

		if (isTraceEnabled()) {
			trace( "sourceDir: " + sourceDir);
			trace( "sourceFileList: " + sourceFileList);
			trace( "outputDir: " + outputDir);
			trace( "outputFormat: " + outputFormat);
			trace("converting " + sourceFileList.size() + " document(s)");
			trace("output dir is " + outputDir);
			trace("output format is " + outputFormat);
			trace("connecting to OpenOffice server " + this.serverContext);
		}
		OpenOfficeConnection connection = openConnection(serverContext);
		if (isTraceEnabled()) {
			trace("connection is " + connection );
		}

		try {
			DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
			if (isTraceEnabled()) {
				trace("converter is " + converter );
			}
			
			int currentItemIndex = 0;
			int maxItemIndex = sourceFileList.size();
			for (File inputFile : sourceFileList) {
				currentItemIndex++;
				String baseOutputFilePath = null;
				if ( outputDir == null ) {
					baseOutputFilePath = inputFile.getParent();
				} else {
					String relativePath = FileHelper.parseRelativePathToParent( sourceDir, inputFile );
					if (StringUtils.isEmpty(relativePath)) {
						baseOutputFilePath = outputDir.getAbsolutePath();
					} else {
						baseOutputFilePath = new File( outputDir, relativePath ).getPath();
					}
				}
				String baseInputFileName = FilenameUtils.getBaseName(inputFile.getName());
				String outputFileName =  baseInputFileName + "." + outputFormat;
				File outputFile = new File( baseOutputFilePath, outputFileName );
				convertDocument( converter, inputFile, outputFile );
				if (progresMonitor != null) {
					int progressPercentage = MathHelper.calculatePercentage( currentItemIndex, maxItemIndex );
					progresMonitor.setProgressPercentage(progressPercentage);
				}
			}

		} finally {
			if (isTraceEnabled()) {
				trace("disconnecting");
			}
			connection.disconnect();
		}
	}
	
	public void convertDocument( File sourceFile, File outputDir, String outputFormat, ProgressMonitor progresMonitor ) throws Exception {
		List<File> sourceFiles = new ArrayList<File>();
		sourceFiles.add( sourceFile );
		File sourceDir = sourceFile.getParentFile();
		convertDocuments( sourceDir, sourceFiles, outputDir, outputFormat, progresMonitor );
	}
	
	private void convertDocument( DocumentConverter converter, File inputFile, File outputFile  ) throws IOException {

		String inputFileExtension = FilenameUtils.getExtension(inputFile.getName());
		String outputFileExtension = FilenameUtils.getExtension(outputFile.getName());
		
		boolean sameExtension = false;
		
		if ((inputFileExtension != null) && (outputFileExtension != null)) {
			if (inputFileExtension.equals(outputFileExtension)) {
				sameExtension = true;
			}
		}
		
		if (sameExtension) {
			if (isTraceEnabled()) {
				trace("input and output file have same extension - copying " + inputFile + " to " + outputFile);
			}
			FileUtils.copyFile(inputFile, outputFile);
		} else {
			if (isTraceEnabled()) {
				trace("converting " + inputFile + " to " + outputFile);
			}
			converter.convert(inputFile, outputFile);
			// converter.convert(inputFile, inputFormat, outputFile, outputFormat);
		}
	}
	
	private OpenOfficeConnection openConnection( OpenOfficeServerContext serverContext ) throws Exception {

		OpenOfficeConnection connection = OpenOfficeConnectionFactory.createConnection(serverContext);
		try {
			if (isTraceEnabled()) {
				trace("connecting to OpenOffice using server context " + serverContext);
			}
			connection.connect();
		} catch (ConnectException ce) {
			ce.printStackTrace(System.err);
			String msg = "Error connecting to OpenOffice using server context " + serverContext;
			System.err.println(msg);
			throw new Exception(msg, ce);
		}
		
		return connection;
	}
	
	private void trace( String msg ) {
		if (isTraceEnabled()) {
			System.out.println( "" + msg );
		}
	}
	
	private void debug( String msg ) {
		if (isDebugEnabled()) {
			System.out.println( "--" + msg );
		}
	}

}
