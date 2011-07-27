#! /bin/sh

INDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/input"

#OUTDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/output/pdf"
OUTDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/output/pdf2"

#OUTBOOK="$HOME/spool/test/6.0ee-sp1-dev/slides/output/dev-training-book.pdf"
OUTBOOK="$HOME/spool/test/6.0ee-sp1-dev/slides/output/dev-training-book2.pdf"

echo $0
echo "INDIR: $INDIR"
echo "OUTDIR: $OUTDIR"
echo "OUTBOOK: $OUTBOOK"

./bin/run-book-publisher-cli.sh -v -indir $INDIR -outdir $OUTDIR -outbook $OUTBOOK

