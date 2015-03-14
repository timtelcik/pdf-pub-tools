# Overview #

The PDF Publishing Tools require Open Office to be running in "headless" (aka. server) mode.

Default settings are to run on localhost, listening on port 8100.

## Start on Mac OS X ##

```
/Applications/OpenOffice.org.app/Contents/MacOS/soffice.bin -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard
```

## Start on Windows XP/Vista/7 ##

```
soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard
```

## Start Open Office Script ##

Sample Bourne Shell script.

```
% $PDF_PUB_TOOLS_BIN/start-open-office-headless.sh
```