#! /bin/sh
############
##
## Purpose: Test wrapper for Book Publisher CLI tool.
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
echo "INDIR: $INDIR"

#OUTDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/output/pdf"
OUTDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/output/pdf2"
echo "OUTDIR: $OUTDIR"

#OUTBOOK="$HOME/spool/test/6.0ee-sp1-dev/slides/output/dev-training-book.pdf"
OUTBOOK="$HOME/spool/test/6.0ee-sp1-dev/slides/output/dev-training-book2.pdf"
echo "OUTBOOK: $OUTBOOK"

CLI_OPTS=
#CLI_OPTS="$CLI_OPTS -d"
#CLI_OPTS="$CLI_OPTS -v"

# $PDF_PUB_TOOLS_BIN/run-book-publisher-cli.sh -v -indir $INDIR -outdir $OUTDIR -outbook $OUTBOOK
$PDF_PUB_TOOLS_BIN/run-book-publisher-cli.sh $CLI_OPTS -indir $INDIR -outdir $OUTDIR -outbook $OUTBOOK

