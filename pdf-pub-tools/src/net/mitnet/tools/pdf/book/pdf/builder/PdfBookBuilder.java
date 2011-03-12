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

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.mitnet.tools.pdf.book.io.FileHelper;
import net.mitnet.tools.pdf.book.model.toc.Toc;
import net.mitnet.tools.pdf.book.model.toc.TocRow;
import net.mitnet.tools.pdf.book.model.toc.TocRowChangeListener;
import net.mitnet.tools.pdf.book.pdf.event.PdfPageEventLogger;
import net.mitnet.tools.pdf.book.pdf.itext.PdfReaderHelper;
import net.mitnet.tools.pdf.book.util.MathHelper;
import net.mitnet.tools.pdf.book.util.ProgressMonitor;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.lowagie.text.Chapter;
import com.lowagie.text.ChapterAutoNumber;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfPageEvent;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;


/**
 * PDF Book Builder.
 * 
 * TODO - review page margins and layout
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see com.lowagie.toolbox.plugins.Handouts
 * @see com.lowagie.toolbox.plugins.NUp
 * @see Chapter
 * @see ChapterAutoNumber
 * @see Section
 */
public class PdfBookBuilder {
	
	// TOOD - default page size based on Locale
	// private static final Rectangle DEFAULT_DOCUMENT_PAGE_SIZE = PageSize.A4;
	private static final Rectangle DEFAULT_DOCUMENT_PAGE_SIZE = PageSize.LETTER;
	
	// private static final String DELICIOUS_ROMAN_FONT = "lib/Delicious-Roman.ttf";
	private static final String DELICIOUS_ROMAN_FONT = "Delicious-Roman.ttf";
	private static final String DEFAULT_FONT = DELICIOUS_ROMAN_FONT;
	private static final int DEFAULT_FONT_SIZE = 14;

	private Rectangle pageSize = DEFAULT_DOCUMENT_PAGE_SIZE;
	private String metaTitle = null;
	private String metaAuthor = null;
	private boolean verbose = false;
	private boolean debug = false;
	
	private ProgressMonitor progressMonitor = null;
	private TocRowChangeListener tocRowChangeListener = null;
	private PdfPageEvent pdfPageEventListener = null;
	

	public PdfBookBuilder() {
		// do nothing
	}
	
	public PdfBookBuilder( Rectangle pageSize ) {
		if ( pageSize == null ) { 
			throw new IllegalStateException( "Page size must be non-null" );
		}
		this.pageSize = pageSize;
	}
	
	public void setVerbose( boolean value ) {
		this.verbose = value;
	}

	public boolean isVerboseEnabled() {
		return this.verbose;
	}
	
	public void setDebug( boolean value ) {
		this.debug = value;
	}
	
	public boolean isDebugEnabled() {
		return this.debug;
	}
	
	public Rectangle getPageSize() {
		return this.pageSize;
	}

	public float getPageWidth() {
		return this.pageSize.getWidth();
	}
	
	public float getPageHeight() {
		return this.pageSize.getHeight();
	}

	public String getMetaTitle() {
		return metaTitle;
	}

	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}

	public String getMetaAuthor() {
		return metaAuthor;
	}

	public void setMetaAuthor(String metaAuthor) {
		this.metaAuthor = metaAuthor;
	}
	
	public ProgressMonitor getProgressMonitor() {
		return progressMonitor;
	}

	public void setProgressMonitor(ProgressMonitor progresMonitor) {
		this.progressMonitor = progresMonitor;
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

	public void buildBook( File sourceDir, File outputFile ) throws Exception {

		if (isVerboseEnabled()) {
			System.out.println( "-- sourceDir: " + sourceDir);
			System.out.println( "-- outputFile: " + outputFile);
			System.out.println( "-- progressMonitor: " + getProgressMonitor());
			System.out.println( "-- tocRowChangeListener: " + getTocRowChangeListener());
		}
		
		List<File> sourceFileList = FileHelper.findPdfFiles(sourceDir,true);
		System.out.println( "-- sourceFileList.size: " + sourceFileList.size());
		System.out.println( "-- sourceFileList: " + sourceFileList);
		
		if (!sourceFileList.isEmpty()) {
			buildBook( sourceFileList, outputFile );
		}
	}

	public void buildBook( List<File> sourceFileList, File outputFile ) {
		
		try {

			float pageWidth = getPageWidth();
			float pageHeight = getPageHeight();
			
			// Create new Document
			
			/*
			float marginLeft = 36;
			float marginRight = 36;
			float marginTop = 36;
			float marginBottom = 36;
			*/
			
			if (isVerboseEnabled()) {
				System.out.println("-- Building output PDF file " + outputFile);
			}
			
			// TableOfContents toc = new TableOfContents();
			
			Document outputDocument = new Document( getPageSize() );
			// Document outputDocument = new Document( getPageSize(), marginLeft, marginRight, marginTop, marginBottom );
			
			PdfWriter pdfWriter = PdfWriter.getInstance(outputDocument, new FileOutputStream(outputFile));
			
			PdfPageEventLogger pdfPageEventLogger = new PdfPageEventLogger();
			pdfWriter.setPageEvent(pdfPageEventLogger);
			
			outputDocument.open();
			
			if (!StringUtils.isEmpty(metaTitle)) {
				outputDocument.addTitle(metaTitle);
			}
			if (!StringUtils.isEmpty(metaAuthor)) {
				outputDocument.addAuthor(metaAuthor);
			}
			
			PdfContentByte pdfContent = pdfWriter.getDirectContent();

			// Loop through and pull pages
			int outputPageCount = 0;
			int currentSourceFileIndex = 0;
			int maxSourceFileIndex = sourceFileList.size();
			
			BaseFont pageLabelFont = BaseFont.createFont( DEFAULT_FONT, BaseFont.CP1250, BaseFont.EMBEDDED );
			if (isVerboseEnabled()) {
				System.out.println("-- Using page label font " + pageLabelFont);
			}

			for (File sourceFile : sourceFileList) {

				currentSourceFileIndex++;

				// TODO - refactor current file PDF page processing to another method
				// TODO - handle failover to ensure processing continues ???
				
				if (sourceFile.isFile()) {
					
					if (isVerboseEnabled()) {
						System.out.println("-- Reading source PDF file " + sourceFile);
					}

					int sourcePageIndex = 0;
					
					PdfReader sourcePdfReader = new PdfReader(sourceFile.getCanonicalPath());
					PdfReaderHelper sourcePdfReaderHelper = new PdfReaderHelper( sourcePdfReader );
					if (isVerboseEnabled()) {
						System.out.println("-- PDF reader is " + sourcePdfReader);
						System.out.println("-- PDF reader helper is " + sourcePdfReaderHelper);
					}
					
					String currentSourcePdfTitle = FilenameUtils.getBaseName(sourceFile.getName());
					String currentSourcePdfAuthor = getSystemUserName();
					if (isVerboseEnabled()) {
						System.out.println("-- PDF title is " + currentSourcePdfTitle);
						System.out.println("-- PDF author is " + currentSourcePdfAuthor);
					}
					
					currentSourcePdfTitle = sourcePdfReaderHelper.getDocumentTitle( currentSourcePdfTitle );
					currentSourcePdfAuthor = sourcePdfReaderHelper.getDocumentTitle( currentSourcePdfAuthor );
					if (isVerboseEnabled()) {
						System.out.println("-- PDF info title is " + currentSourcePdfTitle);
						System.out.println("-- PDF info author is " + currentSourcePdfAuthor);
					}

					
					boolean firstPageOfCurrentSource = true;
					
					int maxSourcePages = sourcePdfReader.getNumberOfPages();
					if (isVerboseEnabled()) {
						System.out.println("-- There are " + maxSourcePages + " page(s) in source PDF file " + sourceFile);
					}

					// process all pages from source doc ...
					while (sourcePageIndex < maxSourcePages) {

						// add new page to current document
						outputDocument.newPage();
						
						outputPageCount++;
						if (isVerboseEnabled()) {
							System.out.println("-- Building output PDF page " + outputPageCount + " ...");
						}
						
						// add first page of current source to TOC listener
						if (firstPageOfCurrentSource) {
							int firstPageOfCurrentSourceInOutput = outputPageCount;
							if (tocRowChangeListener != null) {
								TocRow tocEntry = new TocRow( currentSourcePdfTitle, firstPageOfCurrentSourceInOutput );
								tocRowChangeListener.addTocRow(tocEntry);
								if (isVerboseEnabled()) {
									System.out.println("-- Added TOC entry " + tocEntry + " to listener");
								}
							}
							firstPageOfCurrentSource = false;
						}
						
						// extract first page from source document
						sourcePageIndex++;
						if (isVerboseEnabled()) {
							System.out.println("-- Adding page " + sourcePageIndex + " of " + maxSourcePages + " from source to output");
						}
						PdfImportedPage page1 = pdfWriter.getImportedPage(sourcePdfReader,sourcePageIndex);

						// extract second page from source document ?
						PdfImportedPage page2 = null;
						if (sourcePageIndex < maxSourcePages) {
							sourcePageIndex++;
							if (isVerboseEnabled()) {
								System.out.println("-- Adding page " + sourcePageIndex + " of " + maxSourcePages + " from source to output");
							}
							page2 = pdfWriter.getImportedPage( sourcePdfReader, sourcePageIndex );
						}

						// add first page to top half of current page
						// TODO - review magic transformation matrix numbers and offsets
						float p1a = 0.5f;
						float p1b = 0;
						float p1c = 0;
						float p1d = 0.5f;
						float p1e = (125);
						float p1f = ((pageWidth / 2) + 120 + 20);
						// pdfContent.addTemplate( page1, 0.5f, 0, 0, 0.5f, 125, ((pageWidth / 2) + 120 + 20) );
						pdfContent.addTemplate( page1, p1a, p1b, p1c, p1d, p1e, p1f );

						// add second page to bottom half of current page
						if (page2 != null) {
							// TODO - review magic transformation matrix numbers and offsets
							float p2a = 0.5f; 
							float p2b = 0;
							float p2c = 0;
							float p2d = 0.5f;
							float p2e = 125;
							float p2f = 120;
							// pdfContent.addTemplate( page2, 0.5f, 0, 0, 0.5f, 125, 120 );
							pdfContent.addTemplate( page2, p2a, p2b, p2c, p2d, p2e, p2f );
						}

						// Add current page number to page footer
						String pageCountLabel = "Page " + outputPageCount;
						pdfContent.beginText();
						pdfContent.setFontAndSize( pageLabelFont, DEFAULT_FONT_SIZE );
						pdfContent.showTextAligned( PdfContentByte.ALIGN_CENTER, pageCountLabel, (pageWidth / 2), 40, 0 );
						pdfContent.endText();
					}
					
					if (isVerboseEnabled()) {
						System.out.println("-- Finished reading " + maxSourcePages + " page(s) from source PDF file " + sourceFile);
					}

					// update progress
					if (getProgressMonitor() != null) {
						int fileProgressPercentage = MathHelper.calculatePercentage(currentSourceFileIndex, maxSourceFileIndex);
						getProgressMonitor().setProgressPercentage(fileProgressPercentage);
					}
				}
			}

			// close document
			outputDocument.close();
			
			if (isVerboseEnabled()) {
				System.out.println( "-- Output PDF file " + outputFile + " contains " + outputPageCount + " page(s)" );
			}
			
			// TODO - output ODT page stats summary
			
		} catch (Exception e) {

			String msg = "Error building PDF book: " + e.getMessage();
			e.printStackTrace( System.err );
			System.err.println(msg);

		}
		
	}
	
	// TODO - fix Locale logic
	private Rectangle getPageSizeForLocale() {
		return PageSize.A4;
	}
	
	// TODO - fix Locale logic
	private Rectangle getPageSizeForLocale( Locale locale ) {
		return PageSize.A4;
	}
	
	private void trace( String msg ) {
		if (isVerboseEnabled()) {
			System.out.println( msg );
		}
	}
	
	private void debug( String msg ) {
		if (isDebugEnabled()) {
			System.out.println( msg );
		}
	}
	
	private Map buildTocTemplateData( Toc toc ) {
		Map templateData = new HashMap();
		return templateData;
	}
	
	private String getSystemUserName() {
		return System.getProperty( "user.name" );
	}

}
