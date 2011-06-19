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
import java.util.Collections;
import java.util.List;


/**
 * File Collection.
 * 
 * A file collection retains the source parent dir/folder
 * and a list of files relative to the parent.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class FileCollection {
	
	private File parentDir = null;
	
	private List<File> fileList = Collections.emptyList();
	
	
	public FileCollection( File parentDir, List<File> fileList ) throws IllegalArgumentException {
		if (parentDir == null) {
			throw new IllegalArgumentException("Parent dir must not be null");
		}
		if (fileList == null) {
			throw new IllegalArgumentException("File list must not be null");
		}
		this.parentDir = parentDir;
		this.fileList = fileList;
	}
	
	public List<File> getFileList() {
		return fileList;
	}
	
	public int getFileListCount() {
		int count = 0;
		if (getFileList() != null) {
			count = getFileList().size();
		}
		return count;
	}
	
	public File getParentDir() {
		return parentDir;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		buffer.append(this.getClass().getName());
		buffer.append("]");
		buffer.append("(");
		buffer.append("parentDir=" + getParentDir());
		buffer.append(",fileList.size=" + getFileListCount());
		buffer.append(")");
		return buffer.toString();
	}

}
