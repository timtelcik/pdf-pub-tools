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

. $PDF_PUB_TOOLS_HOME/bin/set-env.sh

MAIN_CLASS=net/mitnet/tools/pdf/book/pdf/builder/ui/cli/PdfBookBuilderCLI

echo
echo "Running ..."
echo
echo "$PDF_PUB_TOOLS_BIN/run-tool.sh $MAIN_CLASS $@"
echo

$PDF_PUB_TOOLS_BIN/run-tool.sh $MAIN_CLASS $@

