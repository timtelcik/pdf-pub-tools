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

package net.mitnet.tools.pdf.book.publisher.io;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;
import net.mitnet.tools.pdf.book.io.FileHelper;
import net.mitnet.tools.pdf.book.test.TestConstants;


/**
 * File Helper Test Case.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class FileHelperTest extends TestCase {

	public FileHelperTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testFindOpenOfficeFiles() throws Exception {
		
		File sourceDir = TestConstants.BOOK_SOURCE_DIR;
		System.out.println( "sourceDir: " + sourceDir);
		if (!sourceDir.exists()) {
			throw new Exception( "Source dir does not exist: " + sourceDir );
		}

		List<File> sourceFileList = FileHelper.findOpenOfficeFiles(sourceDir);
		System.out.println( "sourceFileList.size: " + sourceFileList.size());
		System.out.println( "sourceFileList: " + sourceFileList);
	}
	
	public void testFindOpenOfficeFilesRecursive() throws Exception {
		
		File sourceDir = TestConstants.BOOK_SOURCE_DIR;
		System.out.println( "sourceDir: " + sourceDir);
		if (!sourceDir.exists()) {
			throw new Exception( "Source dir does not exist: " + sourceDir );
		}

		// List<File> sourceFileList = FileHelper.findOpenOfficeFiles(sourceDir);
		List<File> sourceFileList = FileHelper.findOpenOfficeFiles(sourceDir,true);
		System.out.println( "sourceFileList.size: " + sourceFileList.size());
		System.out.println( "sourceFileList: " + sourceFileList);
	}
	
	public void testFindPdfFiles() throws Exception {
		
		File sourceDir = TestConstants.BOOK_OUTPUT_DIR;
		System.out.println( "sourceDir: " + sourceDir);
		if (!sourceDir.exists()) {
			throw new Exception( "Source dir does not exist: " + sourceDir );
		}

		List<File> sourceFileList = FileHelper.findPdfFiles(sourceDir);
		System.out.println( "sourceFileList.size: " + sourceFileList.size());
		System.out.println( "sourceFileList: " + sourceFileList);
	}

	
	public void testFindPdfFilesRecursive() throws Exception {
		
		File sourceDir = TestConstants.BOOK_OUTPUT_DIR;
		System.out.println( "sourceDir: " + sourceDir);
		if (!sourceDir.exists()) {
			throw new Exception( "Source dir does not exist: " + sourceDir );
		}

		List<File> sourceFileList = FileHelper.findPdfFiles(sourceDir,true);
		System.out.println( "sourceFileList.size: " + sourceFileList.size());
		System.out.println( "sourceFileList: " + sourceFileList);
	}

}
