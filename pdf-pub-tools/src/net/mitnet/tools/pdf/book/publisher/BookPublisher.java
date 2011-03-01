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

package net.mitnet.tools.pdf.book.publisher;

import java.io.File;

import net.mitnet.tools.pdf.book.model.TableOfContents;
import net.mitnet.tools.pdf.book.model.TableOfContentsBuilder;
import net.mitnet.tools.pdf.book.model.TableOfContentsTracer;
import net.mitnet.tools.pdf.book.openoffice.converter.OpenOfficeDocConverter;
import net.mitnet.tools.pdf.book.openoffice.net.OpenOfficeServerContext;
import net.mitnet.tools.pdf.book.pdf.builder.PdfBookBuilder;
import net.mitnet.tools.pdf.book.util.ProgressMonitor;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.SimpleBookmark;
import com.lowagie.toolbox.plugins.HtmlBookmarks;
import com.lowagie.toolbox.plugins.InspectPDF;
import com.lowagie.toolbox.plugins.XML2Bookmarks;


/**
 * Training Book Publisher.
 * 
 * TODO - review process to create NUP PDF books with TOC and embedded bookmarks
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see ExportThread
 * @see com.lowagie.toolbox.plugins.Handouts
 * @see com.lowagie.toolbox.plugins.NUp
 * @see HtmlBookmarks
 * @see InspectPDF
 * @see SimpleBookmark
 * @see XML2Bookmarks
 */
public class BookPublisher {
	
	private OpenOfficeServerContext serverContext = null;
	private boolean verbose = false;
	private Rectangle pageSize = null;
	private String metaTitle = null;
	private String metaAuthor = null;
	
	
	public BookPublisher( Rectangle pageSize ) {
		this.serverContext = new OpenOfficeServerContext();
		this.pageSize = pageSize;
	}
	
	public BookPublisher( OpenOfficeServerContext serverContext, Rectangle pageSize ) {
		this.serverContext = serverContext;
		this.pageSize = pageSize;
	}

	public void setVerbose( boolean value ) {
		this.verbose = value;
	}

	public boolean isVerbose() {
		return this.verbose;
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

	/**
	 * Publishes the OpenOffice source documents into a single PDF booklet.
	 *
	 * This method chains together the OpenOffice to PDF conversion process and PDF book assembly.
	 * 
	 * @param sourceDir
	 * @param outputDir
	 * @param outputBookFile
	 * @param progresMonitor
	 * @throws Exception
	 */
	public void publish( File sourceDir, File outputDir, File outputBookFile, ProgressMonitor progresMonitor ) throws Exception {

		
		if (isVerbose()) {
			System.out.println( "-- sourceDir: " + sourceDir);
			System.out.println( "-- outputDir: " + outputDir);
			System.out.println( "-- outputBookFile: " + outputBookFile);
			System.out.println( "-- progresMonitor: " + progresMonitor);
		}
		
		// Convert Open Office documents to PDF
		OpenOfficeDocConverter openOfficeDocConverter = new OpenOfficeDocConverter(this.serverContext);
		openOfficeDocConverter.setVerbose(isVerbose());
		openOfficeDocConverter.convertDocuments(sourceDir, outputDir, OpenOfficeDocConverter.OUTPUT_FORMAT_PDF, progresMonitor);
		
		// Build PDF book
		TableOfContentsBuilder tocBuilder = new TableOfContentsBuilder();
		if (isVerbose()) {
			System.out.println( "-- tocBuilder: " + tocBuilder);
		}
		File pdfSourceDir = outputDir;
		PdfBookBuilder pdfBookBuilder = new PdfBookBuilder(getPageSize());
		pdfBookBuilder.setVerbose(isVerbose());
		pdfBookBuilder.setMetaTitle(getMetaTitle());
		pdfBookBuilder.setMetaAuthor(getMetaAuthor());
		pdfBookBuilder.buildBook( pdfSourceDir, outputBookFile, progresMonitor, tocBuilder );
		
		if (verbose) {
			TableOfContents toc = tocBuilder.getTableOfContents();
			System.out.println( "-- Output PDF Table Of Contents is " + toc );
			System.out.println( "-- Output PDF Table Of Contents contains " + toc.getTocEntryCount() + " entries" );
			TableOfContentsTracer.traceTableOfContents(toc);
		}

		
		// TODO - output TOC as PDF
		
		
		// TODO - merge PDF TOC into output PDF book
		
		
	}

}
