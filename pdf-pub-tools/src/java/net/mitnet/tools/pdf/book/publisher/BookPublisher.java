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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mitnet.tools.pdf.book.io.FileExtensionConstants;
import net.mitnet.tools.pdf.book.io.FileNameHelper;
import net.mitnet.tools.pdf.book.model.converter.TocToPdfBookmarkListConverter;
import net.mitnet.tools.pdf.book.model.converter.TocToTemplateDataMapConverter;
import net.mitnet.tools.pdf.book.model.toc.Toc;
import net.mitnet.tools.pdf.book.model.toc.TocBuilder;
import net.mitnet.tools.pdf.book.model.toc.TocTracer;
import net.mitnet.tools.pdf.book.openoffice.converter.OpenOfficeDocConverter;
import net.mitnet.tools.pdf.book.openoffice.net.OpenOfficeServerContext;
import net.mitnet.tools.pdf.book.openoffice.reports.OpenOfficeReportBuilder;
import net.mitnet.tools.pdf.book.pdf.builder.PdfBookBuilder;
import net.mitnet.tools.pdf.book.pdf.builder.PdfBookBuilderConfig;
import net.mitnet.tools.pdf.book.pdf.event.PdfPageEventLogger;
import net.mitnet.tools.pdf.book.pdf.util.PdfBookmarkBuilder;
import net.mitnet.tools.pdf.book.pdf.util.PdfMetaKeys;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfPageEvent;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.SimpleBookmark;
import com.lowagie.toolbox.plugins.HtmlBookmarks;
import com.lowagie.toolbox.plugins.InspectPDF;
import com.lowagie.toolbox.plugins.XML2Bookmarks;


/**
 * Book Publisher.
 * 
 * TODO: Review process to create NUP PDF books with TOC and embedded bookmarks.
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
	
	private static final String PDF_PUBLISHER_TOOLS_IDENT = "PDF Publisher Tools";
	private static final String PDF_FILE_PLUS_TOC_SUFFIX = "-plus-toc";
	private static final String DEFAULT_TOC_TEMPLATE_FILE_NAME = "toc-template";
	private static final String DEFAULT_TOC_FILE_NAME = "toc";
	private static final String DEFAULT_BOOKMARK_FILE_NAME = "bookmark";
	private static final String DEFAULT_PDF_BOOK_FILE_NAME = "book";
	
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
	 * Publishes OpenOffice source documents into a single PDF booklet.
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
		
		if (isVerboseEnabled()) {
			verbose("Publishing from \"" + sourceDir + " to \"" + outputDir + "\" as book \"" + outputBookFile + "\" ...");
		}
		
		if (isDebugEnabled()) {
			debug("sourceDir: " + sourceDir);
			debug("outputDir: " + outputDir);
			debug("outputBookFile: " + outputBookFile);
			debug("progressMonitor: " + getConfig().getProgressMonitor());
		}

		
		// Init doc converter
		
		OpenOfficeDocConverter openOfficeDocConverter = createOpenOfficeDocConverter();
		

		// Convert Open Office documents to PDF
		
		openOfficeDocConverter.convertDocuments( sourceDir, outputDir, OpenOfficeDocConverter.OUTPUT_FORMAT_PDF );

		
		// Build PDF book
		
		TocBuilder tocBuilder = null;
		if (getConfig().isBuildTocEnabled()) {
			tocBuilder = new TocBuilder();
		}
		if (isDebugEnabled()) {
			debug("tocBuilder: " + tocBuilder);
		}		
		File pdfSourceDir = outputDir;		
		buildPdfBook( pdfSourceDir, outputBookFile, tocBuilder );


		// Update book with TOC

		if (getConfig().isBuildTocEnabled()) {

			Toc toc = tocBuilder.getToc();
			if (isVerboseEnabled()) {
				verbose("Output PDF Table Of Contents contains " + toc.getTocRowCount() + " entries" );
			}
			if (isDebugEnabled()) {
				debug("Output PDF Table Of Contents is " + toc );
				TocTracer.traceToc(toc);
			}
			updatePdfBookWithToc( openOfficeDocConverter, toc, outputBookFile );
		}
		
		
		// Update book with meta-data
		updatePdfBookWithMeta( outputBookFile );
		
	}

	
	/**
	 * Build PDF book.
	 * 
	 * @param sourceDir
	 * @param outputFile
	 * @throws Exception
	 */
	private void buildPdfBook( File sourceDir, File outputFile, TocBuilder tocBuilder ) throws Exception {

		PdfBookBuilderConfig pdfConfig = new PdfBookBuilderConfig();
		PdfPageEvent pdfPageEventListener = new PdfPageEventLogger();

		PdfBookBuilder pdfBookBuilder = new PdfBookBuilder();
		pdfConfig.setPageSize(getConfig().getPageSize());
		pdfConfig.setVerboseEnabled(getConfig().isVerboseEnabled());
		pdfConfig.setMetaTitle(getConfig().getMetaTitle());
		pdfConfig.setMetaAuthor(getConfig().getMetaAuthor());
		pdfConfig.setProgressMonitor(getConfig().getProgressMonitor());
		pdfConfig.setTocRowChangeListener(tocBuilder);
		pdfConfig.setPdfPageEventListener(pdfPageEventListener);
		
		pdfBookBuilder.setConfig(pdfConfig);
		pdfBookBuilder.buildBook( sourceDir, outputFile );
	}

	
	/**
	 * Create Open Office Document Converter.
	 * 
	 * @return
	 * @throws Exception
	 */
	private OpenOfficeDocConverter createOpenOfficeDocConverter() throws Exception {
		
		OpenOfficeDocConverter openOfficeDocConverter = new OpenOfficeDocConverter(getConfig().getServerContext());
		openOfficeDocConverter.setDebugEnabled(isDebugEnabled());
		openOfficeDocConverter.setVerboseEnabled(isVerboseEnabled());
		openOfficeDocConverter.setProgressMonitor(getConfig().getProgressMonitor());
		
		return openOfficeDocConverter;
	}
	

	/**
	 * Build TOC PDF document.
	 * 
	 * @param inputPdfFile
	 * @throws Exception
	 */
	private void buildTocPdf( OpenOfficeDocConverter openOfficeDocConverter, Toc toc, File outputFile ) throws Exception {
		
		// find template file
		File tocTemplateFile = resolveTocTemplateFile();
		debug("tocTemplateFile: " + tocTemplateFile);

		// build TOC doc using template and TOC data
		File tocDocFile = File.createTempFile(DEFAULT_TOC_FILE_NAME, FileExtensionConstants.OPEN_DOC_TEXT_EXTENSION);
		debug("tocDocFile: " + tocDocFile);
		buildTocDoc( tocTemplateFile, toc, tocDocFile );
		
		// convert TOC doc to PDF
		openOfficeDocConverter.convertDocument( tocDocFile, outputFile, OpenOfficeDocConverter.OUTPUT_FORMAT_PDF );
		
	}
	

	/**
	 * TODO: refactor to PdfTocPageBuilder
	 * 
	 * @param openOfficeDocConverter
	 * @param toc
	 * @param outputBookFile
	 * @throws Exception
	 */
	private void updatePdfBookWithToc( OpenOfficeDocConverter openOfficeDocConverter, Toc toc, File inputBookFile ) throws Exception {
		
		debug("openOfficeDocConverter: " + openOfficeDocConverter);
		debug("toc: " + toc);
		debug("inputBookFile: " + inputBookFile);
		
		File outputBookFile = inputBookFile;
		debug("outputBookFile: " + outputBookFile);
		

		// Build TOC PDF document

		File tocPdfFile = File.createTempFile(DEFAULT_TOC_FILE_NAME, FileExtensionConstants.PDF_EXTENSION);
		debug("tocPdfFile: " + tocPdfFile);
		buildTocPdf( openOfficeDocConverter, toc, tocPdfFile );


		// Count pages in TOC PDF document
		
		int tocDocPageCount = countPdfPages(tocPdfFile);
		debug("tocDocPageCount: " + tocDocPageCount);
		

		// Merge TOC PDF doc with PDF book

		if (isVerboseEnabled()) {
			verbose("Merging TOC with book");
		}
		File firstPdf = tocPdfFile;
		File secondPdf = outputBookFile;
		String concatName = FileNameHelper.rewriteFileNameSuffix(outputBookFile,PDF_FILE_PLUS_TOC_SUFFIX,FileExtensionConstants.PDF_EXTENSION);
		File concatPdf = new File(outputBookFile.getParent(),concatName);
		concatPdf(firstPdf, secondPdf, concatPdf);
		debug("concatPdf: " + concatPdf);
		if (concatPdf.exists()) {
			FileUtils.copyFile(concatPdf, outputBookFile);
			FileUtils.deleteQuietly(concatPdf);
		}
		debug("outputBookFile: " + outputBookFile);
		

		// Add TOC bookmarks to PDF book

		File inputPdfFile = outputBookFile;
		File bookmarkPdfFile = File.createTempFile(DEFAULT_BOOKMARK_FILE_NAME, FileExtensionConstants.OPEN_DOC_TEXT_EXTENSION);
		debug("bookmarkPdfFile: " + bookmarkPdfFile);
		List<HashMap<String, Object>> bookmarks = TocToPdfBookmarkListConverter.convert(toc);
		debug("bookmarks: " + bookmarks);
		debug("shifting page numbers by " + tocDocPageCount + " pages");
		SimpleBookmark.shiftPageNumbers(bookmarks, tocDocPageCount, null);
		debug("bookmarks: " + bookmarks);
		PdfBookmarkBuilder pdfBookmarkBuilder = new PdfBookmarkBuilder();
		pdfBookmarkBuilder.addBookmarks(inputPdfFile, bookmarks, bookmarkPdfFile);
		if (bookmarkPdfFile.exists()) {
			FileUtils.copyFile(bookmarkPdfFile, outputBookFile);
			FileUtils.deleteQuietly(bookmarkPdfFile);
		}
	}
	

	/**
	 * Update PDF book with meta-data.
	 * 
	 * @see http://itext-general.2136553.n4.nabble.com/how-to-add-meta-information-to-a-document-that-is-closed-td2137179.html
	 * @see com.lowagie.examples.general.copystamp.AddWatermarkPageNumbers
	 */
	private void updatePdfBookWithMeta( File pdfBookFile ) throws IOException, DocumentException {
		
		info("updating PDF book with meta ...");
		
		File inputPdfFile = pdfBookFile;
		String inputPdfFilename = inputPdfFile.getAbsolutePath();
		File outputPdfFile = File.createTempFile(DEFAULT_PDF_BOOK_FILE_NAME, FileExtensionConstants.PDF_EXTENSION);
		debug("outputPdfFile: " + outputPdfFile);
		String outputPdfFilename = outputPdfFile.getAbsolutePath();
		
		PdfReader reader = new PdfReader(inputPdfFilename);

		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputPdfFilename));
		
        HashMap metaMap = new HashMap();

        String metaTitle = config.getMetaTitle();
        addNonEmptyMapValue( metaMap, PdfMetaKeys.TITLE, metaTitle );
        
        String metaSubject = config.getMetaSubject();
        addNonEmptyMapValue( metaMap, PdfMetaKeys.SUBJECT, metaSubject );
        
        String metaKeywords = config.getMetaKeywords();
        addNonEmptyMapValue( metaMap, PdfMetaKeys.KEYWORDS, metaKeywords );
        
        String metaCreator = PDF_PUBLISHER_TOOLS_IDENT + " with " + Document.getVersion();
        addNonEmptyMapValue( metaMap, PdfMetaKeys.CREATOR, metaCreator );
        
        String metaAuthor = config.getMetaAuthor();
        addNonEmptyMapValue( metaMap, PdfMetaKeys.AUTHOR, metaAuthor );
        
        String metaVersionId = config.getMetaVersionId();
        addNonEmptyMapValue( metaMap, PdfMetaKeys.VERSION_ID, metaVersionId );
        
        debug("updating PDF book with meta map: " + metaMap);
        
        stamper.setMoreInfo( metaMap ); 
		
		stamper.close();
		
		if (outputPdfFile.exists()) {
			FileUtils.copyFile(outputPdfFile, pdfBookFile);
			FileUtils.deleteQuietly(outputPdfFile);
		}
	}
	
	
	// TODO - review template file resolution and temp file allocation
	private File resolveTocTemplateFile() throws IOException {
		
		File tocTemplateFile = null;
		
		String templatePath = getConfig().getTocTemplatePath();
		if (isDebugEnabled()) {
			debug("templatePath: " + templatePath);
		}

		if (StringUtils.isEmpty(templatePath)) {
			templatePath = BookPublisherConfig.DEFAULT_TOC_TEMPLATE_PATH;
			if (isDebugEnabled()) {
				debug("templatePath: " + templatePath);
			}
			URL tocTemplateUrl = getClass().getClassLoader().getResource(templatePath);
			if (isDebugEnabled()) {
				debug("tocTemplateUrl: " + tocTemplateUrl);
			}			
			if (tocTemplateUrl != null) {
				tocTemplateFile = File.createTempFile(DEFAULT_TOC_TEMPLATE_FILE_NAME, FileExtensionConstants.OPEN_DOC_TEXT_EXTENSION);
				tocTemplateFile.deleteOnExit();
				FileUtils.copyURLToFile(tocTemplateUrl, tocTemplateFile); 
			}
		} else {
			tocTemplateFile = new File( templatePath );
		}
		
		if (isDebugEnabled()) {
			debug("tocTemplateFile: " + tocTemplateFile);
		}
		
		return tocTemplateFile;
	}
	
	
	private void buildTocDoc( File tocTemplateFile, Toc toc, File tocOutputFile ) throws Exception {
		
		if (isVerboseEnabled()) {
			verbose("Building TOC doc" );
			verbose("Template file is " + tocTemplateFile);
			verbose("TOC output file is " + tocOutputFile);
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
				Map tocTemplateDataMap = TocToTemplateDataMapConverter.convert(toc);
				if (isDebugEnabled()) {
					debug("tocTemplateDataMap: " + tocTemplateDataMap);
					debug("Generating TOC report" );
				}
				OpenOfficeReportBuilder reportBuilder = new OpenOfficeReportBuilder();
				reportBuilder.setDebugEnabled(isDebugEnabled());
				reportBuilder.setVerboseEnabled(isVerboseEnabled());
				reportBuilder.buildReport(tocTemplateFile, tocTemplateDataMap, tocOutputFile);
			} else {
				error("Template file " + tocTemplateFile + " does not exist - skipping TOC build phase");
			}
		}
	}
	
	
	/**
	 * Returns the number of pages in a PDF file.
	 * 
	 * @param pdfFileName
	 * @return
	 * @throws IOException
	 * 
	 * @see http://stackoverflow.com/questions/6026971/page-count-of-pdf-with-java
	 */
	private int countPdfPages( File file ) throws IOException {
		  String filename = file.getCanonicalPath();
		  int pageCount = countPdfPages( filename );
		  return pageCount;
	}
	
	
	/**
	 * Returns the number of pages in a PDF file.
	 * 
	 * @param pdfFileName
	 * @return
	 * @throws IOException
	 * 
	 * @see http://stackoverflow.com/questions/6026971/page-count-of-pdf-with-java
	 */	
	private int countPdfPages( String filename ) throws IOException {
		  PdfReader reader = new PdfReader( filename );
		  int pageCount = reader.getNumberOfPages();
		  reader.close();
		  return pageCount;
	}

	
	/**
	 * TODO: review concat process and compare to PdfCopy.
	 */
	public void concatPdf( File firstPdf, File secondPdf, File concatPdf ) throws IOException, DocumentException {
		
		if (isDebugEnabled()) {
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
	
	
	// TODO - move to CollectionHelper
	private void addNonEmptyMapValue( Map<String,String> map, String key, String value ) {
		if (map != null) {
	        if (!StringUtils.isEmpty(key)) {
		        if (!StringUtils.isEmpty(value)) {
		        	map.put( key, value );	
		        }
	        }
		}
	}	
	
	
	private boolean isDebugEnabled() {
		return getConfig().isDebugEnabled();
	}
	
	
	private boolean isVerboseEnabled() {
		return getConfig().isVerboseEnabled();
	}
	
	
	private void debug( String msg ) {
		if (isDebugEnabled()) {
			System.out.println("-- " + msg);
		}
	}
	
	private void info( String msg ) {
		System.out.println( msg );
	}
	
	private void verbose( String msg ) {
		if (isVerboseEnabled()) {
			System.out.println( msg );
		}
	}
	
	
	private void error( String msg ) {
		if (msg != null) {
			System.err.println(msg);
		}
	}
	
	
	private void error( String msg, Exception exception ) {
		if (msg != null) {
			System.err.println(msg);
			if (exception != null) {
				exception.printStackTrace(System.err);
			}
		}
	}	
	
}
