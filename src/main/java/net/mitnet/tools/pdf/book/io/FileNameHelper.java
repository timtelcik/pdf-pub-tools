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

package net.mitnet.tools.pdf.book.io;

import java.io.File;

import org.apache.commons.io.FilenameUtils;


/**
 * File Name Helper.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see FilenameUtils
 */
public class FileNameHelper {

	public static String rewriteFileNameSuffix( File sourceFile, String newFileNameExtension ) {
		// String newFileName = rewriteFileNameSuffix( sourceFile.getName(), newFileNameExtension );
		String newFileName = rewriteFileNameSuffix( sourceFile.getAbsolutePath(), newFileNameExtension );
		return newFileName;
	}

	public static String rewriteFileNameSuffix( File sourceFile, String newFileNameSuffix, String newFileNameExtension ) {
		String newFileName = rewriteFileNameSuffix( sourceFile.getName(), newFileNameSuffix, newFileNameExtension );
		return newFileName;
	}

	public static String rewriteFileNameSuffix( String sourceFileName, String newFileNameExtension ) {
		String pathPath = FilenameUtils.getFullPath(sourceFileName);
		String baseFileName = FilenameUtils.getBaseName(sourceFileName);
		String newFileName = baseFileName + newFileNameExtension;
		String newFile = FilenameUtils.concat(pathPath, newFileName);
		return newFile;
	}

	public static String rewriteFileNameSuffix( String sourceFileName, String newFileNameSuffix, String newFileNameExtension ) {
		String baseFileName = FilenameUtils.getBaseName(sourceFileName);
		if (newFileNameSuffix != null) {
			baseFileName = baseFileName + newFileNameSuffix;
		}
		String newFileName = baseFileName + newFileNameExtension;
		return newFileName;
	}
	
}
