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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.jooreports.templates.DocumentTemplateException;


/**
 * Table of Contents Bookmarks List Builder.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see http://api.itextpdf.com/itext/com/itextpdf/text/pdf/SimpleBookmark.html
 * @see http://itext-general.2136553.n4.nabble.com/simply-add-a-bookmark-at-the-root-of-new-PDF-for-each-concat-ed-document-td2140075.html
 * @see http://www.mail-archive.com/itext-questions@lists.sourceforge.net/msg10971.html
 * @see com.itextpdf.text.pdf.SimpleBookmark
 * @see http://itextpdf.com/examples/iia.php?id=140		
 */
public class TocBookmarkListBuilder {
	
	public static List<HashMap<String, Object>> buildTocBookmarkList( Toc toc ) throws IOException, DocumentTemplateException {

		List<HashMap<String, Object>> bookmarkList = new ArrayList<HashMap<String, Object>>();
		
		if (toc != null) {
			
			Iterator<TocRow> tocRowIter = toc.rowIterator();
			
			while (tocRowIter.hasNext()) {
				TocRow tocRow = tocRowIter.next();
				HashMap<String,Object> tocBookmarkEntry = buildBookmarkEntry( tocRow );
				bookmarkList.add( tocBookmarkEntry );
			}
		}
		
		return bookmarkList;
	}
	
	
	public static HashMap<String,Object> buildBookmarkEntry( TocRow tocRow ) {
		
		HashMap<String,Object> bookmarkEntry = buildBookmarkEntry( tocRow.getTitle(), tocRow.getPageNumber() );
		return bookmarkEntry;
	}
	
	
	public static HashMap buildBookmarkEntry( String title, int pageNumber ) {
		
		HashMap bookmarkEntry = new HashMap();
		bookmarkEntry.put("Title", title);
		bookmarkEntry.put("Action", "GoTo");
		bookmarkEntry.put("Page", pageNumber + " Fit");
		
		return bookmarkEntry;
	}

}
