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


/**
 * File Helper.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see FilenameUtils
 * @see FileUtils
 */
public class FileHelper {
	
	public static final String OPEN_OFFICE_DOC_SUFFIX_NAME = "odt";
	public static final String OPEN_OFFICE_DOC_SUFFIX = "." + OPEN_OFFICE_DOC_SUFFIX_NAME;
	
	public static final String PDF_DOC_SUFFIX_NAME = "pdf";
	public static final String PDF_DOC_SUFFIX = "." + OPEN_OFFICE_DOC_SUFFIX_NAME;


	public static List<File> findOpenOfficeFiles( File startDir ) {
		return findFilesBySuffix( startDir, OPEN_OFFICE_DOC_SUFFIX_NAME, false );
	}

	public static List<File> findOpenOfficeFiles( File startDir, boolean recursive ) {
		return findFilesBySuffix( startDir, OPEN_OFFICE_DOC_SUFFIX_NAME, recursive );
	}
	
	public static List<File> findPdfFiles( File startDir ) {
		return findFilesBySuffix( startDir, PDF_DOC_SUFFIX_NAME, false );
	}

	public static List<File> findPdfFiles( File startDir, boolean recursive ) {
		return findFilesBySuffix( startDir, PDF_DOC_SUFFIX_NAME, recursive );
	}
	
	public static List<File> findFilesBySuffix( File startDir, final String fileSuffixName ) {
		return findFilesBySuffix( startDir, fileSuffixName, false );
	}
	
	public static List<File> findFilesBySuffix( File startDir, final String fileSuffixName, boolean recursive ) {
		final String[] fileExtensions = new String[] { fileSuffixName };
		@SuppressWarnings("unchecked")
		Collection<File> fileCollection = FileUtils.listFiles( startDir, fileExtensions, recursive );
		// System.out.println("fileCollection: " + fileCollection);
		List<File> fileList = new ArrayList<File>(fileCollection);
		// System.out.println("fileList: " + fileList);
		return fileList;
	}
	
}
