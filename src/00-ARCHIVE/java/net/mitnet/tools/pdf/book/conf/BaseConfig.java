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

package net.mitnet.tools.pdf.book.conf;

import com.lowagie.text.Rectangle;


/**
 * Base Configuration.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class BaseConfig implements Config {
	
	private boolean verboseEnabled = false;
	private boolean debugEnabled = false;
	private String metaAuthor = null;
	private String metaTitle = null;
	private String metaSubject = null;
	private String metaKeywords = null;
	private String metaVersionId = null;
	private Rectangle pageSize = null;
	private String pageOrientation = PdfPageValues.PAGE_ORIENTATION_PORTRAIT;
	private ProgressMonitor progressMonitor = null;
	private boolean buildTocEnabled = true;
	// private TocBuilder tocBuilder = new TocBuilder();
	private int nup = CliDefaultValues.DEFAULT_NUP;
	
	
	public BaseConfig() {	
		init();
	}
	
	public void setVerboseEnabled( boolean value ) {
		this.verboseEnabled = value;
	}

	public boolean isVerboseEnabled() {
		return this.verboseEnabled;
	}
	
	public void setDebugEnabled( boolean value ) {
		this.debugEnabled = value;
	}

	public boolean isDebugEnabled() {
		return this.debugEnabled;
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
	
	public String getMetaSubject() {
		return metaSubject;
	}

	public void setMetaSubject(String metaSubject) {
		this.metaSubject = metaSubject;
	}

	public String getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}
	
	public void setMetaVersionId(String metaVersionId) {
		this.metaVersionId = metaVersionId;
	}
	
	public String getMetaVersionId() {
		return metaVersionId;
	}	
	
	public void setPageSize(Rectangle pageSize) {
		this.pageSize = pageSize;
	}
	
	public String getPageOrientation() {
		return pageOrientation;
	}

	public void setPageOrientation(String pageOrientation) {
		this.pageOrientation = pageOrientation;
	}	

	public Rectangle getPageSize() {
		return this.pageSize;
	}
	
	public float getPageHeight() {
		float height = 0f;
		if (getPageSize() != null) {
			return getPageSize().getHeight();	
		}
		return height;
	}
	
	public float getPageWidth() {
		float width = 0f;
		if (getPageSize() != null) {
			return getPageSize().getWidth();	
		}
		return width;
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
	
	/*
	public TocBuilder getTocBuilder() {
		return tocBuilder;
	}

	public void setTocBuilder(TocBuilder tocBuilder) {
		this.tocBuilder = tocBuilder;
	}
	*/
	
	public int getNup() {
		return this.nup;
	}

	public void setNup(int value) {
		this.nup = value;
	}
	
	protected void init() {
		
		this.pageSize = PdfPageValues.DEFAULT_PAGE_SIZE;
		Rectangle defPageSize = PdfPageSizeHelper.getDefaultPageSizeByLocale();
		if (defPageSize != null) {
			this.pageSize = defPageSize;			
		}
 
	}

}
