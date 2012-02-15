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

package net.mitnet.tools.pdf.book.pdf.builder;

import net.mitnet.tools.pdf.book.conf.BaseConfig;
import net.mitnet.tools.pdf.book.model.toc.TocRowChangeListener;
import net.mitnet.tools.pdf.book.openoffice.net.OpenOfficeServerContext;

import com.lowagie.text.pdf.PdfPageEvent;


/**
 * PDF Book Builder Configuration.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class PdfBookBuilderConfig extends BaseConfig {

	public static final String DELICIOUS_ROMAN_FONT_FILE_NAME = "Delicious-Roman.ttf";
	public static final String DEFAULT_FONT_FILE_NAME = DELICIOUS_ROMAN_FONT_FILE_NAME;
	// public static final String FONT_FILE_PATH = "resources/fonts";
	public static final String FONT_FILE_PATH = "fonts";
	// public static final String DEFAULT_FONT_PATH = "resources/fonts/" + DELICIOUS_ROMAN_FONT_FILE_NAME;
	public static final String DEFAULT_FONT_PATH = FONT_FILE_PATH + "/" + DELICIOUS_ROMAN_FONT_FILE_NAME;
	public static final int DEFAULT_FONT_SIZE = 14;

	private OpenOfficeServerContext serverContext = new OpenOfficeServerContext();
	private TocRowChangeListener tocRowChangeListener = null;
	private PdfPageEvent pdfPageEventListener = null;
	private String fontName = DEFAULT_FONT_FILE_NAME;
	private int fontSize = DEFAULT_FONT_SIZE;
	private boolean showPageNumbers = true;
	
	
	public PdfBookBuilderConfig() {	
	}
	
	public void setFontName( String fontName ) {
		this.fontName = fontName;
	}
	
	public String getFontName() {
		return this.fontName;
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public int getFontSize() {
		return fontSize;
	}
	
	public TocRowChangeListener getTocRowChangeListener() {
		return tocRowChangeListener;
	}

	public void setTocRowChangeListener(TocRowChangeListener tocRowChangeListener) {
		this.tocRowChangeListener = tocRowChangeListener;
	}

	public PdfPageEvent getPdfPageEventListener() {
		return pdfPageEventListener;
	}

	public void setPdfPageEventListener(PdfPageEvent pdfPageEventListener) {
		this.pdfPageEventListener = pdfPageEventListener;
	}

	public void setShowPageNumbers(boolean showPageNumbers) {
		this.showPageNumbers = showPageNumbers;
	}
	
	public boolean isShowPageNumbers() {
		return showPageNumbers;
	}

}
