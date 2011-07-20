#! /bin/sh
############
##
## Purpose: Setup shell environment.
##
## @author: Tim Telcik <telcik@gmail.com>
##
############

#
# Main environment
#

if [ -z $PDF_PUB_TOOLS_HOME ]; then
   echo "PDF_PUB_TOOLS_HOME is undefined"
   exit 1
fi

if [ -z $PDF_PUB_TOOLS_BIN ]; then
   PDF_PUB_TOOLS_BIN=$PDF_PUB_TOOLS_HOME/bin
fi
echo "PDF_PUB_TOOLS_BIN: $PDF_PUB_TOOLS_BIN"

if [ -z $PDF_PUB_TOOLS_LIB ]; then
   PDF_PUB_TOOLS_LIB=$PDF_PUB_TOOLS_HOME/lib
fi
echo "PDF_PUB_TOOLS_LIB: $PDF_PUB_TOOLS_LIB"

if [ -z $PDF_PUB_TOOLS_TEST_HOME ]; then
   PDF_PUB_TOOLS_TEST_HOME=$PDF_PUB_TOOLS_HOME/test
fi
echo "PDF_PUB_TOOLS_TEST_HOME: $PDF_PUB_TOOLS_TEST_HOME"


#
# Test environment
#

if [ -z $PDF_PUB_TOOLS_TEST_BIN ]; then
   PDF_PUB_TOOLS_TEST_BIN=$PDF_PUB_TOOLS_TEST_HOME/bin
fi
echo "PDF_PUB_TOOLS_TEST_BIN: $PDF_PUB_TOOLS_TEST_BIN"

if [ -z $PDF_PUB_TOOLS_TEST_LIB ]; then
   PDF_PUB_TOOLS_TEST_LIB=$PDF_PUB_TOOLS_TEST_HOME/lib
fi
echo "PDF_PUB_TOOLS_TEST_LIB: $PDF_PUB_TOOLS_TEST_LIB"

