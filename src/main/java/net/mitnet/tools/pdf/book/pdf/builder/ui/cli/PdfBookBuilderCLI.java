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

package net.mitnet.tools.pdf.book.pdf.builder.ui.cli;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPageEvent;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FilenameUtils;

import net.mitnet.tools.pdf.book.common.cli.CliDefaultValues;
import net.mitnet.tools.pdf.book.common.cli.CliOptions;
import net.mitnet.tools.pdf.book.common.cli.CommandLineHelper;
import net.mitnet.tools.pdf.book.common.cli.ConsoleProgressMonitor;
import net.mitnet.tools.pdf.book.common.cli.SystemExitValues;
import net.mitnet.tools.pdf.book.conf.ConfigHelper;
import net.mitnet.tools.pdf.book.model.toc.Toc;
import net.mitnet.tools.pdf.book.model.toc.TocBuilder;
import net.mitnet.tools.pdf.book.model.toc.TocTracer;
import net.mitnet.tools.pdf.book.pdf.builder.PdfBookBuilder;
import net.mitnet.tools.pdf.book.pdf.builder.PdfBookBuilderConfig;
import net.mitnet.tools.pdf.book.pdf.event.PdfPageEventLogger;
import net.mitnet.tools.pdf.book.util.ProgressMonitor;


/**
 * PDF Book Builder Command Line Interface (CLI).
 * 
 * This program builds a list of PDF files into a 2-up PDF book.
 * 
 * TODO - review iText NUP
 * TODO - review tracing and debugging.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class PdfBookBuilderCLI {
	
	private static final String CLI_USAGE = 
			" [options] -i <input-dir> -o <output-file> [-p <page-size>] \n";
	
	private static final String CLI_HEADER =
			"PdfBookBuilderCLI - PDF Book Builder Tool (c) 2010-2013 Tim Telcik <telcik@gmail.com>";
	
	private static final String CLI_FOOTER =
			"For more instructions, see the PDF Publishing Tools project at \"https://code.google.com/p/pdf-pub-tools/\"";
	
	private static final int CLI_USAGE_HELP_WIDTH = 80;
	
	public static final int DEFAULT_NUP = CliDefaultValues.DEFAULT_NUP;
	
	private static final Options CLI_OPTIONS = initOptions();
	

	private static Options initOptions() {
		Options options = new Options();
		options.addOption(CliOptions.OPTION_INPUT_DIR);
		options.addOption(CliOptions.OPTION_OUTPUT_BOOK_FILE);
		options.addOption(CliOptions.OPTION_PAGE_SIZE);
		options.addOption(CliOptions.OPTION_DEBUG);
		options.addOption(CliOptions.OPTION_VERBOSE);
		options.addOption(CliOptions.OPTION_META_TITLE);
		options.addOption(CliOptions.OPTION_META_AUTHOR);
		options.addOption(CliOptions.OPTION_NUP);
		return options;
	}
	
	
	public PdfBookBuilderConfig parseConfig( String[] args ) throws Exception {
		
		PdfBookBuilderConfig config = new PdfBookBuilderConfig();
		
		CommandLineParser commandLineParser = new PosixParser();
		CommandLine commandLine = commandLineParser.parse( CLI_OPTIONS, args );
		CommandLineHelper commandLineHelper = new CommandLineHelper( commandLine );
		
		if (!commandLineHelper.hasOption(CliOptions.OPTION_INPUT_DIR)) {
			System.err.println("Must specify " + CliOptions.OPTION_INPUT_DIR.getDescription());
			showHelp();
		}
		File inputDir = commandLineHelper.getOptionValueAsFile(CliOptions.OPTION_INPUT_DIR);
		config.setInputDir(inputDir);

		if (!commandLineHelper.hasOption(CliOptions.OPTION_OUTPUT_BOOK_FILE)) {
			System.err.println("Must specify " + CliOptions.OPTION_OUTPUT_BOOK_FILE.getDescription());
			showHelp();
		}
		File outputBookFile = commandLineHelper.getOptionValueAsFile(CliOptions.OPTION_OUTPUT_BOOK_FILE);
		config.setOutputBookFile(outputBookFile);

		Rectangle pageSize = ConfigHelper.parsePageSize(commandLineHelper);
		config.setPageSize(pageSize);

		boolean debug = false;
		if (commandLineHelper.hasOption(CliOptions.OPTION_DEBUG)) {
			debug = true;
		}
		config.setDebugEnabled(debug);

		boolean verbose = false;
		if (commandLineHelper.hasOption(CliOptions.OPTION_VERBOSE)) {
			verbose = true;
		}
		config.setVerboseEnabled(verbose);
		
		String metaTitle = FilenameUtils.getBaseName( outputBookFile.getName() );
		if (metaTitle != null) {
			metaTitle = metaTitle.toUpperCase();
		}
		if (commandLineHelper.hasOption(CliOptions.OPTION_META_TITLE)) {
			metaTitle = commandLineHelper.getOptionValue(CliOptions.OPTION_META_TITLE);
		}
		config.setMetaTitle(metaTitle);
		
		String metaAuthor = System.getProperty( "user.name" );
		if (commandLineHelper.hasOption(CliOptions.OPTION_META_AUTHOR)) {
			metaAuthor = commandLineHelper.getOptionValue(CliOptions.OPTION_META_AUTHOR);
		}
		config.setMetaAuthor(metaAuthor);
		
		int nup = CliDefaultValues.DEFAULT_NUP;
		if (commandLineHelper.hasOption(CliOptions.OPTION_NUP)) {
			nup = commandLineHelper.getOptionValueAsInt(CliOptions.OPTION_NUP);
		}
		config.setNup(nup);
			
		PdfPageEvent pdfPageEventListener = new PdfPageEventLogger();
		config.setPdfPageEventListener(pdfPageEventListener);
		
		config.setBuildTocEnabled(true);
		TocBuilder tocBuilder = new TocBuilder();
		// config.setTocBuilder(tocBuilder);
		config.setTocRowChangeListener(tocBuilder);
		
		return config;
	}
	
	
	public void run( String[] args ) throws Exception {
		
		PdfBookBuilderConfig config = parseConfig( args );

		System.out.println( "Building PDF book \"" + config.getOutputBookFile() + "\" from files in source folder \"" + config.getInputDir() + "\" ..." );

		try {

			if (config.isVerboseEnabled()) {
				verbose( "Source dir is \"" +  config.getInputDir() + "\"" );
				verbose( "Output book file is \"" + config.getOutputBookFile() + "\"" );
				verbose( "Page size is " + config.getPageSize() );
				verbose( "Building PDF book \"" + config.getOutputBookFile() + "\" ...");
			}

			if (config.isVerboseEnabled()) {
				ProgressMonitor progressMonitor = new ConsoleProgressMonitor();
				config.setProgressMonitor(progressMonitor);
			}
			
			PdfBookBuilder pdfBookBuilder = new PdfBookBuilder();
			pdfBookBuilder.setConfig(config);
			pdfBookBuilder.buildBook(config.getInputDir(), config.getOutputBookFile());
			
			TocBuilder tocBuilder = (TocBuilder) config.getTocRowChangeListener();
			if (tocBuilder != null) {
				Toc toc = tocBuilder.getToc();
				if (config.isVerboseEnabled()) {
					// System.out.println( "Output PDF Table Of Contents is " + toc );
					System.out.println( "Output PDF Table Of Contents contains " + toc.getTocRowCount() + " entries" );
				}
				if (config.isDebugEnabled()) {
					TocTracer.traceToc(toc);					
				}
			}

			// TODO - output TOC data ???
			/*
			if (config.isBuildTocEnabled()) {
				// TODO - create TOC PDF page using collected TOC data
				// Map tocTemplateData = buildTocTemplateData( toc );
				// OpenOfficeReportBuilder reportBuilder = new OpenOfficeReportBuilder();
				// reportBuilder.buildReport( templateFile, tocTemplateData, outputFile );
				
				// TODO - merge TOC PDF page with PDF book
			}
			*/

		} catch (Exception e) {
			e.printStackTrace(System.err);
			String msg = "Error building PDF book " + config.getOutputBookFile() + " : " + e.getMessage();
			System.err.println( msg );
			throw new Exception( msg, e );
		}

		System.out.println( "Finished building PDF book \"" + config.getOutputBookFile() + "\".");
	}

	
	public static void main( String[] args ) throws Exception {
		
		PdfBookBuilderCLI cli = new PdfBookBuilderCLI();
		cli.run( args );

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

}
