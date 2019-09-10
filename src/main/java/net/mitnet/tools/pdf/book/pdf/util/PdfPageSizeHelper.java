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

import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;

import java.util.Locale;

import net.mitnet.tools.pdf.book.util.LocaleHelper;


/**
 * PDF Paper Size Helper.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see Locale
 */
public class PdfPageSizeHelper {
	
	/**
	 * Returns the default page size for the current locale.
	 * 
	 * eg. 
	 * "en_AU" = PageSize.A4
     * "en_US" = PageSize.LETTER
	 */
	public static final Rectangle getDefaultPageSizeByLocale() {
		
		Rectangle pageSize = PageSize.A4;
		
		Locale locale = Locale.getDefault();
		if (locale != null) {
			pageSize = getDefaultPageSizeByLocale( locale );
		}
		
		return pageSize;
	}
	

	/**
	 * Returns the default page size for the current locale.
	 */
	public static final Rectangle getDefaultPageSizeByLocale( Locale locale ) {
		
		Rectangle pageSize = PdfPageValues.DEFAULT_PAGE_SIZE;
		
		if (locale != null) {
			if (LocaleHelper.isUsaLocale()) {
				pageSize = getDefaultPageSizeByLocale( locale, PdfPageValues.DEFAULT_PAGE_SIZE );
			}
		}
		
		return pageSize;
	}

	
	/**
	 * Returns the default page size for the current locale.
	 * @return
	 */
	public static final Rectangle getDefaultPageSizeByLocale( Locale locale, Rectangle defaultPageSize ) {
		
		Rectangle pageSize = PdfPageValues.DEFAULT_PAGE_SIZE;
		if (defaultPageSize != null) {
			pageSize = defaultPageSize;
		}
		
		if (locale != null) {
			if (LocaleHelper.isUsaLocale()) {
				pageSize = PageSize.LETTER;
			}
		}
		
		return pageSize;
	}

}
