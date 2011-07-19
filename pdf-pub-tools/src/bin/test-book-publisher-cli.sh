#! /bin/sh

INDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/input"

#OUTDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/output/pdf"
OUTDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/output/pdf2"

TEMPLATE=./resources/reports/templates/toc-template.odt

#OUTBOOK="$HOME/spool/test/6.0ee-sp1-dev/slides/output/dev-training-book.pdf"
OUTBOOK="$HOME/spool/test/6.0ee-sp1-dev/slides/output/dev-training-book2.pdf"

echo $0
echo "INDIR: $INDIR"
echo "OUTDIR: $OUTDIR"
echo "OUTBOOK: $OUTBOOK"
echo "TEMPLATE: $TEMPLATE"

./bin/run-book-publisher-cli.sh $INDIR $OUTDIR $OUTBOOK

