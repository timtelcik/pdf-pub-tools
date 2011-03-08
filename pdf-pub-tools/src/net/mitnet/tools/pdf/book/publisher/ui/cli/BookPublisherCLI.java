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
	
	private static final File DEFAULT_OUTPUT_FILE = new File( "my-book" + PdfBookBuilder.PDF_FILE_EXTENSION );
	

	private static final Options OPTIONS = initOptions();
	
	private static Options initOptions() {
		Options options = new Options();
		options.addOption(CliConstants.OPTION_SOURCE_DIR);
		options.addOption(CliConstants.OPTION_OUTPUT_DIR);
		options.addOption(CliConstants.OPTION_OUTPUT_BOOK_FILE);
		options.addOption(CliConstants.OPTION_PAGE_SIZE);
		options.addOption(CliConstants.OPTION_VERBOSE);
		options.addOption(CliConstants.OPTION_META_TITLE);
		options.addOption(CliConstants.OPTION_META_AUTHOR);
		options.addOption(CliConstants.OPTION_OPEN_OFFICE_HOST);
		options.addOption(CliConstants.OPTION_OPEN_OFFICE_PORT);
		return options;
	}

	public static void main(String[] arguments) throws Exception {

		CommandLineParser commandLineParser = new PosixParser();
		CommandLine commandLine = commandLineParser.parse(OPTIONS, arguments);
		CommandLineHelper commandLineHelper = new CommandLineHelper( commandLine );
		
		if (!commandLineHelper.hasOption(CliConstants.OPTION_SOURCE_DIR)) {
			System.err.println("Must specify " + CliConstants.OPTION_SOURCE_DIR.getDescription());
			showHelp();
			System.exit(CliConstants.EXIT_CODE_ERROR);
		}
		File sourceDir = commandLineHelper.getOptionValueAsFile(CliConstants.OPTION_SOURCE_DIR);
		
		if (!commandLineHelper.hasOption(CliConstants.OPTION_OUTPUT_DIR)) {
			System.err.println("Must specify " + CliConstants.OPTION_OUTPUT_DIR.getDescription());
			showHelp();
			System.exit(CliConstants.EXIT_CODE_ERROR);
		}
		File outputDir = commandLineHelper.getOptionValueAsFile(CliConstants.OPTION_OUTPUT_DIR);

		if (!commandLineHelper.hasOption(CliConstants.OPTION_OUTPUT_BOOK_FILE)) {
			System.err.println("Must specify " + CliConstants.OPTION_OUTPUT_BOOK_FILE.getDescription());
			showHelp();
			System.exit(CliConstants.EXIT_CODE_ERROR);
		}
		File outputBookFile = commandLineHelper.getOptionValueAsFile(CliConstants.OPTION_OUTPUT_BOOK_FILE);;
		
		OpenOfficeServerContext serverContext = new OpenOfficeServerContext();

		String openOfficeHost = SocketOpenOfficeConnection.DEFAULT_HOST;
		if (commandLineHelper.hasOption(CliConstants.OPTION_OPEN_OFFICE_HOST)) {
			openOfficeHost = commandLineHelper.getOptionValue(CliConstants.OPTION_OPEN_OFFICE_HOST);
			serverContext.setHost(openOfficeHost);
		}

		int openOfficePort = SocketOpenOfficeConnection.DEFAULT_PORT;
		if (commandLineHelper.hasOption(CliConstants.OPTION_OPEN_OFFICE_PORT)) {
			openOfficePort = commandLineHelper.getOptionValueAsInt(CliConstants.OPTION_OPEN_OFFICE_PORT);
			serverContext.setPort(openOfficePort);
		}

		Rectangle pageSize = PageSize.A4;
		if (commandLineHelper.hasOption(CliConstants.OPTION_PAGE_SIZE)) {
			String pageSizeString = commandLineHelper.getOptionValue(CliConstants.OPTION_PAGE_SIZE);
			if (!StringUtils.isEmpty(pageSizeString)) {
				if (CliConstants.PAGE_SIZE_US_LETTER_STRING.equalsIgnoreCase(pageSizeString)) {
					pageSize = PageSize.LETTER;
				}
			}
		}

		boolean verbose = false;
		if (commandLineHelper.hasOption(CliConstants.OPTION_VERBOSE)) {
			verbose = true;
		}
		
		String metaTitle = FilenameUtils.getBaseName( outputBookFile.getName() );
		if (metaTitle != null) {
			metaTitle = metaTitle.toUpperCase();
		}
		if (commandLineHelper.hasOption(CliConstants.OPTION_META_TITLE)) {
			metaTitle = commandLineHelper.getOptionValue(CliConstants.OPTION_META_TITLE);
		}
		
		String metaAuthor = System.getProperty( "user.name" );
		if (commandLineHelper.hasOption(CliConstants.OPTION_META_AUTHOR)) {
			metaAuthor = commandLineHelper.getOptionValue(CliConstants.OPTION_META_AUTHOR);
		}

		try {

			if (verbose) {
				System.out.println( "-- Source dir is " + sourceDir );
				System.out.println( "-- Output dir is " + outputDir );
				System.out.println( "-- Output file is " + outputBookFile );
				System.out.println( "-- Page size is " + pageSize );
				System.out.println( "-- Publishing PDF book " + outputBookFile + " ...");
			}

			ProgressMonitor progressMonitor = new ConsoleProgressMonitor();
			BookPublisher bookPublisher = new BookPublisher(serverContext, pageSize);
			bookPublisher.setMetaTitle(metaTitle);
			bookPublisher.setMetaAuthor(metaAuthor);
			bookPublisher.setVerbose(verbose);
			bookPublisher.publish( sourceDir, outputDir, outputBookFile, progressMonitor );

		} catch (Exception e) {
			e.printStackTrace(System.err);
			String msg = "Error publishing book " + outputBookFile + " : " + e.getMessage();
			System.err.println( msg );
			throw new Exception( msg, e );
		}
		
		if (verbose) {
			System.out.println( "-- Finished publishing book " + outputBookFile + ".");
		}
	}

	private static void showHelp() {
		// TODO - fix args
		String syntax = BookPublisherCLI.class.getName()
				+ " [options] -indir <input-dir> -ooutdir <output-dir> [-of <output-file>] [-p <page-size>] \n";
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(syntax, OPTIONS);
		System.exit(CliConstants.EXIT_CODE_TOO_FEW_ARGS);
	}

}
