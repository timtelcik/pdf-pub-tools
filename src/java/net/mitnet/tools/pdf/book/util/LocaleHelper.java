/*
    Copyright (C) 2010-2013  Tim Telcik <telcik@gmail.com>

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

package net.mitnet.tools.pdf.book.util;

import java.util.Locale;



/**
 * Locale Helper.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see Locale
 */
public class LocaleHelper {
	
	public static final boolean isAustralianLocale() {
		boolean result = false;
		String code = Locale.getDefault().getCountry();
		if (Iso3166CountryCodes.AUSTRALIA.equalsIgnoreCase(code)) {
			result = true;
		}
		return result;
	}
	
	public static final boolean isUsaLocale() {
		boolean result = false;
		String code = Locale.getDefault().getCountry();
		if (Iso3166CountryCodes.USA.equalsIgnoreCase(code)) {
			result = true;
		}
		return result;
	}

}
