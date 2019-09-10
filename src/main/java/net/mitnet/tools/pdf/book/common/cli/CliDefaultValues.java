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

package net.mitnet.tools.pdf.book.common.cli;

import net.mitnet.tools.pdf.book.pdf.util.PdfPageValues;

/**
 * Command Line Interface (CLI) Options.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class CliDefaultValues {

	// public static final int DEFAULT_NUP = 1;
	public static final int DEFAULT_NUP = 2;
	
	public static final String DEFAULT_PAGE_ORIENTATION = PdfPageValues.PAGE_ORIENTATION_PORTRAIT;
	
	public static final String DEFAULT_PAGE_SIZE = PdfPageValues.PAGE_SIZE_ISO_A4_STRING;
	
	public static final String DEFAULT_TOC_TEMPLATE_PATH = "reports/templates/toc-template.odt";

}
