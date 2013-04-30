#! /bin/sh
############
##
## Purpose: Test wrapper for PDF Book Builder CLI tool.
##
## @author: Tim Telcik <telcik@gmail.com>
##
############

PRG="$0"
echo "PRG: $PRG"

PRG_DIR=`dirname "$PRG"`
echo "PRG_DIR: $PRG_DIR"

. $PRG_DIR/set-env.sh

#INDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/output/pdf"
INDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/output/pdf2"
echo "INDIR: $INDIR"

#OUTBOOK="$HOME/spool/test/6.0ee-sp1-dev/slides/output/dev-training-book.pdf"
OUTBOOK="$HOME/spool/test/6.0ee-sp1-dev/slides/output/dev-training-book2.pdf"
echo "OUTBOOK: $OUTBOOK"

CLI_OPTS=
#CLI_OPTS="$CLI_OPTS -d"
#CLI_OPTS="$CLI_OPTS -v"

$PDF_PUB_TOOLS_BIN/run-pdf-book-builder-cli.sh $CLI_OPTS -indir $INDIR -outbook $OUTBOOK

