#! /bin/sh
############
##
## Purpose: Wrapper for PDF Book Builder CLI application.
##
## @author: Tim Telcik <telcik@gmail.com>
##
############

PRG="$0"
echo "PRG: $PRG"

PRGDIR=`dirname "$PRG"`
echo "PRGDIR: $PRGDIR"

# Only set PDF_PUB_TOOLS_HOME if not already set
if [ -z "$PDF_PUB_TOOLS_HOME" ]; then
   PDF_PUB_TOOLS_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`
fi
echo "PDF_PUB_TOOLS_HOME: $PDF_PUB_TOOLS_HOME"

PDF_PUB_TOOLS_BIN_DIR=$PDF_PUB_TOOLS_HOME/bin
echo "PDF_PUB_TOOLS_BIN_DIR: $PDF_PUB_TOOLS_BIN_DIR"

PDF_PUB_TOOLS_LIB_DIR=$PDF_PUB_TOOLS_HOME/lib
echo "PDF_PUB_TOOLS_LIB_DIR: $PDF_PUB_TOOLS_LIB_DIR"

INDIR=$1
OUTBOOK=$2
CLI_OPTS="-v"

echo "INDIR: $INDIR"
echo "OUTBOOK: $OUTBOOK"

MAIN_CLASS=net/mitnet/tools/pdf/book/pdf/builder/ui/cli/PdfBookBuilderCLI

. $PDF_PUB_TOOLS_BIN_DIR/setclasspath.sh

echo "CLASSPATH: $CLASSPATH"

java -cp $CLASSPATH $MAIN_CLASS $CLI_OPTS -indir $INDIR -outbook $OUTBOOK
#java -cp $CLASSPATH $MAIN_CLASS

