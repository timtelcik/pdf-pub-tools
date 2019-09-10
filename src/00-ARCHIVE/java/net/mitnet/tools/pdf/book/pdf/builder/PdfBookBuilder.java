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

package net.mitnet.tools.pdf.book.pdf.builder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.lowagie.text.Chapter;
import com.lowagie.text.ChapterAutoNumber;
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Section;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;


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
	
	// private Rectangle pageSize = DEFAULT_DOCUMENT_PAGE_SIZE;
	
	private PdfBookBuilderConfig config = new PdfBookBuilderConfig();
	

	public PdfBookBuilder() {
		// do nothing
	}
	
	public PdfBookBuilder( Rectangle pageSize ) {
		if ( pageSize == null ) { 
			throw new IllegalStateException( "Page size must be non-null" );
		}
		getConfig().setPageSize(pageSize) ;
	}
	
	public void setConfig(PdfBookBuilderConfig config) {
		if (config == null) {
			this.config = new PdfBookBuilderConfig();
		} else {
			this.config = config;
		}
	}
	
	public PdfBookBuilderConfig getConfig() {
		return this.config;
	}
	

	public void buildBook( File inputDir, File outputFile ) throws Exception {

		if (isVerboseEnabled()) {
			verbose( "Building book \"" + outputFile + "\" from source folder \"" + inputDir + "\" ..." );
		}

		if (isDebugEnabled()) {
			debug( "-- inputDir: " + inputDir);
			debug( "-- outputFile: " + outputFile);
			debug( "-- progressMonitor: " + getConfig().getProgressMonitor());
			debug( "-- tocRowChangeListener: " + getConfig().getTocRowChangeListener());
		}
		
		List<File> inputFileList = FileHelper.findPdfFiles(inputDir,true);
		if (isDebugEnabled()) {
			debug( "-- inputFileList contains " + inputFileList.size() + " items");
		}
		if (isDebugEnabled()) {
			debug( "-- inputFileList.size: " + inputFileList.size());
			debug( "-- inputFileList: " + inputFileList);
		}
		
		if (!inputFileList.isEmpty()) {
			buildBook( inputFileList, outputFile );
		}
	}

	public void buildBook( List<File> inputFileList, File outputFile ) {
		
		try {

			float pageWidth = getConfig().getPageWidth();
			float pageHeight = getConfig().getPageHeight();
			
			// Create new Document
			
			/*
			float marginLeft = 36;
			float marginRight = 36;
			float marginTop = 36;
			float marginBottom = 36;
			*/
			
			if (isVerboseEnabled()) {
				verbose("Building output PDF file " + outputFile);
			}
			
			// TableOfContents toc = new TableOfContents();
			
			ProgressMonitor progressMonitor = getConfig().getProgressMonitor();
			TocRowChangeListener tocRowChangeListener = getConfig().getTocRowChangeListener();
			
			Document outputDocument = new Document( getConfig().getPageSize() );
			// Document outputDocument = new Document( getPageSize(), marginLeft, marginRight, marginTop, marginBottom );
			
			PdfWriter pdfWriter = PdfWriter.getInstance(outputDocument, new FileOutputStream(outputFile));
			
			// TODO - review PDF page event forwarder
			PdfPageEventLogger pdfPageEventLogger = new PdfPageEventLogger();
			pdfWriter.setPageEvent(pdfPageEventLogger);
			
			outputDocument.open();
			
			String metaTitle = getConfig().getMetaTitle();
			if (!StringUtils.isEmpty(metaTitle)) {
				outputDocument.addTitle(metaTitle);
			}
			String metaAuthor = getConfig().getMetaAuthor();
			if (!StringUtils.isEmpty(metaAuthor)) {
				outputDocument.addAuthor(metaAuthor);
			}
			
			PdfContentByte pdfContent = pdfWriter.getDirectContent();

			// Loop through and pull pages
			int outputPageCount = 0;
			int currentSourceFileIndex = 0;
			int maxSourceFileIndex = inputFileList.size();
			
			// BaseFont pageLabelFont = BaseFont.createFont( PdfBookBuilderConfig.DEFAULT_FONT, BaseFont.CP1250, BaseFont.EMBEDDED );
			BaseFont pageLabelFont = BaseFont.createFont( PdfBookBuilderConfig.DEFAULT_FONT_PATH, BaseFont.CP1250, BaseFont.EMBEDDED );
			if (isVerboseEnabled()) {
				verbose("Using page label font " + pageLabelFont);
			}
			
			if (isVerboseEnabled()) {
				verbose("Assembling pages using n-up " + getConfig().getNup());
			}			

			for (File sourceFile : inputFileList) {

				currentSourceFileIndex++;

				// TODO - refactor current file PDF page processing to another method
				// TODO - handle failover to ensure processing continues ???
				
				if (sourceFile.isFile()) {
					
					if (isVerboseEnabled()) {
						verbose("Reading source PDF file " + sourceFile);
					}

					int sourcePageIndex = 0;
					
					PdfReader sourcePdfReader = new PdfReader(sourceFile.getCanonicalPath());
					PdfReaderHelper sourcePdfReaderHelper = new PdfReaderHelper( sourcePdfReader );
					if (isVerboseEnabled()) {
						verbose("PDF reader is " + sourcePdfReader);
						verbose("PDF reader helper is " + sourcePdfReaderHelper);
					}
					
					String currentSourcePdfTitle = FilenameUtils.getBaseName(sourceFile.getName());
					String currentSourcePdfAuthor = getSystemUserName();
					if (isVerboseEnabled()) {
						verbose("PDF title is " + currentSourcePdfTitle);
						verbose("PDF author is " + currentSourcePdfAuthor);
					}
					
					currentSourcePdfTitle = sourcePdfReaderHelper.getDocumentTitle( currentSourcePdfTitle );
					currentSourcePdfAuthor = sourcePdfReaderHelper.getDocumentTitle( currentSourcePdfAuthor );
					if (isVerboseEnabled()) {
						verbose("PDF info title is " + currentSourcePdfTitle);
						verbose("PDF info author is " + currentSourcePdfAuthor);
					}

					boolean firstPageOfCurrentSource = true;
					
					int maxSourcePages = sourcePdfReader.getNumberOfPages();
					if (isVerboseEnabled()) {
						verbose("There are " + maxSourcePages + " page(s) in source PDF file " + sourceFile);
					}

					// process all pages from source doc
					while (sourcePageIndex < maxSourcePages) {

						// add new page to current document
						outputDocument.newPage();
						
						outputPageCount++;
						if (isVerboseEnabled()) {
							verbose("Building output PDF page " + outputPageCount + " ...");
						}
						
						// add first page of current source to TOC listener
						if (firstPageOfCurrentSource) {
							int currentPageIndex = outputPageCount;
							if (tocRowChangeListener != null) {
								TocRow tocEntry = new TocRow( currentSourcePdfTitle, currentPageIndex );
								tocRowChangeListener.addTocRow(tocEntry);
								if (isVerboseEnabled()) {
									verbose("Added TOC entry " + tocEntry + " to listener");
								}
							}
							firstPageOfCurrentSource = false;
						}
						
						// extract first page from source document
						sourcePageIndex++;
						if (isVerboseEnabled()) {
							verbose("Adding page " + sourcePageIndex + " of " + maxSourcePages + " from source to output");
						}
						PdfImportedPage page1 = pdfWriter.getImportedPage( sourcePdfReader, sourcePageIndex );
						
						// n-up is 1
						if (config.getNup() == 1) {
							// add first page to top half of current page
							// TODO - review magic transformation matrix numbers and offsets
							// TODO - calculate scaling/transform based on page rect and template rect
							float p1a = 0.65f;
							float p1b = 0;
							float p1c = 0;
							float p1d = 0.65f;
							float p1e = 20;
							float p1f = 160;
							pdfContent.addTemplate( page1, p1a, p1b, p1c, p1d, p1e, p1f );

						// n-up is 2 (default)
						} else {
							
							// add first page to top half of current page
							// TODO - review magic transformation matrix numbers and offsets
							float p1a = 0.5f;
							float p1b = 0;
							float p1c = 0;
							float p1d = 0.5f;
							float p1e = (125);
							float p1f = ((pageWidth / 2) + 120 + 20);
							pdfContent.addTemplate( page1, p1a, p1b, p1c, p1d, p1e, p1f );

							// extract second page from source document ?
							PdfImportedPage page2 = null;
							if (sourcePageIndex < maxSourcePages) {
								sourcePageIndex++;
								if (isVerboseEnabled()) {
									verbose("Adding page " + sourcePageIndex + " of " + maxSourcePages + " from source to output");
								}
								page2 = pdfWriter.getImportedPage( sourcePdfReader, sourcePageIndex );
							}
							
							// add second page to bottom half of current page
							if (page2 != null) {
								// TODO - review magic transformation matrix numbers and offsets
								float p2a = 0.5f; 
								float p2b = 0;
								float p2c = 0;
								float p2d = 0.5f;
								float p2e = 125;
								float p2f = 120;
								pdfContent.addTemplate( page2, p2a, p2b, p2c, p2d, p2e, p2f );
							}
						}


						/*
						// add first page to top half of current page
						// TODO - review magic transformation matrix numbers and offsets
						float p1a = 0.5f;
						float p1b = 0;
						float p1c = 0;
						float p1d = 0.5f;
						float p1e = (125);
						float p1f = ((pageWidth / 2) + 120 + 20);
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
							pdfContent.addTemplate( page2, p2a, p2b, p2c, p2d, p2e, p2f );
						}
						*/

						// Add current page number to page footer
						String pageCountLabel = "Page " + outputPageCount;
						pdfContent.beginText();
						pdfContent.setFontAndSize( pageLabelFont, PdfBookBuilderConfig.DEFAULT_FONT_SIZE );
						pdfContent.showTextAligned( PdfContentByte.ALIGN_CENTER, pageCountLabel, (pageWidth / 2), 40, 0 );
						pdfContent.endText();
					}
					
					if (isVerboseEnabled()) {
						verbose("Finished reading " + maxSourcePages + " page(s) from source PDF file " + sourceFile);
					}

					// update progress
					if (isVerboseEnabled()) {
						if (progressMonitor != null) {
							int fileProgressPercentage = MathHelper.calculatePercentage(currentSourceFileIndex, maxSourceFileIndex);
							progressMonitor.setProgressPercentage(fileProgressPercentage);
						}
					}
				}
			}

			// close document
			outputDocument.close();
			
			if (isVerboseEnabled()) {
				verbose("Output PDF file " + outputFile + " contains " + outputPageCount + " page(s)" );
			}
			
			// TODO - output ODT page stats summary
			
		} catch (Exception e) {

			String msg = "Error building PDF book: " + e.getMessage();
			e.printStackTrace( System.err );
			System.err.println(msg);

		}
		
	}
	
	private boolean isVerboseEnabled() {
		return getConfig().isVerboseEnabled();
	}
	
	private boolean isDebugEnabled() {
		return getConfig().isDebugEnabled();
	}
	
	private void verbose( String msg ) {
		if (isVerboseEnabled()) {
			System.out.println( msg );
		}
	}
	
	private void debug( String msg ) {
		if (isDebugEnabled()) {
			System.out.println( "-- " + msg );
		}
	}
	
	private String getSystemUserName() {
		return System.getProperty( "user.name" );
	}

}
