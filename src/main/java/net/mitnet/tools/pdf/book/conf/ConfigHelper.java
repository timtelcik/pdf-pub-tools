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

package net.mitnet.tools.pdf.book.conf;

import net.mitnet.tools.pdf.book.common.cli.CliOptions;
import net.mitnet.tools.pdf.book.common.cli.CommandLineHelper;
import net.mitnet.tools.pdf.book.pdf.util.PdfPageSizeHelper;
import net.mitnet.tools.pdf.book.pdf.util.PdfPageValues;

import org.apache.commons.lang.StringUtils;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;


/**
 * Config Helper.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * TODO: Refactor
 */
public class ConfigHelper {
	
	public static Rectangle parsePageSize( CommandLineHelper commandLineHelper ) {

		Rectangle pageSize = PdfPageSizeHelper.getDefaultPageSizeByLocale();		
		if (commandLineHelper.hasOption(CliOptions.OPTION_PAGE_SIZE)) {
			String pageSizeValue = commandLineHelper.getOptionValue(CliOptions.OPTION_PAGE_SIZE);
			System.out.println("Page Size Option Detected: " + pageSizeValue);
			if (!StringUtils.isEmpty(pageSizeValue)) {
				if (PdfPageValues.PAGE_SIZE_US_LETTER_STRING.equalsIgnoreCase(pageSizeValue)) {
					System.out.println("Setting Page Size to Letter");
					pageSize = PageSize.LETTER;
				}
			}
		}
		return pageSize;
	}

}
