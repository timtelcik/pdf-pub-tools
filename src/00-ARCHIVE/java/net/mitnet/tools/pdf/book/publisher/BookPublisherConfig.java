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

package net.mitnet.tools.pdf.book.publisher;

import java.io.File;


/**
 * Book Publisher Configuration.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class BookPublisherConfig extends BaseConfig {
	
	public static final String DEFAULT_TOC_TEMPLATE_PATH = CliDefaultValues.DEFAULT_TOC_TEMPLATE_PATH;
	
	public static final int DEFAULT_NUP = CliDefaultValues.DEFAULT_NUP;

	private OpenOfficeServerContext serverContext = new OpenOfficeServerContext();
	private String tocTemplatePath = null;
	private File inputDir = null;
	private File outputDir = null;
	private File outputBookFile = null;
	public BookPublisherConfig() {	
		super();
	}
	
	public void setTocTemplatePath(String tocTemplatePath) {
		this.tocTemplatePath = tocTemplatePath;
	}
	
	public String getTocTemplatePath() {
		return tocTemplatePath;
	}

	public void setServerContext(OpenOfficeServerContext serverContext) {
		this.serverContext = serverContext;
	}
	
	public OpenOfficeServerContext getServerContext() {
		return serverContext;
	}

	public File getInputDir() {
		return inputDir;
	}

	public void setInputDir(File sourceDir) {
		this.inputDir = sourceDir;
	}

	public File getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}

	public File getOutputBookFile() {
		return outputBookFile;
	}

	public void setOutputBookFile(File outputBookFile) {
		this.outputBookFile = outputBookFile;
	}

}
