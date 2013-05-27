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

package net.mitnet.tools.pdf.book.openoffice.net;


/**
 * Open Office Server Context.
 * 
 * Contains details for the Open Office service.
 *
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see OpenOfficeConnection
 * @see OpenOfficeConnectionFactory
 */
public class OpenOfficeServerContext {
	
	public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 8100;
	
	private String host = DEFAULT_HOST;
	private int port = DEFAULT_PORT;
	
	
	public OpenOfficeServerContext() {
		this.host = DEFAULT_HOST;
		this.port = DEFAULT_PORT;
	}

	public OpenOfficeServerContext( int port ) {
		this.host = DEFAULT_HOST;
		this.port = port;
	}

	public OpenOfficeServerContext( String host, int port ) {
		this.host = host;
		this.port = port;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "OpenOfficeServerContext [host=" + host + ", port=" + port + "]";
	}

}
