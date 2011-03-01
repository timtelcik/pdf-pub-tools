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

package net.mitnet.tools.pdf.book.ui.gui;

import javax.swing.JProgressBar;

import net.mitnet.tools.pdf.book.util.ProgressMonitor;



/**
 * Progress Bar Monitor.
 * 
 * @author Tim Telcik <telcik@gmail.com>
 */
public class ProgressBarMonitor implements ProgressMonitor {
	
	private JProgressBar progressBar = null;
	
	public ProgressBarMonitor( JProgressBar progressBar ) {
		this.progressBar = progressBar;
	}

	@Override
	public void setProgressPercentage(int value) {
		
		if (value < 100) {
			System.out.println( value + " % complete ...");
		} else if (value == 100) {
			System.out.println( value + " % complete.");
		}

		this.progressBar.setValue(value);
	}

}
