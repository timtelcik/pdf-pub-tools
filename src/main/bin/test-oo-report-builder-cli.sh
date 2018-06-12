#! /bin/sh
############
##
## Purpose: Test wrapper for Open Ofice Report Builder CLI tool.
##
## @author: Tim Telcik <telcik@gmail.com>
##
############

PRG="$0"
echo "PRG: $PRG"

PRG_DIR=`dirname "$PRG"`
echo "PRG_DIR: $PRG_DIR"

. $PRG_DIR/set-env.sh

INTEMPLATE=TODO
echo "INTEMPLATE: $INTEMPLATE"

INDATA=TODO
echo "INDATA: $INDATA"

OUTREPORT=TODO
echo "OUTREPORT: $OUTREPORT"

$PDF_PUB_TOOLS_BIN/run-oo-doc-converter-cli.sh -v -indir $INDIR -outdir $OUTDIR

