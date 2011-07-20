#! /bin/sh
############
##
## Purpose: Setup PDF Publishing Tools Environment.
##
## @author: Tim Telcik <telcik@gmail.com>
##
############

#
# Main Environment
#

if [ -z $PDF_PUB_TOOLS_BIN_DIR ]; then
   PDF_PUB_TOOLS_BIN_DIR=$PDF_PUB_TOOLS_HOME/bin
fi
echo "PDF_PUB_TOOLS_BIN_DIR: $PDF_PUB_TOOLS_BIN_DIR"

if [ -z $PDF_PUB_TOOLS_LIB_DIR ]; then
   PDF_PUB_TOOLS_LIB_DIR=$PDF_PUB_TOOLS_HOME/lib
fi
echo "PDF_PUB_TOOLS_LIB_DIR: $PDF_PUB_TOOLS_LIB_DIR"

if [ -z $PDF_PUB_TOOLS_TEST_DIR ]; then
   PDF_PUB_TOOLS_TEST_DIR=$PDF_PUB_TOOLS_HOME/test
fi
echo "PDF_PUB_TOOLS_TEST_DIR: $PDF_PUB_TOOLS_TEST_DIR"


#
# Test Environment
#

if [ -z $PDF_PUB_TOOLS_TEST_BIN_DIR ]; then
   PDF_PUB_TOOLS_TEST_BIN_DIR=$PDF_PUB_TOOLS_TEST_DIR/bin
fi
echo "PDF_PUB_TOOLS_TEST_BIN_DIR: $PDF_PUB_TOOLS_TEST_BIN_DIR"

if [ -z $PDF_PUB_TOOLS_TEST_LIB_DIR ]; then
   PDF_PUB_TOOLS_TEST_LIB_DIR=$PDF_PUB_TOOLS_TEST_DIR/lib
fi
echo "PDF_PUB_TOOLS_TEST_LIB_DIR: $PDF_PUB_TOOLS_TEST_LIB_DIR"

