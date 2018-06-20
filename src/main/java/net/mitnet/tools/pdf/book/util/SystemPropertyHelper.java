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

package net.mitnet.tools.pdf.book.util;

import org.apache.commons.lang.StringUtils;


/**
 * SystemPropertyHelper.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class SystemPropertyHelper {

	public static String getSystemPropertyAsString( String key ) {
		
		String val = getSystemPropertyAsString( key, null );
		
		return val;
	}

	public static String getSystemPropertyAsString( String key, String defaultValue ) {
		
		String val = System.getProperty(key, defaultValue);
		
		return val;
	}
	
	
	public static Integer getSystemPropertyAsInteger( String key ) {
		
		Integer val = getSystemPropertyAsInteger( key, null );
		
		return val;
	}

	public static Integer getSystemPropertyAsInteger( String key, Integer defaultValue ) {
		
		Integer val = defaultValue;
		
		String strValue = System.getProperty(key);
		
		if (! StringUtils.isEmpty(strValue)) {
			try {
				val = Integer.valueOf(strValue);
			} catch (Exception e) {
				String msg = "Error parsing Integer from String value " + strValue;
				System.err.println(msg);
			}
		}
		
		return val;
	}	

}
