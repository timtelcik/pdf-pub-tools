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

package net.mitnet.tools.pdf.book.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Table Of Contents.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class TableOfContents {
	
	private List<TableOfContentsEntry> tocEntryList = new ArrayList<TableOfContentsEntry>();
	
	
	public TableOfContents() {
	}
	
	public TableOfContents( List<TableOfContentsEntry> tocEntries ) {
		this.tocEntryList = tocEntries;
	}
	
	public void addTocEntry( TableOfContentsEntry tocEntry ) {
		tocEntryList.add( tocEntry );
	}
	
	public int getTocEntryCount() {
		return tocEntryList.size();
	}
	
	public Iterator<TableOfContentsEntry> iterator() {
		return tocEntryList.iterator();
	}

	@Override
	public String toString() {
		return "TableOfContents [tocEntryList=" + tocEntryList + "]";
	}

}
