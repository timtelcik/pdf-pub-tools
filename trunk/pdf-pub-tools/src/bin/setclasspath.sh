#! /bin/sh
############
##
## Purpose: Setup classpath.
##
## @author: Tim Telcik <telcik@gmail.com>
##
############

PRG="$0"
echo "PRG: $PRG"

PRGDIR=`dirname "$PRG"`
echo "PRGDIR: $PRGDIR"

# Only set PDF_PUB_TOOLS_HOME if not already set
if [ -z "$PDF_PUB_TOOLS_HOME" ]; then
   PDF_PUB_TOOLS_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`
fi
echo "PDF_PUB_TOOLS_HOME: $PDF_PUB_TOOLS_HOME"

if [ -z $PDF_PUB_TOOLS_BIN_DIR ]; then
   PDF_PUB_TOOLS_BIN_DIR=$PDF_PUB_TOOLS_HOME/bin
fi
echo "PDF_PUB_TOOLS_BIN_DIR: $PDF_PUB_TOOLS_BIN_DIR"

if [ -z $PDF_PUB_TOOLS_LIB_DIR ]; then
   PDF_PUB_TOOLS_LIB_DIR=$PDF_PUB_TOOLS_HOME/lib
fi
echo "PDF_PUB_TOOLS_LIB_DIR: $PDF_PUB_TOOLS_LIB_DIR"

PPT_CP=
echo "PPT_CP: $PPT_CP"

for f in $PDF_PUB_TOOLS_LIB_DIR/*.jar
do
   PPT_CP=$PPT_CP:$f
done

echo "PPT_CP: $PPT_CP"

CLASSPATH=$CLASSPATH:$PPT_CP
echo "CLASSPATH: $CLASSPATH"

