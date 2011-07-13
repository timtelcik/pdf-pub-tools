#! /bin/sh

# -v -indir "/Users/tim.telcik/spool/test/6.0ee-sp1-dev/slides/input" -outdir "/Users/tim.telcik/spool/test/6.0ee-sp1-dev/slides/output/pdf" -outbook "/Users/tim.telcik/spool/test/6.0ee-sp1-dev/slides/output/dev-training-book.pdf"

indir="/Users/tim.telcik/spool/test/6.0ee-sp1-dev/slides/input"

#outdir="/Users/tim.telcik/spool/test/6.0ee-sp1-dev/slides/output/pdf"
outdir="/Users/tim.telcik/spool/test/6.0ee-sp1-dev/slides/output/pdf2"

#outbook="/Users/tim.telcik/spool/test/6.0ee-sp1-dev/slides/output/dev-training-book.pdf"
outbook="/Users/tim.telcik/spool/test/6.0ee-sp1-dev/slides/output/dev-training-book2.pdf"

echo $0
echo "indir: $indir"
echo "outdir: $outdir"
echo "outbook: $outbook"

./bin/run-book-publisher-cli.sh $indir $outdir $outbook

