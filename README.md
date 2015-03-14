PDF Publishing Tools - README
=============================

History
-------

Created: 1-Mar-2011, Tim Telcik <telcik@gmail.com>
Updated: 1-Mar-2011, Tim Telcik <telcik@gmail.com>
Updated: 20-Jul-2011, Tim Telcik <telcik@gmail.com>
Updated: 15-Feb-2012, Tim Telcik <telcik@gmail.com>
Updated: 14-Mar-2015, Tim Telcik <telcik@gmail.com>
+ Automatically exported from code.google.com/p/pdf-pub-tools


Overview
--------

This folder contains the source code and resources for the PDF Publishing Tools project.

See the JUnit test cases and Eclipse IDE launch configs in folder "test/src" for examples of runtime and API usage.

This project is currently hosted by Google Code at "https://code.google.com/p/pdf-pub-tools/".

See related notes in the "docs" folder.


How To Build
------------

### Checkout Source from Trunk

Setup local trunk folder and checkout latest source from trunk.

% cd <YOUR-PROJECT-WORK-FOLDER>
% mkdir -p pdf-pub-tools/master
% cd pdf-pub-tools/master
% git clone http://github.com/timtelcik/pdf-pub-tools.git


### Build

1/ cd pdf-pub-tools/trunk/pdf-pub-tools

2/ ant clean

3/ ant deploy

   This will create a "deploy" folder with runtime libraries and resources, including "lib/pdf-pub-tools.jar".

4/ cd deploy

5/ Review and use relevant runtime wrapper script.

   eg. 

   deploy/bin/run-book-builder-cli.sh
   deploy/bin/run-book-builder-gui.sh
   deploy/bin/run-pdf-book-builder-cli.sh

   deploy/bin/test-book-publisher-cli.sh, which is a wrapper for run-book-builder-cli.sh
   deploy/bin/test-pdf-book-builder-cli.sh, which is a wrapper for run-pdf-book-builder-cli.sh

   Also see "docs/usage.txt".


Development
-----------

Import "pdf-pub-tools" folder into Eclipse IDE 3.6+ as an existing Java project.

Eclipse will also configure the launch configs (src/*.launch) and make them available for running some of the 
test cases.


Usage
-----

See "docs/usage.txt".
