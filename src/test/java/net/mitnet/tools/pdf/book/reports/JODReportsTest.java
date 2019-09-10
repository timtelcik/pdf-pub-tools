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

package net.mitnet.tools.pdf.book.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateFactory;


/**
 * JOD Reports Test.
 * 
 * @see http://jodreports.sourceforge.net/?q=node/22
 * @see net.sf.jooreports.tools.CreateDocument
 * 
 * <pre>
 * usage:
 * % java -jar jodreports-2.x.jar
 *      net.sf.jooreports.tools.CreateDocument   
 *      web/WEB-INF/templates/order-template.odt 
 *      test-data/order-data.xml 
 *      order.odt
 * </pre>
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class JODReportsTest {

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
	
	public void testReports() throws Exception {
		
		DocumentTemplateFactory documentTemplateFactory = new DocumentTemplateFactory();
		DocumentTemplate template = documentTemplateFactory.getTemplate(new File("my-template.odt"));
		Map data = new HashMap();
		data.put("var", "value");
		//...
		template.createDocument(data, new FileOutputStream("my-output.odt"));
	}

}

