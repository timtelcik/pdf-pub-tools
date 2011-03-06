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
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import net.mitnet.tools.pdf.book.io.FileHelper;
import net.mitnet.tools.pdf.book.io.FileNameHelper;
import net.mitnet.tools.pdf.book.model.toc.Toc;
import net.mitnet.tools.pdf.book.model.toc.TocBuilder;
import net.mitnet.tools.pdf.book.model.toc.TocTemplateDataBuilder;
import net.mitnet.tools.pdf.book.model.toc.TocTracer;
import net.mitnet.tools.pdf.book.openoffice.converter.OpenOfficeDocConverter;
import net.mitnet.tools.pdf.book.openoffice.net.OpenOfficeServerContext;
import net.mitnet.tools.pdf.book.openoffice.reports.OpenOfficeReportBuilder;
import net.mitnet.tools.pdf.book.pdf.builder.PdfBookBuilder;
import net.mitnet.tools.pdf.book.util.FileExtensionConstants;
import net.mitnet.tools.pdf.book.util.ProgressMonitor;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.SimpleBookmark;
import com.lowagie.toolbox.plugins.HtmlBookmarks;
import com.lowagie.toolbox.plugins.InspectPDF;
import com.lowagie.toolbox.plugins.XML2Bookmarks;


/**
 * Book Publisher.
 * 
 * TODO: Review process to create NUP PDF books with TOC and embedded bookmarks
 * TODO: Refactor class variables into config class
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see com.lowagie.toolbox.plugins.Handouts
 * @see com.lowagie.toolbox.plugins.NUp
 * @see HtmlBookmarks
 * @see InspectPDF
 * @see SimpleBookmark
 * @see XML2Bookmarks
 */
public class BookPublisher {
	
	public static final String DEFAULT_TOC_TEMPLATE_PATH = "resources/reports/templates/toc-template.odt";
	
	private OpenOfficeServerContext serverContext = null;
	private boolean verbose = false;
	private Rectangle pageSize = null;
	private String metaTitle = null;
	private String metaAuthor = null;
	// private boolean buildTocEnabled = false;
	private boolean buildTocEnabled = true;
	private String tocTemplatePath = null;
	
	
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
	
	public boolean isBuildTocEnabled() {
		return buildTocEnabled;
	}

	public void setBuildTocEnabled(boolean buildTocEnabled) {
		this.buildTocEnabled = buildTocEnabled;
	}

	public void setTocTemplatePath(String tocTemplatePath) {
		this.tocTemplatePath = tocTemplatePath;
	}
	
	public String getTocTemplatePath() {
		return tocTemplatePath;
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
			debug("sourceDir: " + sourceDir);
			debug("outputDir: " + outputDir);
			debug("outputBookFile: " + outputBookFile);
			debug("progresMonitor: " + progresMonitor);
		}
		
		// Init
		File tempDir = FileHelper.getSystemTempDir();
		if (isVerbose()) {
			debug("tempDir: " + tempDir);
		}
		
		// Convert Open Office documents to PDF
		OpenOfficeDocConverter openOfficeDocConverter = new OpenOfficeDocConverter(this.serverContext);
		openOfficeDocConverter.setVerbose(isVerbose());
		openOfficeDocConverter.convertDocuments(sourceDir, outputDir, OpenOfficeDocConverter.OUTPUT_FORMAT_PDF, progresMonitor);

		// Prepare TOC ?
		TocBuilder tocBuilder = null;
		if (isBuildTocEnabled()) {
			tocBuilder = new TocBuilder();
			if (isVerbose()) {
				debug("tocBuilder: " + tocBuilder);
			}
		}
		
		// Build PDF book
		File pdfSourceDir = outputDir;
		PdfBookBuilder pdfBookBuilder = new PdfBookBuilder(getPageSize());
		pdfBookBuilder.setVerbose(isVerbose());
		pdfBookBuilder.setMetaTitle(getMetaTitle());
		pdfBookBuilder.setMetaAuthor(getMetaAuthor());
		pdfBookBuilder.buildBook( pdfSourceDir, outputBookFile, progresMonitor, tocBuilder );

		// Build TOC doc
		if (isBuildTocEnabled()) {
			
			// Trace TOC
			Toc toc = tocBuilder.getToc();
			if (isVerbose()) {
				debug("Output PDF Table Of Contents is " + toc );
				debug("Output PDF Table Of Contents contains " + toc.getTocEntryCount() + " entries" );
				TocTracer.traceToc(toc);
			}
			
			// Build TOC PDF
			File tocTemplateFile = getTocTemplateFile();
			File tocOutputFile = new File( tempDir, getTempTocFileName() );
			buildTocDoc( toc, tocTemplateFile, tocOutputFile );
			File tocSourceFile = tocOutputFile;
			openOfficeDocConverter.convertDocument(tocSourceFile, tempDir, OpenOfficeDocConverter.OUTPUT_FORMAT_PDF, progresMonitor);
			
			// Merge TOC PDF with book PDF
			if (isVerbose()) {
				debug("Merging TOC with book");
			}
			String firstPdfName = FileNameHelper.rewriteFileNameSuffix(tocSourceFile,FileExtensionConstants.PDF);
			File firstPdf = new File(tempDir,firstPdfName);
			File secondPdf = outputBookFile;
			String concatName = FileNameHelper.rewriteFileNameSuffix(outputBookFile,"-plus-toc",FileExtensionConstants.PDF);
			File concatPdf = new File(outputBookFile.getParent(),concatName);
			concatPdf(firstPdf, secondPdf, concatPdf);
			if (concatPdf.exists()) {
				FileUtils.copyFile(concatPdf, outputBookFile);
				FileUtils.deleteQuietly(concatPdf);
			}
			// TODO - cleanup temp files
		}
		
		// TODO - copy/add meta data to output book PDF
		
		// TODO - add bookmarks to output book PDF
	}
	
	private String getTempTocFileName() {
		return "toc" + FileExtensionConstants.OPEN_OFFICE_DOCUMENT; 
	}

	private File getTocTemplateFile() throws IOException {
		
		File tocTemplateFile = null;
		String templatePath = getTocTemplatePath();
		if (StringUtils.isEmpty(templatePath)) {
			templatePath = DEFAULT_TOC_TEMPLATE_PATH;
			URL tocTemplateUrl = getClass().getClassLoader().getResource(templatePath);
			if (tocTemplateUrl != null) {
				tocTemplateFile = new File(tocTemplateUrl.getFile());
			}
		} else {
			tocTemplateFile = new File( templatePath );
		}
		
		return tocTemplateFile;
	}
	
	private void buildTocDoc( Toc toc, File tocTemplateFile, File tocOutputFile ) throws Exception {
		
		if (isVerbose()) {
			debug("Building TOC doc" );
			debug("Template file is " + tocTemplateFile);
			debug("TOC output file is " + tocOutputFile);
		}
		
		if ((tocTemplateFile != null) && (tocOutputFile != null)) {
			if (tocTemplateFile.exists()) {
				Map tocTemplateDataMap = TocTemplateDataBuilder.buildTocTemplateData(toc);
				if (isVerbose()) {
					debug("tocTemplateDataMap: " + tocTemplateDataMap);
					debug("Generating TOC report" );
				}
				OpenOfficeReportBuilder reportBuilder = new OpenOfficeReportBuilder();
				reportBuilder.setVerbose(isVerbose());
				reportBuilder.buildReport(tocTemplateFile, tocTemplateDataMap, tocOutputFile);
			} else {
				System.err.println("Template file " + tocTemplateFile + " does not exist - skipping TOC build phase");
			}
		}
	}
	
	/**
	 * TODO: review concat process and compare to PdfCopy.
	 */
	public void concatPdf( File firstPdf, File secondPdf, File concatPdf ) throws IOException, DocumentException {
		if (isVerbose()) {
			debug("firstPdf: " + firstPdf);
			debug("secondPdf: " + secondPdf);
			debug("concatPdf: " + concatPdf);
			debug("concat PDFs");
		}

		PdfReader firstReader = new PdfReader(firstPdf.getPath());
		PdfReader secondReader = new PdfReader(secondPdf.getPath());
		PdfCopyFields copy = new PdfCopyFields(new FileOutputStream(concatPdf.getPath()));
		copy.addDocument(firstReader);
		copy.addDocument(secondReader);
		copy.close();
	}
	
	private void debug( String msg ) {
		if (isVerbose()) {
			System.out.println("-- " + msg);
		}
	}

}
