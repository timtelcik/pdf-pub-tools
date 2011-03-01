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

package net.mitnet.tools.pdf.book.openoffice.reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.mitnet.tools.pdf.book.model.TableOfContents;
import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateFactory;

import org.apache.commons.io.FilenameUtils;

import freemarker.ext.dom.NodeModel;
import freemarker.template.Configuration;


/**
 * Open Office Report Builder.
 *
 * @see http://jodreports.sourceforge.net/?q=jooreports
 * @see http://jodreports.sourceforge.net/?q=node/22
 * @see http://jodreports.sourceforge.net/?q=node/23
 * @see http://jodreports.sourceforge.net/?q=node/26
 * @see http://jodreports.sourceforge.net/?q=node/27
 * @see http://www.oooforum.org/forum/viewtopic.phtml?t=74658 
 * @see http://books.evc-cit.info/
 * @see net.sf.jooreports.tools.CreateDocument
 * 
 * <pre>
 * usage:
 * % java -jar jodreports-2.x.jar
 *      net.sf.jooreports.tools.CreateDocument   
 *      web/WEB-INF/templates/order-template.odt 
 *      test-data/order-data.xml 
 *      order.odt
 * 
 * % java -jar jodreports-2.3.0.jar 
 *      net.sf.jooreports.tools.CreateDocument 
 *      web/WEB-INF/templates/order-template.odt 
 *      src/jodreports/src/test/resources/order-data.xml 
 *      order.odt
 * </pre>
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class OpenOfficeReportBuilder {
	
	public OpenOfficeReportBuilder() {
	}
	
	public void buildReport( String templateFile, TableOfContents toc, String outputFile ) throws Exception {
		Map<String,String> templateData = buildTemplateData( toc );
		buildReport( new File(templateFile), templateData, new File(outputFile));
	}
	
	public void buildReport( String templateFile, String dataFile, String outputFile ) throws Exception {
		buildReport( new File(templateFile), new File(dataFile), new File(outputFile));
	}

	public void buildReport( File templateFile, File dataFile, File outputFile ) throws Exception {
	      
        Object templateData = null;
        String dataFileExtension = FilenameUtils.getExtension(dataFile.getName());
		if (dataFileExtension.equals("xml")) {
			NodeModel nodeModel = NodeModel.parse(dataFile);
			templateData = nodeModel;
        } else if (dataFileExtension.equals("properties")) {
        	Properties properties = new Properties();
        	properties.load(new FileInputStream(dataFile));
        	templateData = properties;
        } else {
        	String msg = "Template data file must be 'xml' or 'properties'; unsupported type: " + dataFileExtension;
        	throw new Exception( msg );
        }
		
		buildReport( templateFile, templateData, outputFile );
    }
	
	public void buildReport( File templateFile, Object temnplateData, File outputFile ) throws Exception {
      
		System.out.println( "templateFile:" + templateFile );
		System.out.println( "temnplateData:" + temnplateData );
		System.out.println( "outputFile:" + outputFile );
		
        DocumentTemplateFactory documentTemplateFactory = new DocumentTemplateFactory();
        System.out.println( "documentTemplateFactory:" + documentTemplateFactory );
        
        Configuration freemarkerConfig = documentTemplateFactory.getFreemarkerConfiguration();
        System.out.println( "freemarkerConfig:" + freemarkerConfig );
        
        DocumentTemplate docTemplate = documentTemplateFactory.getTemplate(templateFile);
        System.out.println( "docTemplate:" + docTemplate );
        
        docTemplate.createDocument(temnplateData, new FileOutputStream(outputFile));
    }
	
	private Map<String,String> buildTemplateData( TableOfContents toc ) {
		Map<String,String> templateData = new HashMap<String,String>();
		// TODO - parse toc and fill template data
		return templateData;
	}

}

