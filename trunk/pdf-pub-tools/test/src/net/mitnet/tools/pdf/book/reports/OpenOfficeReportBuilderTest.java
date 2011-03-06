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

package net.mitnet.tools.pdf.book.reports;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.mitnet.tools.pdf.book.model.toc.Toc;
import net.mitnet.tools.pdf.book.model.toc.TocEntry;
import net.mitnet.tools.pdf.book.model.toc.TocTemplateDataBuilder;
import net.mitnet.tools.pdf.book.openoffice.reports.OpenOfficeReportBuilder;
import net.sf.jooreports.templates.DocumentTemplateException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;


/**
 * Open Office Report Builder Test.
 * 
 * TODO: review constants
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */ 
public class OpenOfficeReportBuilderTest extends TestCase {
	
	private File homeDir = new File( System.getProperty( "user.home" ) );
	
	private File baseDir = new File( homeDir, "spool/reports/demo" );
	
	private File reportsBaseDir = null; 
	
	private File tempDir = new File( System.getProperty( "java.io.tmpdir" ) );
	
	private File reportsTempDir = null; 
	
	
	public OpenOfficeReportBuilderTest(String name) {
		super(name);
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		// File reportsBaseDir = getReportsBaseDir();
		
		this.reportsBaseDir = getReportsBaseDir();
		System.out.println("reportsBaseDir: " + reportsBaseDir);
		
		this.reportsTempDir = getReportsTempDir();
		System.out.println("reportsTempDir: " + reportsTempDir);
		
		System.out.println("make dir path" + reportsTempDir);
		this.reportsTempDir.mkdirs();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	public void testBuildOrderReport() throws Exception {
		
		System.out.println("--");
		System.out.println("-- TEST BULD ORDER REPORT");
		System.out.println("--");
		
		// File demoDir = new File( this.baseDir, "order" );
		File demoDir = new File( this.reportsBaseDir, "order" );
		
		System.out.println("demoDir: " + demoDir);
		
		File templateFile = new File( demoDir, "order-template.odt" );
		System.out.println("templateFile: " + templateFile);
		
		File dataFile = new File( demoDir, "order-data.xml" );
		System.out.println("dataFile: " + dataFile);
		
		// File outputFile = new File( demoDir, "order.odt" );
		File outputFile = new File( this.tempDir, "order.odt" );
		System.out.println("outputFile" + outputFile);
		
		OpenOfficeReportBuilder reportBuilder = new OpenOfficeReportBuilder();
		reportBuilder.buildReport(templateFile, dataFile, outputFile);
	}
	
	public void testBuildToc() throws Exception {
		
		System.out.println("--");
		System.out.println("-- TEST BULD TOC");
		System.out.println("--");
		
		File demoDir = new File( this.baseDir, "toc" );
		System.out.println("demoDir: " + demoDir);
		
		File templateFile = new File( demoDir, "toc-template.odt" );
		System.out.println("templateFile: " + templateFile);
		
		File dataFile = new File( demoDir, "toc-data.xml" );
		System.out.println("dataFile: " + dataFile);
		
		// File outputFile = new File( demoDir, "toc-from-file.odt" );
		File outputFile = new File( this.reportsTempDir, "toc-from-file.odt" );
		System.out.println("outputFile: " + outputFile);
		
		OpenOfficeReportBuilder reportBuilder = new OpenOfficeReportBuilder();
		reportBuilder.buildReport(templateFile, dataFile, outputFile);
	}
	
	public void testBuildTocFromMap() throws Exception {
		
		System.out.println("--");
		System.out.println("-- TEST BULD TOC FROM MAP");
		System.out.println("--");
		
		File demoDir = new File( this.baseDir, "toc" );
		System.out.println("demoDir: " + demoDir);
		
		File templateFile = new File( demoDir, "toc-template.odt" );
		System.out.println("templateFile: " + templateFile);
		
		// File dataFile = new File( demoDir, "toc-data.xml" );
		// System.out.println("dataFile: " + dataFile);
		Map templateData = createSampleTocTemplateData();
		System.out.println("templateData: " + templateData);
		
		// File outputFile = new File( demoDir, "toc-from-map.odt" );
		File outputFile = new File( this.reportsTempDir, "toc-from-map.odt" );
		System.out.println("outputFile: " + outputFile);
		
		OpenOfficeReportBuilder reportBuilder = new OpenOfficeReportBuilder();
		reportBuilder.buildReport(templateFile, templateData, outputFile);
	}
	
	private Map createSampleTocTemplateData() throws IOException, DocumentTemplateException {
		
		Toc toc = new Toc();
		
		TocEntry tocEntry = null;
		
		tocEntry = new TocEntry( "TITLE AAA", 100 );
		toc.addTocEntry(tocEntry);
		
		tocEntry = new TocEntry( "TITLE BBB", 200 );
		toc.addTocEntry(tocEntry);
		
		tocEntry = new TocEntry( "TITLE CCC", 300 );
		toc.addTocEntry(tocEntry);
		
		System.out.println("toc: " + toc);
		
		Map tocTemplateDataMap = TocTemplateDataBuilder.buildTocTemplateData(toc);
		
		System.out.println("tocTemplateDataMap: " + tocTemplateDataMap);
		
		return tocTemplateDataMap;
	}
	
	private File getReportsBaseDir() throws IOException {
		File baseDir = null;
		URL reportsUrl = getClass().getClassLoader().getResource("reports");
		System.out.println("reportsUrl: " + reportsUrl);
		if (reportsUrl != null) {
			String urlString = reportsUrl.toExternalForm();
			baseDir = new File( urlString);
		}
		System.out.println("baseDir: " + baseDir);
		return baseDir;
	}
	
	private File getReportsTempDir() throws IOException {
		File tempDir = new File( this.tempDir, "reports" );
		System.out.println("tempDir: " + tempDir);
		return tempDir;
	}	
	
	public static TestSuite suite() {
		 TestSuite suite = new TestSuite();
		 // suite.addTest(new OpenOfficeReportBuilderTest("testBuildOrderReport"));
		 suite.addTest(new OpenOfficeReportBuilderTest("testBuildToc"));
		 suite.addTest(new OpenOfficeReportBuilderTest("testBuildTocFromMap"));
		 return suite;
	}

}

