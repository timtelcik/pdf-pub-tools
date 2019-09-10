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

package net.mitnet.tools.pdf.book.pdf.util;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.SimpleBookmark;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * PDF Bookmark Builder.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 *
 * @see http://api.itextpdf.com/itext/com/itextpdf/text/pdf/SimpleBookmark.html
 * @see http://api.itextpdf.com/itext/com/itextpdf/text/pdf/PdfStamper.html
 * @see http://api.itextpdf.com/itext/com/itextpdf/text/pdf/interfaces/PdfViewerPreferences.html
 * @see http://itext-general.2136553.n4.nabble.com/simply-add-a-bookmark-at-the-root-of-new-PDF-for-each-concat-ed-document-td2140075.html 
 * @see http://itextpdf.com/examples/iia.php?id=139
 * @see http://itextpdf.com/examples/iia.php?id=140
 * @see http://itextpdf.com/examples/iia.php?id=141 
 * @see http://kuujinbo.info/cs/itext_toc.aspx
 * 
 * @see PdfStamper
 * @see SimpleBookmark
 */
public class PdfBookmarkBuilder {
	
	public PdfBookmarkBuilder() {
	}


	public void addBookmarks( File inputPdfFile, List<HashMap<String, Object>> bookmarksList, File outputPdfFile ) throws IOException, DocumentException {
		
		String inputPdfFileName = inputPdfFile.getAbsolutePath();
		
		String outputPdfFileName = outputPdfFile.getAbsolutePath();
		
		addBookmarks( inputPdfFileName, bookmarksList, outputPdfFileName );
	}	
	
	
	public void addBookmarks( String inputPdfFile, String bookmarksFile, String outputPdfFile ) throws IOException, DocumentException {
		
		addBookmarks( inputPdfFile, bookmarksFile, outputPdfFile, null );
	}
	
	
	public void addBookmarks( String inputPdfFileName, String bookmarksFileName, String outputPdfFileName, Integer pageShift ) throws IOException, DocumentException {
		
		Reader bookmarksReader = new FileReader( bookmarksFileName );
		
		System.out.println("importing bookmarks from file " + bookmarksFileName);

		List<HashMap<String, Object>> bookmarksList = SimpleBookmark.importFromXML( bookmarksReader );
		
		if (pageShift != null) {
			System.out.println("shifting pages by (" + pageShift + ")");
			SimpleBookmark.shiftPageNumbers( bookmarksList, pageShift, null );
		}
		
		System.out.println("adding bookmarks to file " + bookmarksFileName);
		
		addBookmarks( inputPdfFileName, bookmarksList, outputPdfFileName );
		
		String newBookmarksOutFileName = "new-bookmarks.xml";
		
		Writer newBookmarksWriter = new FileWriter( newBookmarksOutFileName );

		// TODO: Review config and usage		
		SimpleBookmark.exportToXML( bookmarksList, newBookmarksWriter, StandardCharsets.UTF_8.name(), true );
	}	
	
	
	public void addBookmarks( String inputPdfFile, List<HashMap<String, Object>> bookmarksList, String outputPdfFile ) throws IOException, DocumentException {

		logList( bookmarksList, "Bookmarks" );
		
		PdfReader pdfReader = new PdfReader( inputPdfFile );
		
		System.out.println("adding bookmark outlines to file " + inputPdfFile);

		PdfStamper pdfStamper = new PdfStamper( pdfReader, new FileOutputStream( outputPdfFile ) );
		
		pdfStamper.setOutlines( bookmarksList );
		
		pdfStamper.addViewerPreference( PdfName.NONFULLSCREENPAGEMODE, PdfName.USEOUTLINES );
		
		pdfStamper.close();
	}	

	
	public static void main(String[] args) throws IOException, DocumentException {
		
		if (args.length < 3) {
			System.err.println("usage: " + PdfBookmarkBuilder.class.getName() + " <input-pdf-file> <bookmarks-file> <output-pdf-file> [page-shift]");
			System.exit(1);
		}
		
		String inputPdfFile = args[0];
		String bookmarksFile = args[1];
		String outputPdfFile = args[2];

		Integer pageShift = null;
		if (args.length == 4) {
			pageShift = Integer.valueOf( args[3] );			
		}
		
		System.out.println("inputPdfFile: " + inputPdfFile);
		System.out.println("bookmarksFile: " + bookmarksFile);
		System.out.println("outputPdfFile: " + outputPdfFile);

		if (pageShift != null) {
			System.out.println("pageShift: (" + pageShift + ")");			
		}
		
		PdfBookmarkBuilder builder = new PdfBookmarkBuilder();
		
		builder.addBookmarks( inputPdfFile, bookmarksFile, outputPdfFile, pageShift );
		
		System.exit(0);
	}
	
	
	private void logList( List list, String name ) {
		
		if (list != null) {
			if (!StringUtils.isEmpty(name)) {
				System.out.println("list name : " + name);
			}
			System.out.println("list size : " + list.size());			
			for (Object obj : list) {
				System.out.println(obj);
			}
		}
	}

}
