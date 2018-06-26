#! /bin/sh

##
#
# Install Maven dependencies.
#
# NOTE: Some (or all) of these dependencies are missing from upstream Maven repositories.
#
##

# jodconverter-2.2.2.jar
mvn install:install-file -Dfile=jodconverter-2.2.2.jar -DgroupId=com.artofsolving.jodconverter -DartifactId=jodconverter -Dversion=2.2.2 -Dpackaging=jar

# jodconverter-core-3.0-beta-4.jar
mvn install:install-file -Dfile=jodconverter-core-3.0-beta-4.jar -DgroupId=org.artofsolving.jodconverter -DartifactId=jodconverter-core -Dversion=3.0-beta-4 -Dpackaging=jar

# jodconverter-cli-2.2.2.jar
mvn install:install-file -Dfile=jodconverter-cli-2.2.2.jar -DgroupId=com.artofsolving.jodconverter -DartifactId=jodconverter-cli -Dversion=2.2.2 -Dpackaging=jar

# jodreports-2.3.0.jar
mvn install:install-file -Dfile=jodreports-2.3.0.jar -DgroupId=net.sf.jodreports -DartifactId=jodreports -Dversion=2.3.0 -Dpackaging=jar

# iText-toolbox-2.1.7.jar
# https://mvnrepository.com/artifact/com.lowagie/itext/2.1.7
mvn install:install-file -Dfile=iText-toolbox-2.1.7.jar -DgroupId=com.lowagie -DartifactId=itext-toolbox -Dversion=2.1.7 -Dpackaging=jar

