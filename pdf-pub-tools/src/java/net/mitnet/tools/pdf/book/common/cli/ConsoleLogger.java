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

package net.mitnet.tools.pdf.book.common.cli;


/**
 * Console Logger.
 * 
 * TODO: Merge/replace with Apache Log4j.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class ConsoleLogger {

	public static void verbose( String msg ) {
		System.out.println( msg );
	}
	
	public static void debug( String msg ) {
		System.out.println( "DEBUG: " + msg );
	}
	
	public static void info( String msg ) {
		System.out.println( "INFO: " + msg );
	}
	
	public static void error( String msg ) {
		System.err.println( "ERROR: " + msg );
	}
	
	public static void error( String msg, Exception exception ) {
		System.err.println( "ERROR: " + msg );
		if (exception != null) {
			System.err.println( exception );
			exception.printStackTrace(System.err);
		}
	}	

}

