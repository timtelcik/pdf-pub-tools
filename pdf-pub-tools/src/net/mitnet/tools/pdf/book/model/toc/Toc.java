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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Table Of Contents.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class Toc {
	
	private List<TocRow> tocRowList = new ArrayList<TocRow>();
	
	
	public Toc() {
	}
	
	public Toc( List<TocRow> tocEntries ) {
		this.tocRowList = tocEntries;
	}
	
	public void addTocRow( TocRow tocRow ) {
		tocRowList.add( tocRow );
	}
	
	public int getTocRowCount() {
		return this.tocRowList.size();
	}
	
	public List<TocRow> getTocRowList() {
		return this.tocRowList;
	}
	
	public Iterator<TocRow> rowIterator() {
		return tocRowList.iterator();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append( "[" + this.getClass().getName() + "]" );
		buffer.append( "(" );
		buffer.append( "tocRowCount=" + getTocRowCount() );
		buffer.append( ", tocRowList=" + getTocRowList() );
		buffer.append( ")" );
		return buffer.toString();
	}

}
