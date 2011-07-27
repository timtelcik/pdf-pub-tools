#! /bin/sh
#
# Start OpenOffice in server (headless) mode
#
# See Liferay Portal Admin Guide, pg.115
# See "http://code.google.com/p/openmeetings/wiki/OpenOfficeConverter"
# See "http://doc.nuxeo.org/5.3/books/nuxeo-book/html/admin-openoffice.html"
#

# /Applications/OpenOffice.org.app/Contents/MacOS/soffice.bin -headless -accept="socket,host=1.0.0.1,port=8100;urp;" -nofirststartwizard

OPEN_OFFICE_HOME=/Applications/OpenOffice.org.app/Contents/MacOS
$OPEN_OFFICE_HOME/soffice.bin -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard

