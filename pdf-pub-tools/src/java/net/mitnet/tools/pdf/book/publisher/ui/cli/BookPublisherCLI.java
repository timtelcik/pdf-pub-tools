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

package net.mitnet.tools.pdf.book.publisher.ui.cli;

import java.io.File;

import net.mitnet.tools.pdf.book.io.FileExtensionConstants;
import net.mitnet.tools.pdf.book.openoffice.converter.OpenOfficeDocConverter;
import net.mitnet.tools.pdf.book.openoffice.net.OpenOfficeServerContext;
import net.mitnet.tools.pdf.book.openoffice.ui.cli.OpenOfficeDocConverterCLI;
import net.mitnet.tools.pdf.book.pdf.builder.PdfBookBuilder;
import net.mitnet.tools.pdf.book.pdf.builder.ui.cli.PdfBookBuilderCLI;
import net.mitnet.tools.pdf.book.publisher.BookPublisher;
import net.mitnet.tools.pdf.book.publisher.BookPublisherConfig;
import net.mitnet.tools.pdf.book.ui.cli.CliConstants;
import net.mitnet.tools.pdf.book.ui.cli.CommandLineHelper;
import net.mitnet.tools.pdf.book.ui.cli.ConsoleProgressMonitor;
import net.mitnet.tools.pdf.book.util.ProgressMonitor;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;


/**
 * Book Publisher Command Line Interface (CLI).
 * 
 * This program converts a collection of Open Office documents into a single 2-up PDF.
 * 
 * TODO - refactor CLI logic with OpenOfficeDocConverterCLI and PdfBookBuilderCLI
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see OpenOfficeDocConverter
 * @see OpenOfficeDocConverterCLI 
 * @see PdfBookBuilder
 * @see PdfBookBuilderCLI
 */
public class BookPublisherCLI {
	
	private static final File DEFAULT_OUTPUT_FILE = new File( "my-book" + FileExtensionConstants.PDF_EXTENSION );
	

	private static final Options OPTIONS = initOptions();
	
	// private static BookPublisherConfig config = new BookPublisherConfig();
	
	
	private static Options initOptions() {
		Options options = new Options();
		options.addOption(CliConstants.OPTION_SOURCE_DIR);
		options.addOption(CliConstants.OPTION_OUTPUT_DIR);
		options.addOption(CliConstants.OPTION_OUTPUT_BOOK_FILE);
		options.addOption(CliConstants.OPTION_PAGE_SIZE);
		options.addOption(CliConstants.OPTION_DEBUG);
		options.addOption(CliConstants.OPTION_META_TITLE);
		options.addOption(CliConstants.OPTION_META_AUTHOR);
		options.addOption(CliConstants.OPTION_META_VERSION_ID);
		options.addOption(CliConstants.OPTION_OPEN_OFFICE_HOST);
		options.addOption(CliConstants.OPTION_OPEN_OFFICE_PORT);
		options.addOption(CliConstants.OPTION_VERBOSE);
		options.addOption(CliConstants.OPTION_TOC_TEMPLATE_PATH);
		return options;
	}

	private static BookPublisherConfig parseConfig( String[] arguments ) throws Exception {

		CommandLineParser commandLineParser = new PosixParser();
		CommandLine commandLine = commandLineParser.parse(OPTIONS, arguments);
		CommandLineHelper commandLineHelper = new CommandLineHelper( commandLine );
		
		BookPublisherConfig config = new BookPublisherConfig();
		
		if (!commandLineHelper.hasOption(CliConstants.OPTION_SOURCE_DIR)) {
			System.err.println("Must specify " + CliConstants.OPTION_SOURCE_DIR.getDescription());
			showHelp();
			System.exit(CliConstants.EXIT_CODE_ERROR);
		}
		File sourceDir = commandLineHelper.getOptionValueAsFile(CliConstants.OPTION_SOURCE_DIR);
		config.setSourceDir(sourceDir);
		
		if (!commandLineHelper.hasOption(CliConstants.OPTION_OUTPUT_DIR)) {
			System.err.println("Must specify " + CliConstants.OPTION_OUTPUT_DIR.getDescription());
			showHelp();
			System.exit(CliConstants.EXIT_CODE_ERROR);
		}
		File outputDir = commandLineHelper.getOptionValueAsFile(CliConstants.OPTION_OUTPUT_DIR);
		config.setOutputDir(outputDir);

		if (!commandLineHelper.hasOption(CliConstants.OPTION_OUTPUT_BOOK_FILE)) {
			System.err.println("Must specify " + CliConstants.OPTION_OUTPUT_BOOK_FILE.getDescription());
			showHelp();
			System.exit(CliConstants.EXIT_CODE_ERROR);
		}
		File outputBookFile = commandLineHelper.getOptionValueAsFile(CliConstants.OPTION_OUTPUT_BOOK_FILE);
		config.setOutputBookFile(outputBookFile);
		
		OpenOfficeServerContext serverContext = new OpenOfficeServerContext();
		config.setServerContext(serverContext);

		String openOfficeHost = OpenOfficeServerContext.DEFAULT_HOST;
		if (commandLineHelper.hasOption(CliConstants.OPTION_OPEN_OFFICE_HOST)) {
			openOfficeHost = commandLineHelper.getOptionValue(CliConstants.OPTION_OPEN_OFFICE_HOST);
			serverContext.setHost(openOfficeHost);
		}

		int openOfficePort = OpenOfficeServerContext.DEFAULT_PORT;
		if (commandLineHelper.hasOption(CliConstants.OPTION_OPEN_OFFICE_PORT)) {
			openOfficePort = commandLineHelper.getOptionValueAsInt(CliConstants.OPTION_OPEN_OFFICE_PORT);
			serverContext.setPort(openOfficePort);
		}
		
		// TODO - resolve page size using Locale
		// eg. "en_AU" = PageSize.A4
		//     "en_US" = PageSize.LETTER

		Rectangle pageSize = PageSize.A4;
		// Rectangle pageSize = PageSize.LETTER;
		if (commandLineHelper.hasOption(CliConstants.OPTION_PAGE_SIZE)) {
			String pageSizeString = commandLineHelper.getOptionValue(CliConstants.OPTION_PAGE_SIZE);
			System.out.println("Page Size Option Detected: " + pageSizeString);
			if (!StringUtils.isEmpty(pageSizeString)) {
				if (CliConstants.PAGE_SIZE_US_LETTER_STRING.equalsIgnoreCase(pageSizeString)) {
					System.out.println("Setting Page Size to Letter");
					pageSize = PageSize.LETTER;
				}
			}
		}
		config.setPageSize(pageSize);

		boolean debugEnabled = false;
		if (commandLineHelper.hasOption(CliConstants.OPTION_DEBUG)) {
			debugEnabled = true;
		}
		config.setDebugEnabled(debugEnabled);
		
		boolean verboseEnabled = false;
		if (commandLineHelper.hasOption(CliConstants.OPTION_VERBOSE)) {
			verboseEnabled = true;
		}
		config.setVerboseEnabled(verboseEnabled);
		
		File tocTemplatePath = null;
		if (commandLineHelper.hasOption(CliConstants.OPTION_TOC_TEMPLATE_PATH)) {
			tocTemplatePath = commandLineHelper.getOptionValueAsFile(CliConstants.OPTION_TOC_TEMPLATE_PATH);
			if (tocTemplatePath != null) {
				config.setTocTemplatePath(tocTemplatePath.getCanonicalPath());
			}
		}
		
		/*
		String metaTitle = FilenameUtils.getBaseName( outputBookFile.getName() );
		if (metaTitle != null) {
			metaTitle = metaTitle.toUpperCase();
		}
		*/
		String metaTitle = null;
		if (commandLineHelper.hasOption(CliConstants.OPTION_META_TITLE)) {
			metaTitle = commandLineHelper.getOptionValue(CliConstants.OPTION_META_TITLE);
		}
		config.setMetaTitle(metaTitle);
		
		// String metaAuthor = System.getProperty( "user.name" );
		String metaAuthor = null;
		if (commandLineHelper.hasOption(CliConstants.OPTION_META_AUTHOR)) {
			metaAuthor = commandLineHelper.getOptionValue(CliConstants.OPTION_META_AUTHOR);
		}
		config.setMetaAuthor(metaAuthor);
		
		if (commandLineHelper.hasOption(CliConstants.OPTION_META_VERSION_ID)) {
			String metaVersionId = commandLineHelper.getOptionValue(CliConstants.OPTION_META_VERSION_ID);
			config.setMetaVersionId(metaVersionId);
		}
		
		if (verboseEnabled) {
			ProgressMonitor progressMonitor = new ConsoleProgressMonitor();
			config.setProgressMonitor(progressMonitor);
		}
		
		return config;
	}
		
		
	public static void main(String[] arguments) throws Exception {
		
		BookPublisherConfig config = new BookPublisherConfig();			
		
		try {

			if (config.isVerboseEnabled()) {
				verbose( "Source dir is \"" + config.getSourceDir() + "\"" );
				verbose( "Output dir is \"" + config.getOutputDir() + "\"" );
				verbose( "Output file is \"" + config.getOutputBookFile() + "\"" );
				verbose( "Page size is " + config.getPageSize() );
				if (config.getTocTemplatePath() != null) {
					verbose( "-- TOC template path is \"" + config.getTocTemplatePath() + "\"" );
				}
			}
			
			debug( "Publishing PDF book \"" + config.getOutputBookFile() + "\" from files in source folder \"" + config.getSourceDir() + "\" ..." );
			
			BookPublisher bookPublisher = new BookPublisher();
			bookPublisher.setConfig( config );
			bookPublisher.publish( config.getSourceDir(), config.getOutputDir(), config.getOutputBookFile() );

		} catch (Exception e) {
			String msg = "Error publishing book \"" + config.getOutputBookFile() + "\" : " + e.getMessage();
			error( msg, e );
			throw new Exception( msg, e );
		}
		
		System.out.println( "Finished publishing book \"" + config.getOutputBookFile() + "\"." );
	}

	private static void showHelp() {
		// TODO - show all args
		String syntax = BookPublisherCLI.class.getName()
				+ " [options] -indir <input-dir> -ooutdir <output-dir> [-of <output-file>] [-p <page-size>] \n";
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(syntax, OPTIONS);
		System.exit(CliConstants.EXIT_CODE_TOO_FEW_ARGS);
	}
	
	private static void verbose( String msg ) {
		System.out.println( msg );
	}
	
	private static void debug( String msg ) {
		System.out.println( "-- " + msg );
	}
	
	private static void error( String msg ) {
		System.err.println( "-- " + msg );
	}
	
	private static void error( String msg, Exception exception ) {
		System.err.println( "-- " + msg );
		if (exception != null) {
			System.err.println( exception );
			exception.printStackTrace(System.err);
		}
	}	

}

