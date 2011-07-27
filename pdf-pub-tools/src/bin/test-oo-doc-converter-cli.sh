#! /bin/sh

INDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/input"

#OUTDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/output/pdf"
OUTDIR="$HOME/spool/test/6.0ee-sp1-dev/slides/output/pdf2"

echo "INDIR: $INDIR"
echo "OUTDIR: $OUTDIR"

./bin/run-oo-doc-converter-cli2.sh -v -indir $INDIR -outdir $OUTDIR

