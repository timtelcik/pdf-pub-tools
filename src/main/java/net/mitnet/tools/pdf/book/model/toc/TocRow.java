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

package net.mitnet.tools.pdf.book.model.toc;


/**
 * Table Of Contents Row.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class TocRow {
	
	private String title;
	private int pageNumber;
	
	
	public TocRow() {	
	}
	
	public TocRow( String title, int pageNumber ) {
		this.title = title;
		this.pageNumber = pageNumber;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getPageNumber() {
		return pageNumber;
	}
	
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append( "[" + this.getClass().getName() + "]" );
		buffer.append( "(" );
		buffer.append( "title=" + getTitle() );
		buffer.append( ", pageNumber=" + getPageNumber() );
		buffer.append( ")" );
		return buffer.toString();
	}	

}
