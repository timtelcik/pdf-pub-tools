PDF Publishing Tools - README
=============================

History
-------

* Created: 1-Mar-2011, Tim Telcik <telcik@gmail.com>
* Updated: 1-Mar-2011, Tim Telcik <telcik@gmail.com>
* Updated: 20-Jul-2011, Tim Telcik <telcik@gmail.com>
* Updated: 15-Feb-2012, Tim Telcik <telcik@gmail.com>
* Updated: 14-Mar-2015, Tim Telcik <telcik@gmail.com>

    Automatically exported from code.google.com/p/pdf-pub-tools


Overview
--------

This folder contains the source code and resources for the PDF Publishing Tools project.

See the JUnit test cases and Eclipse IDE launch configs in folder "test/src" for examples of runtime and API usage.

This project is currently hosted by Google Code at "https://code.google.com/p/pdf-pub-tools/".

See related notes in the "docs" folder.


How To Build
------------

### Checkout Source from GitHub

Setup local master folder and clone latest source from master.

eg.

    % cd <YOUR-PROJECT-WORK-FOLDER>
    % mkdir -p pdf-pub-tools/master
    % cd pdf-pub-tools/master
    % git clone https://github.com/timtelcik/pdf-pub-tools.git


### Build

1/ Navigate to source folder

eg.

    cd pdf-pub-tools/master/pdf-pub-tools

2/ Clean source folder

eg.

    ant clean

3/ Deploy resources

eg.

    ant deploy

NOTE: This will create a "deploy" folder with runtime libraries and resources, including "lib/pdf-pub-tools.jar".

4/ Navigate to deploy folder

eg.

    cd deploy

5/ Review and/or use relevant runtime wrapper scripts.

eg. 

    deploy/bin/run-book-builder-cli.sh
    deploy/bin/run-book-builder-gui.sh
    deploy/bin/run-pdf-book-builder-cli.sh

NOTE: deploy/bin/test-book-publisher-cli.sh is a wrapper for run-book-builder-cli.sh
NOTE: deploy/bin/test-pdf-book-builder-cli.sh is a wrapper for run-pdf-book-builder-cli.sh

Also see "docs/usage.txt".


Development
-----------

Import "pdf-pub-tools" folder into Eclipse IDE 3.6+ as an existing Java project.

Eclipse will also configure the launch configs (src/*.launch) and make them available for running some of the 
test cases.


Usage
-----

See "docs/usage.txt".
