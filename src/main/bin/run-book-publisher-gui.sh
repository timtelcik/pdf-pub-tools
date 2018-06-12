#! /bin/sh
############
##
## Purpose: Wrapper for Book Publisher GUI application.
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

PDF_PUB_TOOLS_BIN=$PDF_PUB_TOOLS_HOME/bin
echo "PDF_PUB_TOOLS_BIN: $PDF_PUB_TOOLS_BIN"

PDF_PUB_TOOLS_LIB=$PDF_PUB_TOOLS_HOME/lib
echo "PDF_PUB_TOOLS_LIB: $PDF_PUB_TOOLS_LIB"

INDIR=$1
OUTDIR=$2
OUTBOOK=$3

echo "INDIR: $INDIR"
echo "OUTDIR: $OUTDIR"
echo "OUTBOOK: $OUTBOOK"

MAIN_CLASS=net/mitnet/tools/pdf/book/publisher/ui/gui/BookPublisherGUI

. $PDF_PUB_TOOLS_BIN/set-classpath.sh

echo "CLASSPATH: $CLASSPATH"

CLI_OPTS="-v"

#java -cp $CLASSPATH $MAIN_CLASS $CLI_OPTS -indir $INDIR -outdir $OUTDIR -outbook $OUTBOOK
java -cp $CLASSPATH $MAIN_CLASS

