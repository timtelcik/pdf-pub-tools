#! /bin/sh
############
##
## Purpose: Test wrapper for Open Ofice Doc Converter CLI tool.
##
## @author: Tim Telcik <telcik@gmail.com>
##
############

PRG="$0"
echo "PRG: $PRG"

PRG_DIR=`dirname "$PRG"`
echo "PRG_DIR: $PRG_DIR"

. $PRG_DIR/set-env.sh

INDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/input"

#OUTDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/output/pdf"
OUTDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/output/pdf2"

echo "INDIR: $INDIR"
echo "OUTDIR: $OUTDIR"

$PDF_PUB_TOOLS_BIN/run-oo-doc-converter-cli.sh -v -indir $INDIR -outdir $OUTDIR

