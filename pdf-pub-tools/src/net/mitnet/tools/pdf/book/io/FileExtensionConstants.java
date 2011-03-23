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

package net.mitnet.tools.pdf.book.io;


/**
 * File Extension Constants.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see http://en.wikipedia.org/wiki/OpenDocument
 */
public class FileExtensionConstants {
	
	public static final String FILE_EXTENSION_SEP = ".";

	public static final String OPEN_DOC_TEXT_EXTENSION_NAME = "odt";
	public static final String OPEN_DOC_TEXT_EXTENSION = FILE_EXTENSION_SEP + OPEN_DOC_TEXT_EXTENSION_NAME;
	
	public static final String OPEN_DOC_PRESENTATION_EXTENSION_NAME = "odp";
	public static final String OPEN_DOC_PRESENTATION_EXTENSION = FILE_EXTENSION_SEP + OPEN_DOC_PRESENTATION_EXTENSION_NAME;
	
	public static final String OPEN_DOC_SPREADSHEET_EXTENSION_NAME = "ods";
	public static final String OPEN_DOC_SPREADSHEET_EXTENSION = FILE_EXTENSION_SEP + OPEN_DOC_SPREADSHEET_EXTENSION_NAME;

	public static final String OPEN_DOC_GRAPHICS_EXTENSION_NAME = "odg";
	public static final String OPEN_DOC_GRAPHICS_EXTENSION = FILE_EXTENSION_SEP + OPEN_DOC_GRAPHICS_EXTENSION_NAME;

	public static final String PDF_EXTENSION_NAME = "pdf";
	public static final String PDF_EXTENSION = FILE_EXTENSION_SEP + PDF_EXTENSION_NAME;
	
	public static final String PLAIN_TEXT_EXTENSION_NAME = "txt";
	public static final String PLAIN_TEXT_EXTENSION = FILE_EXTENSION_SEP + PLAIN_TEXT_EXTENSION_NAME;
	
	public static final String[] OPEN_DOC_EXTENSIONS = new String[] { 
		FileExtensionConstants.OPEN_DOC_TEXT_EXTENSION_NAME,
		FileExtensionConstants.OPEN_DOC_PRESENTATION_EXTENSION_NAME,
		FileExtensionConstants.OPEN_DOC_SPREADSHEET_EXTENSION_NAME,
		FileExtensionConstants.OPEN_DOC_GRAPHICS_EXTENSION_NAME,
		FileExtensionConstants.PDF_EXTENSION_NAME,
		FileExtensionConstants.PLAIN_TEXT_EXTENSION_NAME,
	};

}
