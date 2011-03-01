This application is a small, simple application for exporting training slides 
to PDF format automatically, so the PDFs can then be distributed to partners
running Liferay training. It uses OpenOffice.org to perform the conversion.
As a second step, the application also converts the resulting PDFs into a
two-slide-per-page book format for distribution in paper form to the
students. 

To use this, you must first start OpenOffice.org in "headless" mode. This can 
be done by navigating to the OpenOffice.org executable and issueing the 
following command: 

soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard

This will start OpenOffice.org, and it will listen on port 8100. This port is 
for now hard coded into this application, so don't try running it on another 
port--it won't work. This is my first Swing app, so it could be a long time 
before I figure out how to add a menu bar with settings and stuff. :-) 

This thing will pin your processor to the wall during the export, but 
OpenOffice.org does that while it's exporting a PDF, so I don't think it's my 
code. Of course, I could be wrong about that. 

Anyway, enjoy! We now don't have to manually generate our PDFs and store them 
in SVN anymore!! 