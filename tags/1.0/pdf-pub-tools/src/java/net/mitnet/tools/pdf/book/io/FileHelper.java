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
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;


/**
 * File Helper.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * @author Rich Sezov <sezovr@gmail.com>
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
	
	
	/**
	 * Get a list of all file/directories in a directory structure, filtering
	 * by the extension of the file. This method also ensures that the files 
	 * are returned in the proper order on all operating systems. 
	 * @param startDir
	 * @param fileExtensions
	 * @param recursive
	 * @return List<File>
	 */
	public static List<File> findFilesByExtensions( File startDir, String[] fileExtensions, boolean recursive ) {
		//List<File> fileCollection = (List<File>) FileUtils.listFiles( startDir, fileExtensions, recursive );
		// Below algorithm is necessary to ensure proper file order 
		// on all operating systems. 
		List<File> fileCollection = getFileListingNoSort(startDir, recursive);
		Collections.sort(fileCollection);
		
		// System.out.println("fileCollection: " + fileCollection);
		List<File> fileList = new ArrayList<File>();
		for (File file : fileCollection) {
			if (checkExtensions(fileExtensions, file)) {
				fileList.add(file);
			}
		}
		
		// System.out.println("fileList: " + fileList);
		return fileList;
	}	
	
	/**
	 * Get a list of all files/directories in a directory structure. 
	 * Can optionally recurse into any number of nested directories. 
	 * @param aStartingDir
	 * @param recursive
	 * @return List<File>
	 */
	private static List<File> getFileListingNoSort(
			File aStartingDir, boolean recursive) {
		List<File> result = new ArrayList<File>();
		File[] filesAndDirs = aStartingDir.listFiles();
		List<File> filesDirs = Arrays.asList(filesAndDirs);
		for (File file : filesDirs) {
			result.add(file); //always add, even if directory
			if (!file.isFile() && recursive == true) {
				//must be a directory
				//recursive call!
				List<File> deeperList = getFileListingNoSort(file, recursive);
				result.addAll(deeperList);
			}
		}
		return result;
	}
	
	private static boolean checkExtensions (String[] fileExtensions, File file) {
		List <String> extensions = Arrays.asList(fileExtensions);
		String ext = FilenameUtils.getExtension(file.getName());
		boolean extThere = false;
		for (int i = 0; i < extensions.size(); i++) {
			if (ext.equalsIgnoreCase(extensions.get(i))) {
				extThere = true;
			}
		}
		return extThere;
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
