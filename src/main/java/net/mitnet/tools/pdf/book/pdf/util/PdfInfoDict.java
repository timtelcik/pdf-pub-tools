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

package net.mitnet.tools.pdf.book.pdf.util;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;


/**
 * PDF Info Dict.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see PdfReader#getInfo
 */
public class PdfInfoDict {
	
	public PdfInfoDict() {
	}

	
	public void showInfoDict( File inputPdfFile ) throws IOException, DocumentException {
		
		String inputPdfFileName = inputPdfFile.getCanonicalPath();
				
		showInfoDict( inputPdfFileName );
	}	
	
	
	public void showInfoDict( String inputPdfFileName ) throws IOException, DocumentException {
		
		PdfReader pdfReader = new PdfReader( inputPdfFileName );

		Map<String,String> infoMap = pdfReader.getInfo();
		
		String mapName = "PDF " + inputPdfFileName;

		logPdfInfoDict( infoMap, mapName );
		
		pdfReader.close();
	}	

	
	public static void main(String[] args) throws IOException, DocumentException {
		
		if (args.length < 1) {
			System.err.println("usage: " + PdfInfoDict.class.getName() + " <pdf-file>");
			System.exit(1);
		}
		
		String pdfFileName = args[0];
		
		System.out.println("pdfFileName: " + pdfFileName);
		
		PdfInfoDict info = new PdfInfoDict();
		
		info.showInfoDict( pdfFileName );
		
		System.exit(0);
	}
	
	
	private void logPdfInfoDict( Map<String,String> pdfInfoDict, String name ) {
		
		if (pdfInfoDict != null) {
			if (!StringUtils.isEmpty(name)) {
				System.out.println("pdfInfoDict name : " + name);
			}
			System.out.println("pdfInfoDict size : " + pdfInfoDict.size());
			//System.out.println("infoMap : " + infoMap);
			Set<String> keySet = pdfInfoDict.keySet();
			//System.out.println("keySet : " + keySet);
			for (String key : keySet) {
				String val = pdfInfoDict.get(key);
				String line = String.format("key : %s, value : %s", key, val);
				System.out.println(line);
			}
		}
	}

}
