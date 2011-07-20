#! /bin/sh

INDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/input"

#OUTDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/output/pdf"
OUTDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/output/pdf2"

echo $0
echo "INDIR: $INDIR"
echo "OUTDIR: $OUTDIR"
echo "TEMPLATE: $TEMPLATE"

./bin/run-book-publisher-cli.sh $INDIR $OUTDIR

