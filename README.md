# PDF Publishing Tools - README

## History

 * Created: 1-Mar-2011, Tim Telcik <telcik@gmail.com>
 * Updated: 1-Mar-2011, Tim Telcik <telcik@gmail.com>
 * Updated: 20-Jul-2011, Tim Telcik <telcik@gmail.com>
 * Updated: 15-Feb-2012, Tim Telcik <telcik@gmail.com>
 * Updated: 14-Mar-2015, Tim Telcik <telcik@gmail.com>
 * Updated: 13-Jun-2018, Tim Telcik <telcik@gmail.com>
 * Updated: 10-Sep-2018, Tim Telcik <telcik@gmail.com> 


## Overview

This folder contains the source code and resources for the PDF Publishing Tools project.

See the JUnit test cases and Eclipse IDE launch configuration in folder "src/test/resources/eclipse-ide/launch" for examples of runtime and API usage.

This project is currently hosted by GitHub at "https://github.com/timtelcik/pdf-pub-tools/".

See related notes in the "docs" folder.


## How To Build

### Install Build Tools

 * Oracle Java 1.8+ (8)
 * Gradle 4.5.1


### Checkout Source from GitHub

Setup local trunk folder and checkout latest source from trunk.

```
$ cd <YOUR-PROJECT-WORK-FOLDER>
$ git clone https://github.com/timtelcik/pdf-pub-tools/
$ cd pdf-pub-tools
```


### Install Java Dependencies

```
$ cd <YOUR-PROJECT-WORK-FOLDER>
$ cd pdf-pub-tools
$ cd lib
$ sh ./install-maven-deps.sh
```


### Build

1/ cd pdf-pub-tools/

2/ gradle clean

3/ gradle build


### Install Package

1/ cd pdf-pub-tools/build/distribution

2/ unzip pdf-pub-tools-VERSION.zip


  
### Usage

1/ Run tool wrapper script

e.g. run book builder

```
$ ./bin/run-book-builder-cli.sh
```

e.g. run book publisher

```
$ ./bin/run-book-publisher-cli.sh
```

e.g. run PDF book builder

```
$ ./bin/run-book-builder-cli.sh
```

Also see "docs/usage.txt".


## Development

### Eclipse IDE

Import "pdf-pub-tools" source code into Eclipse IDE 3.6+ as a Gradle project.

Eclipse IDE will also configure the launch configs (src/*.launch) and make them available for running some of the test cases.
