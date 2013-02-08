/*
    Copyright (C) 2010-2013  Tim Telcik <telcik@gmail.com>

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.SimpleBookmark;


/**
 * PDF Bookmark Builder.
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
 * @author Tim Telcik <telcik@gmail.com>
 */
public class PdfBookmarkBuilder {
	
	public PdfBookmarkBuilder() {
	}


	public void addBookmarks( File inputPdfFile, String bookmarksFile, File outputPdfFile ) throws IOException, DocumentException {
		
		Reader bookmarksReader = new FileReader(bookmarksFile);

		List<HashMap<String, Object>> bookmarks = SimpleBookmark.importFromXML(bookmarksReader);
		
		addBookmarks( inputPdfFile, bookmarks, outputPdfFile);
	}

	
	public void addBookmarks( File inputPdfFile, List<HashMap<String, Object>> bookmarks, File outputPdfFile ) throws IOException, DocumentException {
		
		String inputPdfFileName = inputPdfFile.getAbsolutePath();
		
		String outputPdfFileName = outputPdfFile.getAbsolutePath();
		
		addBookmarks( inputPdfFileName, bookmarks, outputPdfFileName);
	}
	
	
	public void addBookmarks( String inputPdfFile, String bookmarksFile, String outputPdfFile ) throws IOException, DocumentException {
		
		Reader bookmarksReader = new FileReader(bookmarksFile);

		List<HashMap<String, Object>> bookmarks = SimpleBookmark.importFromXML(bookmarksReader);
		
		addBookmarks( inputPdfFile, bookmarks, outputPdfFile);
	}
	
	
	public void addBookmarks( String inputPdfFile, List<HashMap<String, Object>> bookmarks, String outputPdfFile ) throws IOException, DocumentException {
		
		PdfReader reader = new PdfReader(inputPdfFile);

		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputPdfFile));
		
		if (bookmarks != null) {
			System.out.println("bookmarks.size: " + bookmarks.size());	
		}
		System.out.println("bookmarks: " + bookmarks);
		
		stamper.setOutlines(bookmarks);
		
		stamper.addViewerPreference(PdfName.NONFULLSCREENPAGEMODE, PdfName.USEOUTLINES);
		
		stamper.close();
	}	

	
	public static void main(String[] args) throws IOException, DocumentException {
		
		if (args.length != 3) {
			System.err.println("usage: " + PdfBookmarkBuilder.class.getName() + " <input-pdf-file> <bookmarks-file> <output-pdf-file>");
			System.exit(1);
		}
		
		String inputPdfFile = args[0];
		String bookmarksFile = args[1];
		String outputPdfFile = args[2];
		
		System.out.println("inputPdfFile: " + inputPdfFile);
		System.out.println("bookmarksFile: " + bookmarksFile);
		System.out.println("outputPdfFile: " + outputPdfFile);
		
		PdfBookmarkBuilder builder = new PdfBookmarkBuilder();
		builder.addBookmarks(inputPdfFile,bookmarksFile,outputPdfFile);
		
		System.exit(0);
	}

}
