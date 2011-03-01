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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import net.mitnet.tools.pdf.book.openoffice.reports.OpenOfficeReportBuilder;
import net.sf.jooreports.templates.DocumentTemplateException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;


/**
 * Open Office Report Builder Test.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */ 
public class OpenOfficeReportBuilderTest extends TestCase {
	
	private File homeDir = new File( System.getProperty( "user.home" ) );
	
	private File baseDir = new File( homeDir, "spool/reports/demo" );


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	public void testBuildOrderReport() throws Exception {
		
		System.out.println("-- TEST BULD ORDER REPORT");
		
		File demoDir = new File( this.baseDir, "order" );
		System.out.println("demoDir:" + demoDir);
		
		File templateFile = new File( demoDir, "order-template.odt" );
		System.out.println("templateFile:" + templateFile);
		
		File dataFile = new File( demoDir, "order-data.xml" );
		System.out.println("dataFile:" + dataFile);
		
		File outputFile = new File( demoDir, "order.odt" );
		System.out.println("outputFile" + outputFile);
		
		OpenOfficeReportBuilder reportBuilder = new OpenOfficeReportBuilder();
		reportBuilder.buildReport(templateFile, dataFile, outputFile);
	}
	
	public void testBuildToc() throws Exception {
		
		System.out.println("-- TEST BULD TOC");
		
		File demoDir = new File( this.baseDir, "toc" );
		System.out.println("demoDir:" + demoDir);
		
		File templateFile = new File( demoDir, "toc-template.odt" );
		System.out.println("templateFile:" + templateFile);
		
		File dataFile = new File( demoDir, "toc-data.xml" );
		System.out.println("dataFile:" + dataFile);
		
		File outputFile = new File( demoDir, "toc-from-file.odt" );
		System.out.println("outputFile:" + outputFile);
		
		OpenOfficeReportBuilder reportBuilder = new OpenOfficeReportBuilder();
		reportBuilder.buildReport(templateFile, dataFile, outputFile);
	}
	
	public void testBuildTocFromMap() throws Exception {
		
		System.out.println("-- TEST BULD TOC FROM MAP");
		
		File demoDir = new File( this.baseDir, "toc" );
		System.out.println("demoDir:" + demoDir);
		
		File templateFile = new File( demoDir, "toc-template.odt" );
		System.out.println("templateFile:" + templateFile);
		
		// File dataFile = new File( demoDir, "toc-data.xml" );
		// System.out.println("dataFile:" + dataFile);
		Map templateData = createSampleTocTemplateData();
		System.out.println("templateData:" + templateData);
		
		File outputFile = new File( demoDir, "toc-from-map.odt" );
		System.out.println("outputFile:" + outputFile);
		
		OpenOfficeReportBuilder reportBuilder = new OpenOfficeReportBuilder();
		reportBuilder.buildReport(templateFile, templateData, outputFile);
	}
	
    /**
     * template contains <tt>[#list items as item]</tt>
     */
	/*
	public void testScriptForRepeatingTableRow() throws IOException,
			DocumentTemplateException {
		File templateFile = getTestFile("visual-repeat-table-row-template.odt");
		Map model = new HashMap();
		List items = new ArrayList();
		model.put("items", items);
		Map one = new HashMap();
		one.put("value", new Integer(1));
		one.put("description", "one");
		items.add(one);
		Map two = new HashMap();
		two.put("value", new Integer(2));
		two.put("description", "two");
		items.add(two);
		Map three = new HashMap();
		three.put("value", new Integer(3));
		three.put("description", "three");
		items.add(three);
		String actual = processTemplate(templateFile, model);
		String expected = "one\n" + "1\n" + "two\n" + "2\n" + "three\n" + "3\n"
				+ "Total\n" + "6";
		assertEquals(expected, actual);
	}
	*/
	
    /**
     * template data contains <tt>[#list items as item]</tt>
     */
	private Map createSampleTocTemplateData() throws IOException, DocumentTemplateException {

		Map model = new HashMap();
		
		Map toc = new HashMap();
		model.put("toc", toc);
		
		List sectionList = new ArrayList();
		toc.put("section", sectionList);
		
		Map tocEntryMap = null;
		
		tocEntryMap = createTocSectionData( "TITLE AAA", 100 );
		sectionList.add(tocEntryMap);
		
		tocEntryMap = createTocSectionData( "TITLE BBB", 200 );
		sectionList.add(tocEntryMap);
		
		tocEntryMap = createTocSectionData( "TITLE CCC", 300 );
		sectionList.add(tocEntryMap);
		
		return model;
	}
	
	private Map createTocSectionData( String title, int pageNumber ) {
		Map tocEntry = new HashMap();
		tocEntry.put("title",title);
		tocEntry.put("pageNumber", new Integer(pageNumber));
		return tocEntry;
	}

}
