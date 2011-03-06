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

package net.mitnet.tools.pdf.book.pdf.builder.ui.cli;

import java.io.File;

import net.mitnet.tools.pdf.book.model.toc.Toc;
import net.mitnet.tools.pdf.book.model.toc.TocBuilder;
import net.mitnet.tools.pdf.book.model.toc.TocTracer;
import net.mitnet.tools.pdf.book.pdf.builder.PdfBookBuilder;
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

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;


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
	
	private static final String PAGE_SIZE_US_LETTER_STRING = "LETTER";
	private static final String PAGE_SIZE_ISO_A4_STRING = "A4";
	private static final String DEFAULT_PAGE_SIZE = PAGE_SIZE_ISO_A4_STRING;

	private static final Option OPTION_SOURCE_DIR = 
		new Option("i","source-dir", true, "source directory");
	
	private static final Option OPTION_OUTPUT_BOOK_FILE = 
		new Option("b","output-book", true, "output book file");
	
	private static final Option OPTION_PAGE_SIZE = 
		new Option("ps","page-size", true, "output page size [usletter,a4]");
	
	private static final Option OPTION_VERBOSE = 
		new Option("v", "verbose", false, "verbose");
	
	private static final Option OPTION_META_TITLE = 
		new Option("mt", "meta-title", false, "PDF meta-data title");
	
	private static final Option OPTION_META_AUTHOR = 
		new Option("ma", "meta-author", false, "PDF meta-data author");

	
	private static final Options OPTIONS = initOptions();
	
	private static final int EXIT_CODE_ERROR = 1;
	private static final int EXIT_CODE_TOO_FEW_ARGS = 255;

	private static Options initOptions() {
		Options options = new Options();
		options.addOption(OPTION_SOURCE_DIR);
		options.addOption(OPTION_OUTPUT_BOOK_FILE);
		options.addOption(OPTION_PAGE_SIZE);
		options.addOption(OPTION_VERBOSE);
		options.addOption(OPTION_META_TITLE);
		options.addOption(OPTION_META_AUTHOR);
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

		if (!commandLineHelper.hasOption(OPTION_OUTPUT_BOOK_FILE)) {
			System.err.println("Must specify an output file");
			showHelp();
			System.exit(EXIT_CODE_ERROR);
		}
		File outputBookFile = commandLineHelper.getOptionValueAsFile(OPTION_OUTPUT_BOOK_FILE);

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
			System.out.println("-- Building PDF book " + outputBookFile);
		}

		try {

			if (verbose) {
				System.out.println( "-- Source dir is " + sourceDir );
				System.out.println( "-- Output file is " + outputBookFile );
				System.out.println( "-- Page size is " + pageSize );
				System.out.println( "-- Building PDF book " + outputBookFile + " ...");
			}

			ProgressMonitor progressMonitor = new ConsoleProgressMonitor();
			TocBuilder tocBuilder = new TocBuilder();
			PdfBookBuilder pdfBookBuilder = new PdfBookBuilder(pageSize);
			pdfBookBuilder.setMetaTitle(metaTitle);
			pdfBookBuilder.setMetaAuthor(metaAuthor);
			pdfBookBuilder.setVerbose(verbose);
			pdfBookBuilder.buildBook(sourceDir, outputBookFile, progressMonitor, tocBuilder);
			
			if (verbose) {
				Toc toc = tocBuilder.getToc();
				System.out.println( "-- Output PDF Table Of Contents is " + toc );
				System.out.println( "-- Output PDF Table Of Contents contains " + toc.getTocEntryCount() + " entries" );
				TocTracer.traceTableOfContents(toc);
			}

			// TODO - output TOC data ???
			/*
			if (isBuildTableOfContentsEnabled()) {
				// TODO - create TOC PDF page using collected TOC data
				// Map tocTemplateData = buildTocTemplateData( toc );
				// OpenOfficeReportBuilder reportBuilder = new OpenOfficeReportBuilder();
				// reportBuilder.buildReport( templateFile, tocTemplateData, outputFile );
				
				// TODO - merge TOC PDF page with PDF book
			}
			*/

		} catch (Exception e) {
			e.printStackTrace(System.err);
			String msg = "Error building PDF book " + outputBookFile + " : " + e.getMessage();
			System.err.println( msg );
			throw new Exception( msg, e );
		}


		if (verbose) {
			System.out.println( "-- Finished building PDF book " + outputBookFile + ".");
		}
	}

	private static void showHelp() {
		String syntax = PdfBookBuilderCLI.class.getName()
				+ " [options] -i <input-dir> -o <output-file> [-p <page-size>] \n";
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(syntax, OPTIONS);
		System.exit(EXIT_CODE_TOO_FEW_ARGS);
	}

}
