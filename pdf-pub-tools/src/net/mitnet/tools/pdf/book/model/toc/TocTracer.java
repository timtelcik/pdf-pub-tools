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

package net.mitnet.tools.pdf.book.model.toc;

import java.util.Iterator;


/**
 * Table Of Conetns Tracer.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class TocTracer {
	
	public static void traceToc( Toc toc ) {
		if (toc != null) {
			System.out.println("--- Begin Table Of Contents");
			Iterator<TocEntry> tocIter = toc.iterator();
			while (tocIter.hasNext()) {
				TocEntry tocEntry = tocIter.next();
				String title = tocEntry.getTitle();
				int pageNumber = tocEntry.getPageNumber();
				System.out.println( title + " ... Page " + pageNumber );
			}
			System.out.println("--- End Table Of Contents");
		}
	}

}
