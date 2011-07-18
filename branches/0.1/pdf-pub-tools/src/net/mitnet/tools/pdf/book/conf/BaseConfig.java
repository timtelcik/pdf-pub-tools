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

package net.mitnet.tools.pdf.book.conf;

import net.mitnet.tools.pdf.book.util.ProgressMonitor;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;


/**
 * Base Configuration.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class BaseConfig implements Config {
	
	// TOOD - default page size based on Locale
	public static final Rectangle DEFAULT_DOCUMENT_PAGE_SIZE = PageSize.A4;
	// public static final Rectangle DEFAULT_DOCUMENT_PAGE_SIZE = PageSize.LETTER;
	
	private boolean verbose = false;
	private boolean debug = false;
	private String metaAuthor = null;
	private String metaTitle = null;
	private Rectangle pageSize = DEFAULT_DOCUMENT_PAGE_SIZE;
	private ProgressMonitor progressMonitor = null;
	private boolean buildTocEnabled = true;
	
	
	public BaseConfig() {	
	}
	
	public void setVerbose( boolean value ) {
		this.verbose = value;
	}

	public boolean isVerbose() {
		return this.verbose;
	}
	
	public void setDebugEnabled( boolean value ) {
		this.debug = value;
	}

	public boolean isDebugEnabled() {
		return this.debug;
	}
	
	public void setMetaAuthor(String metaAuthor) {
		this.metaAuthor = metaAuthor;
	}
	
	public String getMetaAuthor() {
		return metaAuthor;
	}
	
	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}
	
	public String getMetaTitle() {
		return metaTitle;
	}
	
	public void setPageSize(Rectangle pageSize) {
		this.pageSize = pageSize;
	}

	public Rectangle getPageSize() {
		return this.pageSize;
	}
	
	public float getPageHeight() {
		return this.pageSize.getHeight();
	}
	
	public float getPageWidth() {
		return this.pageSize.getWidth();
	}
	
	public void setProgressMonitor(ProgressMonitor progressMonitor) {
		this.progressMonitor = progressMonitor;
	}
	
	public ProgressMonitor getProgressMonitor() {
		return progressMonitor;
	}

	public void setBuildTocEnabled(boolean buildTocEnabled) {
		this.buildTocEnabled = buildTocEnabled;
	}
	
	public boolean isBuildTocEnabled() {
		return buildTocEnabled;
	}
	
}
