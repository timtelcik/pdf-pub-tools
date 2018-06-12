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

package net.mitnet.tools.pdf.book.pdf.util;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;


/**
 * PDF Page Values.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class PdfPageValues {

	public static final String PAGE_ORIENTATION_LANDSCAPE = "landscape";
	
	public static final String PAGE_ORIENTATION_PORTRAIT = "portrait";
	
	public static final String PAGE_SIZE_ISO_A4_STRING = "A4";
	
	public static final String PAGE_SIZE_US_LETTER_STRING = "LETTER";

	public static final Rectangle DEFAULT_PAGE_SIZE = PageSize.A4;

}
