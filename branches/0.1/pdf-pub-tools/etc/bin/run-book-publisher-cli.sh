#! /bin/sh
############
##
## Purpose: Wrapper for CLI application.
##
## @author: Tim Telcik <telcik@gmail.com>
##
############

indir=$1
outdir=$2
outbook=$3

LIB_DIR=lib
PDF_PUB_TOOLS_LIB=$LIB_DIR/pdf-pub-tools.jar
MAIN=net/mitnet/tools/pdf/book/publisher/ui/cli/BookPublisherCLI

CLASSPATH=$PDF_PUB_TOOLS_LIB

for f in lib/*.jar
do
   CLASSPATH=$CLASSPATH:$f
done

echo "SUPPORT_LIBS: $SUPPORT_LIBS"

echo "CLASSPATH: $CLASSPATH"

# -v -indir "/Users/tim.telcik/spool/test/6.0ee-sp1-dev/slides/input" -outdir "/Users/tim.telcik/spool/test/6.0ee-sp1-dev/slides/output/pdf" -outbook "/Users/tim.telcik/spool/test/6.0ee-sp1-dev/slides/output/dev-training-book.pdf"

java -cp $CLASSPATH $MAIN -v -indir $indir -outdir $outdir -outbook $outbook

