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
import net.mitnet.tools.pdf.book.pdf.builder.PdfBookBuilderConfig;
import net.mitnet.tools.pdf.book.pdf.event.PdfPageEventLogger;
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
import com.lowagie.text.pdf.PdfPageEvent;


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
	
	private static final Options OPTIONS = initOptions();
	

	private static Options initOptions() {
		Options options = new Options();
		options.addOption(CliConstants.OPTION_SOURCE_DIR);
		options.addOption(CliConstants.OPTION_OUTPUT_BOOK_FILE);
		options.addOption(CliConstants.OPTION_PAGE_SIZE);
		options.addOption(CliConstants.OPTION_VERBOSE);
		options.addOption(CliConstants.OPTION_META_TITLE);
		options.addOption(CliConstants.OPTION_META_AUTHOR);
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

		if (!commandLineHelper.hasOption(CliConstants.OPTION_OUTPUT_BOOK_FILE)) {
			System.err.println("Must specify " + CliConstants.OPTION_OUTPUT_BOOK_FILE.getDescription());
			showHelp();
			System.exit(CliConstants.EXIT_CODE_ERROR);
		}
		File outputBookFile = commandLineHelper.getOptionValueAsFile(CliConstants.OPTION_OUTPUT_BOOK_FILE);

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

			PdfBookBuilderConfig config = new PdfBookBuilderConfig();
			
			ProgressMonitor progressMonitor = new ConsoleProgressMonitor();
			config.setProgressMonitor(progressMonitor);
			
			PdfPageEvent pdfPageEventListener = new PdfPageEventLogger();
			config.setPdfPageEventListener(pdfPageEventListener);
			
			config.setPageSize(pageSize);
			config.setMetaTitle(metaTitle);
			config.setMetaAuthor(metaAuthor);
			config.setVerbose(verbose);
			
			config.setBuildTocEnabled(true);
			TocBuilder tocBuilder = new TocBuilder();
			config.setTocRowChangeListener(tocBuilder);
			
			PdfBookBuilder pdfBookBuilder = new PdfBookBuilder();
			pdfBookBuilder.setConfig(config);
			pdfBookBuilder.buildBook(sourceDir, outputBookFile);
			
			if (verbose) {
				Toc toc = tocBuilder.getToc();
				System.out.println( "-- Output PDF Table Of Contents is " + toc );
				System.out.println( "-- Output PDF Table Of Contents contains " + toc.getTocRowCount() + " entries" );
				TocTracer.traceToc(toc);
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
		System.exit(CliConstants.EXIT_CODE_TOO_FEW_ARGS);
	}

}
