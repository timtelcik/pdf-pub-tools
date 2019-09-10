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

package net.mitnet.tools.pdf.book.publisher.ui.cli;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import com.google.common.io.Files;
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
	
	private static final String CLI_USAGE = 
			" [options] -indir <input-dir> -ooutdir <output-dir> [-of <output-file>] [-p <page-size>] \n";
	
	private static final String CLI_HEADER =
			"BookPublisherCLI - Book Publisher Tool (c) 2010-2013 Tim Telcik <telcik@gmail.com>";
	
	private static final String CLI_FOOTER =
			"For more instructions, see the PDF Publishing Tools project at \"https://code.google.com/p/pdf-pub-tools/\"";
	
	private static final File DEFAULT_OUTPUT_FILE = new File( "my-book" + FileExtensionConstants.PDF_EXTENSION );
	
	private static final int DEFAULT_NUP = CliDefaultValues.DEFAULT_NUP;
	
	private static final int CLI_USAGE_HELP_WIDTH = 80;

	private static final Options CLI_OPTIONS = initOptions();
	
	
	private static Options initOptions() {
		Options options = new Options();
		options.addOption(CliOptions.OPTION_INPUT_DIR);
		options.addOption(CliOptions.OPTION_OUTPUT_DIR);
		options.addOption(CliOptions.OPTION_OUTPUT_BOOK_FILE);
		options.addOption(CliOptions.OPTION_PAGE_SIZE);
		options.addOption(CliOptions.OPTION_PAGE_ORIENTATION);
		options.addOption(CliOptions.OPTION_DEBUG);
		options.addOption(CliOptions.OPTION_META_TITLE);
		options.addOption(CliOptions.OPTION_META_AUTHOR);
		options.addOption(CliOptions.OPTION_META_VERSION_ID);
		options.addOption(CliOptions.OPTION_OPEN_OFFICE_HOST);
		options.addOption(CliOptions.OPTION_OPEN_OFFICE_PORT);
		options.addOption(CliOptions.OPTION_VERBOSE);
		options.addOption(CliOptions.OPTION_TOC_TEMPLATE_PATH);
		// options.addOption(CliConstants.OPTION_FRONT_COVER_TEMPLATE_PATH);
		options.addOption(CliOptions.OPTION_NUP);
		return options;
	}

	
	private BookPublisherConfig parseConfig( String[] arguments ) throws Exception {

		CommandLineParser commandLineParser = new PosixParser();
		CommandLine commandLine = commandLineParser.parse(CLI_OPTIONS, arguments);
		CommandLineHelper commandLineHelper = new CommandLineHelper( commandLine );
		
		BookPublisherConfig config = new BookPublisherConfig();
		
		if (!commandLineHelper.hasOption(CliOptions.OPTION_INPUT_DIR)) {
			System.err.println("Must specify " + CliOptions.OPTION_INPUT_DIR.getDescription());
			showHelp();
		}
		File sourceDir = commandLineHelper.getOptionValueAsFile(CliOptions.OPTION_INPUT_DIR);
		if (!sourceDir.exists()) {
			System.err.println("Must specify " + CliOptions.OPTION_INPUT_DIR.getDescription());
			showHelp();
		}
		config.setInputDir(sourceDir);
		
		/*
		if (!commandLineHelper.hasOption(CliConstants.OPTION_OUTPUT_DIR)) {
			System.err.println("Must specify " + CliConstants.OPTION_OUTPUT_DIR.getDescription());
			showHelp();
			System.exit(CliConstants.EXIT_CODE_ERROR);
		}
		File outputDir = commandLineHelper.getOptionValueAsFile(CliConstants.OPTION_OUTPUT_DIR);
		if (!outputDir.exists()) {
			System.err.println("Must specify " + CliConstants.OPTION_OUTPUT_DIR.getDescription());
			showHelp();
			System.exit(CliConstants.EXIT_CODE_ERROR);
		}
		config.setOutputDir(outputDir);		
		*/
		
		File outputDir = null;
		if (commandLineHelper.hasOption(CliOptions.OPTION_OUTPUT_DIR)) {
			outputDir = commandLineHelper.getOptionValueAsFile(CliOptions.OPTION_OUTPUT_DIR);
			if (!outputDir.exists()) {
				System.err.println("Must specify " + CliOptions.OPTION_OUTPUT_DIR.getDescription());
				showHelp();
			}
		} else {		
			outputDir = Files.createTempDir();
			// outputDir.deleteOnExit();
			// TODO: add runtime shotdown hook to recursively remove of temp folder
			// Thread removeTempFolderShutdownHook = new RemoveTempFolderShutdownHook();
			// Runtime.getRuntime().addShutdownHook(removeTempFolderShutdownHook);
		}
		config.setOutputDir(outputDir);

		if (!commandLineHelper.hasOption(CliOptions.OPTION_OUTPUT_BOOK_FILE)) {
			System.err.println("Must specify " + CliOptions.OPTION_OUTPUT_BOOK_FILE.getDescription());
			showHelp();
		}
		File outputBookFile = commandLineHelper.getOptionValueAsFile(CliOptions.OPTION_OUTPUT_BOOK_FILE);
		config.setOutputBookFile(outputBookFile);
		
		OpenOfficeServerContext serverContext = new OpenOfficeServerContext();
		config.setServerContext(serverContext);

		String openOfficeHost = OpenOfficeServerContext.DEFAULT_HOST;
		if (commandLineHelper.hasOption(CliOptions.OPTION_OPEN_OFFICE_HOST)) {
			openOfficeHost = commandLineHelper.getOptionValue(CliOptions.OPTION_OPEN_OFFICE_HOST);
			serverContext.setHost(openOfficeHost);
		}

		int openOfficePort = OpenOfficeServerContext.DEFAULT_PORT;
		if (commandLineHelper.hasOption(CliOptions.OPTION_OPEN_OFFICE_PORT)) {
			openOfficePort = commandLineHelper.getOptionValueAsInt(CliOptions.OPTION_OPEN_OFFICE_PORT);
			serverContext.setPort(openOfficePort);
		}
		
		Rectangle pageSize = ConfigHelper.parsePageSize(commandLineHelper);
		config.setPageSize(pageSize);
		
		boolean debugEnabled = false;
		if (commandLineHelper.hasOption(CliOptions.OPTION_DEBUG)) {
			debugEnabled = true;
		}
		config.setDebugEnabled(debugEnabled);
		
		boolean verboseEnabled = false;
		if (commandLineHelper.hasOption(CliOptions.OPTION_VERBOSE)) {
			verboseEnabled = true;
		}
		config.setVerboseEnabled(verboseEnabled);
		
		File tocTemplatePath = null;
		if (commandLineHelper.hasOption(CliOptions.OPTION_TOC_TEMPLATE_PATH)) {
			tocTemplatePath = commandLineHelper.getOptionValueAsFile(CliOptions.OPTION_TOC_TEMPLATE_PATH);
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
		if (commandLineHelper.hasOption(CliOptions.OPTION_META_TITLE)) {
			metaTitle = commandLineHelper.getOptionValue(CliOptions.OPTION_META_TITLE);
		}
		config.setMetaTitle(metaTitle);
		
		// String metaAuthor = System.getProperty( "user.name" );
		String metaAuthor = null;
		if (commandLineHelper.hasOption(CliOptions.OPTION_META_AUTHOR)) {
			metaAuthor = commandLineHelper.getOptionValue(CliOptions.OPTION_META_AUTHOR);
		}
		config.setMetaAuthor(metaAuthor);
		
		if (commandLineHelper.hasOption(CliOptions.OPTION_META_VERSION_ID)) {
			String metaVersionId = commandLineHelper.getOptionValue(CliOptions.OPTION_META_VERSION_ID);
			config.setMetaVersionId(metaVersionId);
		}
		
		int nup = DEFAULT_NUP;
		if (commandLineHelper.hasOption(CliOptions.OPTION_NUP)) {
			nup = commandLineHelper.getOptionValueAsInt(CliOptions.OPTION_NUP);
			config.setNup(nup);
		}		
		
		if (verboseEnabled) {
			ProgressMonitor progressMonitor = new ConsoleProgressMonitor();
			config.setProgressMonitor(progressMonitor);
		}
		
		return config;
	}
	
	
	public void run( String[] args ) throws Exception {
		
		BookPublisherConfig config = parseConfig(args);

		if (config.isVerboseEnabled()) {
			verbose( "Source dir is \"" + config.getInputDir() + "\"" );
			verbose( "Output dir is \"" + config.getOutputDir() + "\"" );
			verbose( "Output file is \"" + config.getOutputBookFile() + "\"" );
			verbose( "Page size is " + config.getPageSize() );
			if (config.getTocTemplatePath() != null) {
				verbose( "TOC template path is \"" + config.getTocTemplatePath() + "\"" );
			}
		}
		
		debug( "Publishing PDF book \"" + config.getOutputBookFile() + "\" from files in source folder \"" + config.getInputDir() + "\" ..." );
		
		BookPublisher bookPublisher = new BookPublisher();
		bookPublisher.setConfig( config );
		bookPublisher.publish( config.getInputDir(), config.getOutputDir(), config.getOutputBookFile() );
		
		System.out.println( "Finished publishing book \"" + config.getOutputBookFile() + "\"." );		
	}
		
		
	public static void main(String[] args) throws Exception {
		
		try {

			BookPublisherCLI cli = new BookPublisherCLI();
			cli.run( args );

		} catch (Exception e) {
			// String msg = "Error publishing book \"" + config.getOutputBookFile() + "\" : " + e.getMessage();
			String msg = "Error publishing book : " + e.getMessage();
			error( msg, e );
			throw new Exception( msg, e );
		}
		
		System.exit(0);
	}
	

    private void showHelp() {
        HelpFormatter helpFormatter = new HelpFormatter( );
        helpFormatter.setWidth( CLI_USAGE_HELP_WIDTH );
        helpFormatter.printHelp( CLI_USAGE, CLI_HEADER, CLI_OPTIONS, CLI_FOOTER );
		System.exit(SystemExitValues.EXIT_CODE_TOO_FEW_ARGS);
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

