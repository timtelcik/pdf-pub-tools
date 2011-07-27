#! /bin/sh
############
##
## Purpose: Wrapper for Open Office Report Builder CLI application.
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

INTEMPLATE=$1
INDATA=$2
OUTREPORT=$3
CLI_OPTS="-v"

echo "INTEMPLATE: $INTEMPLATE"
echo "INDATA: $INDATA"
echo "OUTREPORT: $OUTREPORT"
echo "CLI_OPTS: $CLI_OPTS"

MAIN_CLASS=net/mitnet/tools/pdf/book/openoffice/ui/cli/OpenOfficeReportBuilderCLI

. $PDF_PUB_TOOLS_BIN/set-classpath.sh
# . $PDF_PUB_TOOLS_BIN/set-env.sh

echo "CLASSPATH: $CLASSPATH"

java -cp $CLASSPATH $MAIN_CLASS $CLI_OPTS -intemplate $INTEMPLATE -indata $INDATA -outreport $OUTREPORT

