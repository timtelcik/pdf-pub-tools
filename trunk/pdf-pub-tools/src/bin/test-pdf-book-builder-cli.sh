#! /bin/sh

#INDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/output/pdf"
INDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/output/pdf2"

#OUTBOOK="$HOME/spool/test/6.0ee-sp1-dev/slides/output/dev-training-book.pdf"
OUTBOOK="$HOME/spool/test/6.0ee-sp1-dev/slides/output/dev-training-book2.pdf"

echo "INDIR: $INDIR"
echo "OUTBOOK: $OUTBOOK"

./bin/run-pdf-book-builder-cli.sh -v -indir $INDIR -outbook $OUTBOOK

