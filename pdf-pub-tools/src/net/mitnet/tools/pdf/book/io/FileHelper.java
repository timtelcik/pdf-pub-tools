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

package net.mitnet.tools.pdf.book.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;


/**
 * File Helper.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see FilenameUtils
 * @see FileUtils
 */
public class FileHelper {
	
	public static List<File> findOpenOfficeFiles( File startDir ) {
		return findOpenOfficeFiles( startDir, false );
	}

	public static List<File> findOpenOfficeFiles( File startDir, boolean recursive ) {
		String[] fileExtensions = FileExtensionConstants.OPEN_DOC_EXTENSIONS;
		return findFilesByExtensions( startDir, fileExtensions, recursive );
	}
	
	public static List<File> findOpenOfficeTextDocFiles( File startDir ) {
		return findFilesByExtension( startDir, FileExtensionConstants.OPEN_DOC_TEXT_EXTENSION_NAME, false );
	}

	public static List<File> findOpenOfficeTextDocFiles( File startDir, boolean recursive ) {
		return findFilesByExtension( startDir, FileExtensionConstants.OPEN_DOC_TEXT_EXTENSION_NAME, recursive );
	}
	
	public static List<File> findPdfFiles( File startDir ) {
		return findFilesByExtension( startDir, FileExtensionConstants.PDF_EXTENSION_NAME, false );
	}

	public static List<File> findPdfFiles( File startDir, boolean recursive ) {
		return findFilesByExtension( startDir, FileExtensionConstants.PDF_EXTENSION_NAME, recursive );
	}
	
	public static List<File> findFilesByExtension( File startDir, final String fileSuffixName ) {
		return findFilesByExtension( startDir, fileSuffixName, false );
	}
	
	public static List<File> findFilesByExtension( File startDir, final String fileExtension, boolean recursive ) {
		String[] fileExtensions = new String[] { fileExtension };
		List<File> fileList = findFilesByExtensions( startDir, fileExtensions, recursive );
		return fileList;
	}
	
	public static List<File> findFilesByExtensions( File startDir, String[] fileExtensions, boolean recursive ) {
		@SuppressWarnings("unchecked")
		Collection<File> fileCollection = FileUtils.listFiles( startDir, fileExtensions, recursive );
		// System.out.println("fileCollection: " + fileCollection);
		List<File> fileList = new ArrayList<File>(fileCollection);
		// System.out.println("fileList: " + fileList);
		return fileList;
	}	
	
	/**
	 * Returns the system temp dir.
	 */
	public static File getSystemTempDir() {
		File tempDir = new File( System.getProperty( "java.io.tmpdir" ) );
		return tempDir;
	}
	

	/**
	 * Returns the path between a file and a parent directory/folder.
	 * 
	 * NOTE: The file must be a child of the parent.
	 * 
	 * For example,
	 * if parentDir is "/home/users/nobody/spool/docs/input"
	 * and file is "/home/users/nobody/spool/docs/input/a/b/c/test.odt"
	 * the relative path will be "a/b/c"
	 * 
	 * @param parentDir parent dir
	 * @param file file relative to parent dir
	 */
	public static String getPathToParent( File parentDir, File file ) {
		
		String pathToParent = null;
		
		if (parentDir != null && file != null) {
			// String fileName = file.getName();
			// int fileNameLength = fileName.length();
			String parentDirAbsolutePath = parentDir.getAbsolutePath();
			String fileAbsolutePath = file.getAbsolutePath();
			int parentDirAbsolutePathLength = parentDirAbsolutePath.length();
			// int fileAbsolutePathLength = fileAbsolutePath.length();
			String relativeFilePath = fileAbsolutePath.substring(parentDirAbsolutePathLength);
			// String relativePath = FilenameUtils.getPathNoEndSeparator(relativeFilePath);
			String relativePath = FilenameUtils.getPath(relativeFilePath);
			if (!StringUtils.isEmpty(relativePath)) {
				pathToParent = relativePath;				
			}
		}
		
		return pathToParent;
	}
	
}
