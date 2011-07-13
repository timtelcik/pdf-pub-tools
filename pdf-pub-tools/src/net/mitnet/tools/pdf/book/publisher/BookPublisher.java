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

import net.mitnet.tools.pdf.book.io.FileExtensionConstants;
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
import net.mitnet.tools.pdf.book.pdf.builder.PdfBookBuilderConfig;
import net.mitnet.tools.pdf.book.pdf.event.PdfPageEventLogger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfPageEvent;
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
	
	private BookPublisherConfig config = new BookPublisherConfig();
	
	public BookPublisher() {
		getConfig().setServerContext(new OpenOfficeServerContext());
		getConfig().setPageSize(PageSize.A4);
	}
	
	public BookPublisher( Rectangle pageSize ) {
		getConfig().setServerContext(new OpenOfficeServerContext());
		getConfig().setPageSize(pageSize);
	}
	
	public BookPublisher( OpenOfficeServerContext serverContext, Rectangle pageSize ) {
		getConfig().setServerContext(serverContext);
		getConfig().setPageSize(pageSize);
	}
	
	public void setConfig(BookPublisherConfig config) {
		if (config == null) {
			this.config = new BookPublisherConfig();
		} else {
			this.config = config;
		}
	}
	
	public BookPublisherConfig getConfig() {
		return this.config;
	}


	/**
	 * Publishes the OpenOffice source documents into a single PDF booklet.
	 *
	 * This method chains together the OpenOffice to PDF conversion process and PDF book assembly.
	 * 
	 * TODO: refactor
	 * 
	 * @param sourceDir
	 * @param outputDir
	 * @param outputBookFile
	 * @param progresMonitor
	 * @throws Exception
	 */
	public void publish( File sourceDir, File outputDir, File outputBookFile ) throws Exception {
		
		if (isVerbose()) {
			debug("sourceDir: " + sourceDir);
			debug("outputDir: " + outputDir);
			debug("outputBookFile: " + outputBookFile);
			debug("progressMonitor: " + getConfig().getProgressMonitor());
		}
		
		// Init
		File tempDir = FileHelper.getSystemTempDir();
		if (isVerbose()) {
			debug("tempDir: " + tempDir);
		}
		
		// Convert Open Office documents to PDF
		OpenOfficeDocConverter openOfficeDocConverter = new OpenOfficeDocConverter(getConfig().getServerContext());
		openOfficeDocConverter.setTraceEnabled(isVerbose());
		openOfficeDocConverter.setProgressMonitor(getConfig().getProgressMonitor());
		openOfficeDocConverter.convertDocuments(sourceDir, outputDir, OpenOfficeDocConverter.OUTPUT_FORMAT_PDF );

		// Prepare TOC ?
		TocBuilder tocBuilder = null;
		if (getConfig().isBuildTocEnabled()) {
			tocBuilder = new TocBuilder();
			if (isVerbose()) {
				debug("tocBuilder: " + tocBuilder);
			}
		}
		
		// Build PDF book
		PdfBookBuilderConfig pdfConfig = new PdfBookBuilderConfig();
		PdfPageEvent pdfPageEventListener = new PdfPageEventLogger();

		File pdfSourceDir = outputDir;
		PdfBookBuilder pdfBookBuilder = new PdfBookBuilder();
		pdfConfig.setPageSize(getConfig().getPageSize());
		pdfConfig.setVerbose(getConfig().isVerbose());
		pdfConfig.setMetaTitle(getConfig().getMetaTitle());
		pdfConfig.setMetaAuthor(getConfig().getMetaAuthor());
		pdfConfig.setProgressMonitor(getConfig().getProgressMonitor());
		pdfConfig.setTocRowChangeListener(tocBuilder);
		pdfConfig.setPdfPageEventListener(pdfPageEventListener);
		
		pdfBookBuilder.setConfig(pdfConfig);
		pdfBookBuilder.buildBook( pdfSourceDir, outputBookFile );

		// Build TOC doc
		// TODO: refactor to PdfTocPageBuilder
		if (getConfig().isBuildTocEnabled()) {
			
			// Trace TOC
			Toc toc = tocBuilder.getToc();
			if (isVerbose()) {
				debug("Output PDF Table Of Contents is " + toc );
				debug("Output PDF Table Of Contents contains " + toc.getTocRowCount() + " entries" );
				TocTracer.traceToc(toc);
			}
			
			// Build TOC PDF
			File tocTemplateFile = getTocTemplateFile();
			debug("tocTemplateFile: " + tocTemplateFile);
			File tocOutputFile = new File( tempDir, getTempTocFileName() );
			debug("tocOutputFile: " + tocOutputFile);
			buildTocDoc( tocTemplateFile, toc, tocOutputFile );
			File tocSourceFile = tocOutputFile;
			openOfficeDocConverter.convertDocument(tocSourceFile, tempDir, OpenOfficeDocConverter.OUTPUT_FORMAT_PDF);
			
			// Merge TOC PDF with book PDF
			if (isVerbose()) {
				debug("Merging TOC with book");
			}
			String firstPdfName = FileNameHelper.rewriteFileNameSuffix(tocSourceFile,FileExtensionConstants.PDF_EXTENSION);
			File firstPdf = new File(tempDir,firstPdfName);
			File secondPdf = outputBookFile;
			String concatName = FileNameHelper.rewriteFileNameSuffix(outputBookFile,"-plus-toc",FileExtensionConstants.PDF_EXTENSION);
			File concatPdf = new File(outputBookFile.getParent(),concatName);
			concatPdf(firstPdf, secondPdf, concatPdf);
			if (concatPdf.exists()) {
				FileUtils.copyFile(concatPdf, outputBookFile);
				FileUtils.deleteQuietly(concatPdf);
			}
			// TODO - cleanup temp files
		}
		
		// TODO - copy/add meta data to output book PDF
		
		// TODO - convert TOC model to PDF bookmarks using com.itextpdf.text.pdf.SimpleBookmark and insert in output book PDF
	}
	
	private String getTempTocFileName() {
		return "toc" + FileExtensionConstants.OPEN_DOC_TEXT_EXTENSION; 
	}

	private File getTocTemplateFile() throws IOException {
		
		File tocTemplateFile = null;
		
		String templatePath = getConfig().getTocTemplatePath();
		if (isVerbose()) {
			debug("templatePath: " + templatePath);
		}

		if (StringUtils.isEmpty(templatePath)) {
			templatePath = BookPublisherConfig.DEFAULT_TOC_TEMPLATE_PATH;
			if (isVerbose()) {
				debug("templatePath: " + templatePath);
			}
			URL tocTemplateUrl = getClass().getClassLoader().getResource(templatePath);
			if (isVerbose()) {
				debug("tocTemplateUrl: " + tocTemplateUrl);
			}			
			if (tocTemplateUrl != null) {
				tocTemplateFile = new File(tocTemplateUrl.getFile());
			}
		} else {
			tocTemplateFile = new File( templatePath );
		}
		
		if (isVerbose()) {
			debug("tocTemplateFile: " + tocTemplateFile);
		}
		
		return tocTemplateFile;
	}
	
	private void buildTocDoc( File tocTemplateFile, Toc toc, File tocOutputFile ) throws Exception {
		
		if (isVerbose()) {
			debug("Building TOC doc" );
			debug("Template file is " + tocTemplateFile);
			debug("TOC output file is " + tocOutputFile);
		}
		
		// assertions
		if (tocTemplateFile == null) {
			throw new Exception("TOC template file is null");
		}
		if (tocOutputFile == null) {
			throw new Exception("TOC output file is null");
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
	
	private boolean isVerbose() {
		return getConfig().isVerbose();
	}
	
	private void debug( String msg ) {
		if (isVerbose()) {
			System.out.println("-- " + msg);
		}
	}

}
