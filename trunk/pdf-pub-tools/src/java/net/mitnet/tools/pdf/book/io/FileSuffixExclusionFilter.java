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
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;


/**
 * File Suffix Exclusion filter.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class FileSuffixExclusionFilter implements FileFilter {
	
	private List<String> exclusionList = null;
	
	
	public FileSuffixExclusionFilter() {
		init();
	}

	@Override
	public boolean accept(File file) {
		
		boolean result = false;
		
		String fileName = file.getName();
		String fileExt = FilenameUtils.getExtension(fileName);
		
		if (!this.exclusionList.contains(fileExt)) {
			result = true;
		}
		
		return result;
	}
	
	private void init() {
		this.exclusionList = new ArrayList<String>();
		this.exclusionList.add(".svn");
		this.exclusionList.add(".DS_Store");
	}

}
