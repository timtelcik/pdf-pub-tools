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

package net.mitnet.tools.pdf.book.test;

import java.io.File;


/**
 * Test Constants.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class TestConstants {
	
	
	public static final File HOME_DIR = new File( System.getProperty( "user.home" ) );
	
	public static final File FILE_SEP = new File( System.getProperty( "file.separator" ) );
	
	public static final File SPOOL_DIR = new File( HOME_DIR, "/spool" );
	
	public static final File BOOK_SOURCE_DIR = new File( SPOOL_DIR, "/docs/liferay-book/source" );
	public static final File BOOK_OUTPUT_DIR = new File( SPOOL_DIR, "/docs/liferay-book/output" );
	
	public static final File PDF_SOURCE_DIR = new File( SPOOL_DIR, "/docs/itext-toolbox/source" );
	public static final File PDF_OUTPUT_DIR = new File( SPOOL_DIR, "/docs/itext-toolbox/output" );

}
