#! /bin/sh -x
##
# Start Open Office in server (headless) mode
#
# See "http://code.google.com/p/openmeetings/wiki/OpenOfficeConverter"
# See "http://doc.nuxeo.org/5.3/books/nuxeo-book/html/admin-openoffice.html"
#
##

# Open Office 3
#OPEN_OFFICE_HOME=/Applications/OpenOffice.org.app/Contents/MacOS
#$OPEN_OFFICE_HOME/soffice.bin -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard

# Open Office 4
OPEN_OFFICE_HOME=/Applications/OpenOffice.app/Contents/program
$OPEN_OFFICE_HOME/soffice.bin -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard

