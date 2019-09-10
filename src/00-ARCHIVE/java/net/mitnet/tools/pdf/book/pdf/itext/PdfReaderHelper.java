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

package net.mitnet.tools.pdf.book.pdf.itext;

import java.util.Map;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;


/**
 * PDF Reader Helper.
 *
 * This class provides helper methods to an underlying PDFReader.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class PdfReaderHelper {
	
	private PdfReader pdfReader = null;
	private Map<String,String> pdfInfo = null;
	
	
	public PdfReaderHelper( PdfReader pdfReader ) {
		this.pdfReader = pdfReader;
		this.pdfInfo = pdfReader.getInfo();
	}
	
	/*
	public String getPdfStringValue( PdfDictionary pdfDictionary, PdfName pdfName ) {
		return getPdfStringValue( pdfDictionary, pdfName, null );
	}
	
	public String getPdfStringValue( PdfDictionary pdfDictionary, PdfName pdfName, String defaultValue ) {
		String result = defaultValue;
		if (pdfDictionary != null) {
			PdfString pdfString = pdfDictionary.getAsString( pdfName );
			if (pdfString != null) {
				result = pdfString.toString();
			}
		}
		return result;
	}
	*/
	
	public String getDocumentTitle( String defaultValue ) {
		return getPdfStringValue( this.pdfInfo, PdfName.TITLE, defaultValue );
	}

	public String getDocumentAuthor( String defaultValue ) {
		return getPdfStringValue( this.pdfInfo, PdfName.AUTHOR, defaultValue );
	}
	
	/**
	 * @see PdfReader#getInfo()
	 */
	public String getPdfStringValue( Map<String,String> pdfInfo, PdfName pdfName, String defaultValue ) {
		String result = defaultValue;
		if ((pdfInfo != null) && (pdfName != null)) {
			String pdfObjectKey = PdfName.decodeName(pdfName.toString());
			String pdfStringValue = (String) pdfInfo.get( pdfObjectKey );
			if (pdfStringValue != null) {
				result = pdfStringValue;
			}
		}
		return result;
	}
	
	/*
	public String getPdfDocumentTitle( PdfDictionary pdfDicionary ) {
		return getPdfStringValue( pdfDicionary, PdfName.TITLE );
	}
	*/
	
	/*
	public String getPdfDocumentAuthor( PdfDictionary pdfDicionary ) {
		return getPdfStringValue( pdfDicionary, PdfName.AUTHOR );
	}
	*/

}
