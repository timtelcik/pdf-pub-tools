= PDF Publishing Tools - README =

Created: 1-Mar-2011, Tim Telcik <telcik@gmail.com>
Updated: 1-Mar-2011, Tim Telcik <telcik@gmail.com>
Updated: 20-Jul-2011, Tim Telcik <telcik@gmail.com>
Updated: 15-Feb-2012, Tim Telcik <telcik@gmail.com>
Updated: 13-Jun-2018, Tim Telcik <telcik@gmail.com>


== Overview ==

This folder contains the source code and resources for the PDF Publishing Tools project.

See the JUnit test cases and Eclipse IDE launch configs in folder "test" for examples of runtime and API usage.

This project is currently hosted by GitHub at "https://github.com/timtelcik/pdf-pub-tools/".

See related notes in the "docs" folder.


== How To Build ==

=== Checkout Source from GitHub ===

Setup local trunk folder and checkout latest source from trunk.

% cd <YOUR-PROJECT-WORK-FOLDER>
% git clone https://github.com/timtelcik/pdf-pub-tools/
% cd pdf-pub-tools


=== Build ===

1/ cd pdf-pub-tools/

2/ mvn -U clean

3/ mvn -U package

TODO: Build distribution package.

4/ cd deploy

TODO: Review distribution package.

5/ Review and use relevant runtime wrapper script.

TODO: Review distribution package.

   e.g.

   deploy/bin/run-book-builder-cli.sh
   deploy/bin/run-book-builder-gui.sh
   deploy/bin/run-pdf-book-builder-cli.sh

   deploy/bin/test-book-publisher-cli.sh, which is a wrapper for run-book-builder-cli.sh
   deploy/bin/test-pdf-book-builder-cli.sh, which is a wrapper for run-pdf-book-builder-cli.sh

   Also see "docs/usage.txt".


== Development ==

Import "pdf-pub-tools" folder into Eclipse IDE 3.6+ as a Maven project.

Eclipse will also configure the launch configs (src/*.launch) and make them available for running some of the 
test cases.


== Usage ==

See "docs/usage.txt".

