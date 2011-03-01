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

package net.mitnet.tools.pdf.book.openoffice.io;

import net.mitnet.tools.pdf.book.io.FileNameSuffixFileFilter;


/**
 * Open Office Document File Filter.
 *
 * @author Tim Telcik <telcik@gmail.com>
 */
public class OpenOfficeDocumentFileFilter extends FileNameSuffixFileFilter {
	
	public static final String OPEN_OFFICE_DOCUMENT_FILE_EXTENSION = ".odt";
	
	public OpenOfficeDocumentFileFilter() {
		super( OPEN_OFFICE_DOCUMENT_FILE_EXTENSION );
	}

}
