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

package net.mitnet.tools.pdf.book.pdf.event;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * TOC Page Event Listener.
 * 
 * @author Tim Telick <telcik@gmail.com>
 * 
 * @see PdfPageEvent
 * @see PdfPageEventHelper
 */
public class TocPdfPageEventListener extends PdfPageEventHelper {

	/**
	 * Called when the document is opened.
	 * 
	 * @param writer
	 *            the <CODE>PdfWriter</CODE> for this document
	 * @param document
	 *            the document
	 */
	public void onOpenDocument(PdfWriter writer, Document document) {
	}

	/**
	 * Called when a page is initialized.
	 * <P>
	 * Note that if even if a page is not written this method is still called.
	 * It is preferable to use <CODE>onEndPage</CODE> to avoid infinite loops.
	 * 
	 * @param writer
	 *            the <CODE>PdfWriter</CODE> for this document
	 * @param document
	 *            the document
	 */
	public void onStartPage(PdfWriter writer, Document document) {
	}

	/**
	 * Called when a page is finished, just before being written to the
	 * document.
	 * 
	 * @param writer
	 *            the <CODE>PdfWriter</CODE> for this document
	 * @param document
	 *            the document
	 */
	public void onEndPage(PdfWriter writer, Document document) {
	}

	/**
	 * Called when the document is closed.
	 * <P>
	 * Note that this method is called with the page number equal to the last
	 * page plus one.
	 * 
	 * @param writer
	 *            the <CODE>PdfWriter</CODE> for this document
	 * @param document
	 *            the document
	 */
	public void onCloseDocument(PdfWriter writer, Document document) {
	}

}
