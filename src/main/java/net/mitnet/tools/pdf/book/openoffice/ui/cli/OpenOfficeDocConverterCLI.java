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

package net.mitnet.tools.pdf.book.openoffice.ui.cli;

import java.io.File;

import net.mitnet.tools.pdf.book.common.cli.CliOptions;
import net.mitnet.tools.pdf.book.common.cli.CommandLineHelper;
import net.mitnet.tools.pdf.book.common.cli.ConsoleProgressMonitor;
import net.mitnet.tools.pdf.book.common.cli.SystemExitValues;
import net.mitnet.tools.pdf.book.openoffice.converter.OpenOfficeDocConverter;
import net.mitnet.tools.pdf.book.util.ProgressMonitor;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;



/**
 * Open Office Document Converter Command Line Interface (CLI).
 * 
 * This program converts Open Office documents from a source directory
 * to another format supported by Open Office in an output directory.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see com.artofsolving.jodconverter.DocumentConverter
 * @see com.artofsolving.jodconverter.cli.ConvertDocument
 * @see com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter
 */
public class OpenOfficeDocConverterCLI {

	private static final Options OPTIONS = initOptions();
	
	private static final String DEFAULT_OUTPUT_FORMAT = "pdf";

	private static Options initOptions() {
		Options options = new Options();
		options.addOption(CliOptions.OPTION_INPUT_DIR);
		options.addOption(CliOptions.OPTION_OUTPUT_DIR);
		options.addOption(CliOptions.OPTION_OUTPUT_FORMAT);
		options.addOption(CliOptions.OPTION_OPEN_OFFICE_HOST);
		options.addOption(CliOptions.OPTION_OPEN_OFFICE_PORT);
		options.addOption(CliOptions.OPTION_DEBUG);
		options.addOption(CliOptions.OPTION_VERBOSE);
		return options;
	}

	public static void main(String[] arguments) throws Exception {

		CommandLineParser commandLineParser = new PosixParser();
		CommandLine commandLine = commandLineParser.parse(OPTIONS, arguments);
		CommandLineHelper commandLineHelper = new CommandLineHelper(commandLine);
		
		if (!commandLineHelper.hasOption(CliOptions.OPTION_INPUT_DIR)) {
			System.err.println("Must specify " + CliOptions.OPTION_INPUT_DIR.getDescription());
			showHelp();
		}
		File sourceDir = commandLineHelper.getOptionValueAsFile(CliOptions.OPTION_INPUT_DIR);

		if (!commandLineHelper.hasOption(CliOptions.OPTION_OUTPUT_DIR)) {
			System.err.println("Must specify " + CliOptions.OPTION_OUTPUT_DIR.getDescription());
			showHelp();
		}
		File outputDir = commandLineHelper.getOptionValueAsFile(CliOptions.OPTION_OUTPUT_DIR);

		int openOfficePort = 8100;
		if (commandLineHelper.hasOption(CliOptions.OPTION_OPEN_OFFICE_PORT)) {
			openOfficePort = commandLineHelper.getOptionValueAsInt(CliOptions.OPTION_OPEN_OFFICE_PORT);
		}

		String openOfficeHost = "localhost";
		if (commandLineHelper.hasOption(CliOptions.OPTION_OPEN_OFFICE_HOST)) {
			openOfficeHost = commandLineHelper.getOptionValue(CliOptions.OPTION_OPEN_OFFICE_HOST);
		}

		String outputFormat = DEFAULT_OUTPUT_FORMAT;
		if (commandLineHelper.hasOption(CliOptions.OPTION_OUTPUT_FORMAT)) {
			outputFormat = commandLineHelper.getOptionValue(CliOptions.OPTION_OUTPUT_FORMAT);
		}

		boolean verbose = false;
		if (commandLineHelper.hasOption(CliOptions.OPTION_VERBOSE)) {
			verbose = true;
		}

		//OpenOfficeConnection connection = new SocketOpenOfficeConnection(openOfficeHost, openOfficePort);
		OfficeManager officeManager = new DefaultOfficeManagerConfiguration().setPortNumber(8100).buildOfficeManager();
	    
	    
		try {
			if (verbose) {
				System.out.println("-- connecting to OpenOffice.org host "
						+ openOfficeHost + " on port " + openOfficePort);
			}
			//connection.connect();
			officeManager.start();
		} catch (Exception ce) {
			ce.printStackTrace(System.err);
			System.err.println(ce.getMessage());
			String msg = "ERROR: connection failed. Please make sure OpenOffice.org is running on host " 
				+ openOfficeHost 
				+ " and listening on port " 
				+ openOfficePort + ".";
			System.err.println(msg);
			System.exit(SystemExitValues.EXIT_CODE_CONNECTION_FAILED);
		}
		try {
			if (verbose) {
				System.out.println( "Source dir is " + sourceDir );
				System.out.println( "Output dir is " + outputDir );
				System.out.println( "Output format is " + outputFormat );
				System.out.println( "Converting files ...");
			}
			ProgressMonitor progressMonitor = new ConsoleProgressMonitor();
			OpenOfficeDocConverter docConverter = new OpenOfficeDocConverter(openOfficeHost,openOfficePort);
			docConverter.setVerboseEnabled(verbose);
			docConverter.setProgressMonitor(progressMonitor);
			docConverter.convertDocuments( sourceDir, outputDir, outputFormat );
		} finally {
			if (verbose) {
				System.out.println("-- disconnecting");
			}
			//connection.disconnect();
			officeManager.stop();
			System.out.println( "Finished converting files.");
		}
	}

	private static void showHelp() {
		String syntax = OpenOfficeDocConverterCLI.class.getName()
				+ " [options] -indir <input-dir> -outdir <output-dir>\n";
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(syntax, OPTIONS);
		System.exit(SystemExitValues.EXIT_CODE_TOO_FEW_ARGS);
	}

}
