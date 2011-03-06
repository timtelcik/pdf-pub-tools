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

import net.mitnet.tools.pdf.book.openoffice.converter.OpenOfficeDocConverter;
import net.mitnet.tools.pdf.book.openoffice.net.OpenOfficeServerContext;
import net.mitnet.tools.pdf.book.openoffice.ui.cli.OpenOfficeDocConverterCLI;
import net.mitnet.tools.pdf.book.pdf.builder.PdfBookBuilder;
import net.mitnet.tools.pdf.book.pdf.builder.ui.cli.PdfBookBuilderCLI;
import net.mitnet.tools.pdf.book.publisher.BookPublisher;
import net.mitnet.tools.pdf.book.ui.cli.CommandLineHelper;
import net.mitnet.tools.pdf.book.ui.cli.ConsoleProgressMonitor;
import net.mitnet.tools.pdf.book.util.ProgressMonitor;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
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
	
	private static final String PAGE_SIZE_US_LETTER_STRING = "LETTER";
	private static final String PAGE_SIZE_ISO_A4_STRING = "A4";
	private static final String DEFAULT_PAGE_SIZE = PAGE_SIZE_ISO_A4_STRING;
	private static final File DEFAULT_OUTPUT_FILE = new File( "my-book" + PdfBookBuilder.PDF_FILE_EXTENSION );
	

	private static final Option OPTION_SOURCE_DIR = 
		new Option("i","source-dir", true, "source directory");
	
	private static final Option OPTION_OUTPUT_DIR = 
		new Option("o","output-dir", true, "output directory");
	
	private static final Option OPTION_OUTPUT_BOOK_FILE = 
		new Option("b","output-book-file", true, "output book file");
	
	private static final Option OPTION_PAGE_SIZE = 
		new Option("ps","page-size", true, "output page size [usletter,a4]");
	
	private static final Option OPTION_VERBOSE = 
		new Option("v", "verbose", false, "verbose");
	
	private static final Option OPTION_META_TITLE = 
		new Option("mt", "meta-title", false, "PDF meta-data title");
	
	private static final Option OPTION_META_AUTHOR = 
		new Option("ma", "meta-author", false, "PDF meta-data author");
	
	private static final Option OPTION_OPEN_OFFICE_HOST = 
		new Option("ooh","open-office-host", true, "OpenOffice host");
	
	private static final Option OPTION_OPEN_OFFICE_PORT = 
		new Option("oop","open-office-port", true, "OpenOffice port");
	
	private static final Options OPTIONS = initOptions();
	
	private static final int EXIT_CODE_ERROR = 1;
	private static final int EXIT_CODE_TOO_FEW_ARGS = 255;

	private static Options initOptions() {
		Options options = new Options();
		options.addOption(OPTION_SOURCE_DIR);
		options.addOption(OPTION_OUTPUT_DIR);
		options.addOption(OPTION_OUTPUT_BOOK_FILE);
		options.addOption(OPTION_PAGE_SIZE);
		options.addOption(OPTION_VERBOSE);
		options.addOption(OPTION_META_TITLE);
		options.addOption(OPTION_META_AUTHOR);
		options.addOption(OPTION_OPEN_OFFICE_HOST);
		options.addOption(OPTION_OPEN_OFFICE_PORT);
		return options;
	}

	public static void main(String[] arguments) throws Exception {

		CommandLineParser commandLineParser = new PosixParser();
		CommandLine commandLine = commandLineParser.parse(OPTIONS, arguments);
		CommandLineHelper commandLineHelper = new CommandLineHelper( commandLine );
		
		if (!commandLineHelper.hasOption(OPTION_SOURCE_DIR)) {
			System.err.println("Must specify a source directory");
			showHelp();
			System.exit(EXIT_CODE_ERROR);
		}
		File sourceDir = commandLineHelper.getOptionValueAsFile(OPTION_SOURCE_DIR);
		
		if (!commandLineHelper.hasOption(OPTION_OUTPUT_DIR)) {
			System.out.println("Must specify an output directory");
			showHelp();
			System.exit(EXIT_CODE_ERROR);
		}
		File outputDir = commandLineHelper.getOptionValueAsFile(OPTION_OUTPUT_DIR);

		File outputBookFile = DEFAULT_OUTPUT_FILE;
		if (commandLineHelper.hasOption(OPTION_OUTPUT_BOOK_FILE)) {
			outputBookFile = commandLineHelper.getOptionValueAsFile(OPTION_OUTPUT_BOOK_FILE);
		}
		
		OpenOfficeServerContext serverContext = new OpenOfficeServerContext();

		String openOfficeHost = SocketOpenOfficeConnection.DEFAULT_HOST;
		if (commandLineHelper.hasOption(OPTION_OPEN_OFFICE_HOST)) {
			openOfficeHost = commandLineHelper.getOptionValue(OPTION_OPEN_OFFICE_HOST);
			serverContext.setHost(openOfficeHost);
		}

		int openOfficePort = SocketOpenOfficeConnection.DEFAULT_PORT;
		if (commandLineHelper.hasOption(OPTION_OPEN_OFFICE_PORT)) {
			openOfficePort = commandLineHelper.getOptionValueAsInt(OPTION_OPEN_OFFICE_PORT);
			serverContext.setPort(openOfficePort);
		}

		Rectangle pageSize = PageSize.A4;
		if (commandLineHelper.hasOption(OPTION_PAGE_SIZE)) {
			String pageSizeString = commandLineHelper.getOptionValue(OPTION_PAGE_SIZE);
			if (!StringUtils.isEmpty(pageSizeString)) {
				if (PAGE_SIZE_US_LETTER_STRING.equalsIgnoreCase(pageSizeString)) {
					pageSize = PageSize.LETTER;
				}
			}
		}

		boolean verbose = false;
		if (commandLineHelper.hasOption(OPTION_VERBOSE)) {
			verbose = true;
		}
		
		String metaTitle = FilenameUtils.getBaseName( outputBookFile.getName() );
		if (metaTitle != null) {
			metaTitle = metaTitle.toUpperCase();
		}
		if (commandLineHelper.hasOption(OPTION_META_TITLE)) {
			metaTitle = commandLineHelper.getOptionValue(OPTION_META_TITLE);
		}
		
		String metaAuthor = System.getProperty( "user.name" );
		if (commandLineHelper.hasOption(OPTION_META_AUTHOR)) {
			metaAuthor = commandLineHelper.getOptionValue(OPTION_META_AUTHOR);
		}

		if (verbose) {
			System.out.println("-- Building PDF training book " + outputBookFile);
		}

		try {

			if (verbose) {
				System.out.println( "-- Source dir is " + sourceDir );
				System.out.println( "-- Output dir is " + outputDir );
				System.out.println( "-- Output file is " + outputBookFile );
				System.out.println( "-- Page size is " + pageSize );
				System.out.println( "-- Publishing PDF training book " + outputBookFile + " ...");
			}

			ProgressMonitor progressMonitor = new ConsoleProgressMonitor();
			BookPublisher bookPublisher = new BookPublisher(serverContext, pageSize);
			bookPublisher.setMetaTitle(metaTitle);
			bookPublisher.setMetaAuthor(metaAuthor);
			bookPublisher.setVerbose(verbose);
			bookPublisher.publish( sourceDir, outputDir, outputBookFile, progressMonitor );

		} catch (Exception e) {
			e.printStackTrace(System.err);
			String msg = "Error publishing PDF training book " + outputBookFile + " : " + e.getMessage();
			System.err.println( msg );
			throw new Exception( msg, e );
		}
		
		if (verbose) {
			System.out.println( "-- Finished publishing PDF training book " + outputBookFile + ".");
		}
	}

	private static void showHelp() {
		String syntax = BookPublisherCLI.class.getName()
				+ " [options] -i <input-dir> -o <output-dir> [-of <output-file>] [-p <page-size>] \n";
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(syntax, OPTIONS);
		System.exit(EXIT_CODE_TOO_FEW_ARGS);
	}

}
