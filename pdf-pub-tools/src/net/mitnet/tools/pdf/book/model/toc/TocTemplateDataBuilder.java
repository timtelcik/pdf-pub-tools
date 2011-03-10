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
import java.util.Map;

import net.sf.jooreports.templates.DocumentTemplateException;


/**
 * Table Of Contents Template Data Builder.
 * 
 * NOTE: JODReports template data contains <tt>[#list items as item]</tt>
 * 
 * TODO: validate and prevent NPE
 * TODO: rename sections to rows
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class TocTemplateDataBuilder {
	
	public static final String KEY_TOC = "toc";
	
	public static final String KEY_SECTION = "section";
	// public static final String KEY_TOC_ROW = "tocRow";
	
	public static final String KEY_SECTION_TITLE = "title";
	// public static final String KEY_TOC_ROW_TITLE = "title";
	
	public static final String KEY_SECTION_PAGE_NUMBER = "pageNumber";
	// public static final String KEY_TOC_ROW_PAGE_NUMBER = "pageNumber";
	
	
	public static Map buildTocTemplateData( Toc toc ) throws IOException, DocumentTemplateException {

		Map dataMap = new HashMap();
		
		Map tocMap = new HashMap();
		dataMap.put(KEY_TOC, tocMap);
		
		List sectionList = new ArrayList();
		tocMap.put(KEY_SECTION, sectionList);
		
		Iterator<TocRow> tocRowIter = toc.rowIterator();
		
		while (tocRowIter.hasNext()) {
			TocRow tocRow = tocRowIter.next();
			Map tocRowMap = createTocSectionData( tocRow );
			sectionList.add( tocRowMap );
		}
		
		return dataMap;
	}
	
	public static Map createTocSectionData( TocRow tocRow ) {
		Map tocRowMap = createTocSectionData( tocRow.getTitle(), tocRow.getPageNumber() );
		return tocRowMap;
	}
	
	public static Map createTocSectionData( String title, int pageNumber ) {
		Map tocRowMap = new HashMap();
		tocRowMap.put(KEY_SECTION_TITLE,title);
		tocRowMap.put(KEY_SECTION_PAGE_NUMBER, new Integer(pageNumber));
		return tocRowMap;
	}

}
