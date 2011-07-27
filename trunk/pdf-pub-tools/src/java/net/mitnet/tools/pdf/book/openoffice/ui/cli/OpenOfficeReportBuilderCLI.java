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

package net.mitnet.tools.pdf.book.openoffice.ui.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import net.mitnet.tools.pdf.book.openoffice.reports.OpenOfficeReportBuilder;
import net.mitnet.tools.pdf.book.ui.cli.CliConstants;
import net.mitnet.tools.pdf.book.ui.cli.CommandLineHelper;
import net.mitnet.tools.pdf.book.ui.cli.ConsoleProgressMonitor;
import net.mitnet.tools.pdf.book.util.ProgressMonitor;
import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateFactory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FilenameUtils;

import freemarker.ext.dom.NodeModel;
import freemarker.template.Configuration;


/**
 * Open Office Report Builder Command Line Interface (CLI).
 * 
 * This program creates a report from a template and data source.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see OpenOfficeReportBuilder
 */
public class OpenOfficeReportBuilderCLI {

	private static final Options OPTIONS = initOptions();
	
	private static final String DEFAULT_OUTPUT_FORMAT = "pdf";

	private static Options initOptions() {
		Options options = new Options();
		options.addOption(CliConstants.OPTION_SOURCE_DIR);
		options.addOption(CliConstants.OPTION_OUTPUT_DIR);
		options.addOption(CliConstants.OPTION_OUTPUT_FORMAT);
		options.addOption(CliConstants.OPTION_OPEN_OFFICE_HOST);
		options.addOption(CliConstants.OPTION_OPEN_OFFICE_PORT);
		options.addOption(CliConstants.OPTION_VERBOSE);
		return options;
	}

	public static void main(String[] arguments) throws Exception {

		CommandLineParser commandLineParser = new PosixParser();
		CommandLine commandLine = commandLineParser.parse(OPTIONS, arguments);
		CommandLineHelper commandLineHelper = new CommandLineHelper(commandLine);
		
		if (!commandLineHelper.hasOption(CliConstants.OPTION_SOURCE_TEMPLATE_FILE)) {
			System.err.println("Must specify " + CliConstants.OPTION_SOURCE_TEMPLATE_FILE.getDescription());
			showHelp();
			System.exit(CliConstants.EXIT_CODE_ERROR);
		}
		File sourceTemplateFile = commandLineHelper.getOptionValueAsFile(CliConstants.OPTION_SOURCE_TEMPLATE_FILE);

		if (!commandLineHelper.hasOption(CliConstants.OPTION_SOURCE_DATA_FILE)) {
			System.err.println("Must specify " + CliConstants.OPTION_SOURCE_DATA_FILE.getDescription());
			showHelp();
			System.exit(CliConstants.EXIT_CODE_ERROR);
		}
		File sourceDataFile = commandLineHelper.getOptionValueAsFile(CliConstants.OPTION_SOURCE_DATA_FILE);
		
		if (!commandLineHelper.hasOption(CliConstants.OPTION_OUTPUT_REPORT_FILE)) {
			System.err.println("Must specify " + CliConstants.OPTION_OUTPUT_REPORT_FILE.getDescription());
			showHelp();
			System.exit(CliConstants.EXIT_CODE_ERROR);
		}
		File outputReportFile = commandLineHelper.getOptionValueAsFile(CliConstants.OPTION_OUTPUT_REPORT_FILE);

		boolean verbose = false;
		if (commandLineHelper.hasOption(CliConstants.OPTION_VERBOSE)) {
			verbose = true;
		}

		try {
			System.out.println( "Source template file is " + sourceTemplateFile );
			System.out.println( "Source data file is " + sourceDataFile );
			System.out.println( "Output report file is " + outputReportFile );
			System.out.println( "Building report ...");
			// ProgressMonitor progressMonitor = new ConsoleProgressMonitor();
			OpenOfficeReportBuilder reportBuilder = new OpenOfficeReportBuilder();
			reportBuilder.buildReport(sourceTemplateFile, sourceDataFile, outputReportFile);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.err.println( "Error building report: " + e.getMessage());
			System.exit(CliConstants.EXIT_CODE_ERROR);
		}
		System.out.println( "Finished converting files.");
	}
	
	private static void showHelp() {
		String syntax = OpenOfficeReportBuilderCLI.class.getName()
				+ " [options] -intemplate <input-template-file> -indata <input-data-file> -outreport <output-report-file>\n";
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(syntax, OPTIONS);
		System.exit(CliConstants.EXIT_CODE_TOO_FEW_ARGS);
	}

}
