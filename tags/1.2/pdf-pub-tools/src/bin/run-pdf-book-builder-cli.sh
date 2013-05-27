#! /bin/sh
############
##
## Purpose: Wrapper for PDF Book Builder CLI tool.
##
## @author: Tim Telcik <telcik@gmail.com>
##
############

PRG="$0"
echo "PRG: $PRG"

PRG_DIR=`dirname "$PRG"`
echo "PRG_DIR: $PRG_DIR"

. $PRG_DIR/set-env.sh

MAIN_CLASS=net/mitnet/tools/pdf/book/pdf/builder/ui/cli/PdfBookBuilderCLI
echo "MAIN_CLASS: $MAIN_CLASS"

echo
echo "Running $MAIN_CLASS ..."
echo
#echo "$PDF_PUB_TOOLS_BIN/run-tool.sh $MAIN_CLASS $@"
#echo

$PDF_PUB_TOOLS_BIN/run-tool.sh $MAIN_CLASS $@

