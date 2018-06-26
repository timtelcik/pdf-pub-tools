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

package net.mitnet.tools.pdf.book.openoffice;

import java.io.File;

import junit.framework.TestCase;
import net.mitnet.tools.pdf.book.common.cli.ConsoleProgressMonitor;
import net.mitnet.tools.pdf.book.openoffice.converter.OpenOfficeDocConverter;
import net.mitnet.tools.pdf.book.openoffice.net.OpenOfficeServerContext;
import net.mitnet.tools.pdf.book.test.TestConstants;
import net.mitnet.tools.pdf.book.util.ProgressMonitor;


/**
 * Open Office Document Converter Test.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see OpenOfficeDocConverter
 */
public class OpenOfficeDocConverterTest extends TestCase {
	
	public OpenOfficeDocConverterTest(String name) {
		super(name);
	}

	
	protected void setUp() throws Exception {
		super.setUp();
	}

	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	// TODO: Revise test case with relocatable data
	public void testConvertDocuments() throws Exception {
		
		OpenOfficeServerContext serverContext = new OpenOfficeServerContext();
		
		File sourceDir = TestConstants.BOOK_SOURCE_DIR;
		System.out.println( "sourceDir: " + sourceDir);
		if (!sourceDir.exists()) {
			//throw new Exception( "Source dir does not exist: " + sourceDir );
			System.out.println( "Source dir does not exist: " + sourceDir );
			return;
		}
		
		// File outputDir = null;
		File outputDir = TestConstants.BOOK_OUTPUT_DIR;
		if (!outputDir.exists()) {
			System.out.println("making dir " + outputDir);
			outputDir.mkdirs();
		}
		
		if (sourceDir.exists()) {
			System.out.println( "Converting files in dir " + sourceDir + " ..." );
			ProgressMonitor progressMonitor = new ConsoleProgressMonitor();
			OpenOfficeDocConverter docConverter = new OpenOfficeDocConverter(serverContext);
			// docConverter.convertDocuments( sourceFileList, outputDir, PublisherConstants.OUTPUT_FORMAT_PDF, progressMonitor );
			docConverter.setVerboseEnabled(true);
			docConverter.setProgressMonitor(progressMonitor);
			docConverter.convertDocuments( sourceDir, outputDir, OpenOfficeDocConverter.OUTPUT_FORMAT_PDF );
		}
		
	}

}
