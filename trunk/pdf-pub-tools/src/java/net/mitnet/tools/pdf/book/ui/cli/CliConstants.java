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

package net.mitnet.tools.pdf.book.ui.cli;

import org.apache.commons.cli.Option;


/**
 * Command Line Interface (CLI) Constants.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class CliConstants {
	
	public static final Option OPTION_DEBUG = new Option("d", "debug",
			false, "debug");

	public static final Option OPTION_SOURCE_DIR = new Option("indir",
			"input-dir", true, "source directory");

	public static final Option OPTION_OUTPUT_DIR = new Option("outdir",
			"output-dir", true, "output directory");

	public static final Option OPTION_OUTPUT_BOOK_FILE = new Option("outbook",
			"output-book-file", true, "output book file");

	public static final Option OPTION_PAGE_SIZE = new Option("ps", "page-size",
			true, "output page size [usletter,a4]");

	public static final Option OPTION_VERBOSE = new Option("v", "verbose",
			false, "verbose");

	public static final Option OPTION_META_TITLE = new Option("mt",
			"meta-title", false, "PDF meta-data title");

	public static final Option OPTION_META_AUTHOR = new Option("ma",
			"meta-author", false, "PDF meta-data author");
	
	public static final Option OPTION_META_VERSION_ID = new Option("mvid",
			"meta-version-id", false, "PDF meta-data version ID");	

	public static final Option OPTION_OPEN_OFFICE_HOST = new Option("ooh",
			"open-office-host", true, "OpenOffice host");

	public static final Option OPTION_OPEN_OFFICE_PORT = new Option("oop",
			"open-office-port", true, "OpenOffice port");
	
	public static final Option OPTION_OUTPUT_FORMAT = 
		new Option("f","output-format", true, "output format (e.g. pdf)");

	public static final Option OPTION_OUTPUT_REPORT_FILE = new Option("outreport",
			"output-report-file-file", true, "output report file");
	
	public static final Option OPTION_SOURCE_TEMPLATE_FILE = new Option("intemplate",
			"input-template-file", true, "input source template file");

	public static final Option OPTION_SOURCE_DATA_FILE = new Option("indata",
			"input-data-file", true, "input source data file");
	
	public static final Option OPTION_TOC_TEMPLATE_PATH = 
		new Option("tocp","toc-template-path", true, "TOC tamplte path (e.g. /opt/toc-template.odt)");

	public static final int EXIT_CODE_ERROR = 1;

	public static final int EXIT_CODE_TOO_FEW_ARGS = 255;
	
	public static final int EXIT_CODE_CONNECTION_FAILED = 1;

	public static final String PAGE_SIZE_US_LETTER_STRING = "LETTER";

	public static final String PAGE_SIZE_ISO_A4_STRING = "A4";

	private static final String DEFAULT_PAGE_SIZE = PAGE_SIZE_ISO_A4_STRING;

}
