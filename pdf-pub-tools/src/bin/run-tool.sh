#! /bin/sh
############
##
## Purpose: Wrapper for a GUI or CLI tool.
##
## @author: Tim Telcik <telcik@gmail.com>
##
## @see http://www.grymoire.com/Unix/Sh.html
##
############

echo
echo "This script was called with $# arguments"
echo "All arguments ..."
echo $*
echo
echo "All arguments with spaces ..."
echo $@
echo

# TODO - validate argument count; need at least 1 (MAIN_CLASS)

PRG="$0"
echo "PRG: $PRG"

PRGDIR=`dirname "$PRG"`
echo "PRGDIR: $PRGDIR"

# Only set PDF_PUB_TOOLS_HOME if not already set
if [ -z "$PDF_PUB_TOOLS_HOME" ]; then
   PDF_PUB_TOOLS_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`
fi
echo "PDF_PUB_TOOLS_HOME: $PDF_PUB_TOOLS_HOME"

PDF_PUB_TOOLS_BIN=$PDF_PUB_TOOLS_HOME/bin
echo "PDF_PUB_TOOLS_BIN: $PDF_PUB_TOOLS_BIN"

PDF_PUB_TOOLS_LIB=$PDF_PUB_TOOLS_HOME/lib
echo "PDF_PUB_TOOLS_LIB: $PDF_PUB_TOOLS_LIB"

MAIN_CLASS=$1
echo "MAIN_CLASS: $MAIN_CLASS"

shift

echo
echo "main class arguments ..."
echo "$@"
echo

if [ ! -z "$PDF_PUB_TOOLS_BIN" ]; then
   echo "Using $PDF_PUB_TOOLS_BIN/set-env.sh
   . $PDF_PUB_TOOLS_BIN/set-env.sh
elif [ ! -z "$PDF_PUB_TOOLS_HOME" ]; then
   echo "Using $PDF_PUB_TOOLS_HOME/bin/set-env.sh
   . $PDF_PUB_TOOLS_HOME/bin/set-env.sh
else
   echo "ERROR: Invalid runtime environment. Environment variables PDF_PUB_TOOLS_HOME is required."
   exit 1
fi

. $PDF_PUB_TOOLS_BIN/set-classpath.sh

# echo "CLASSPATH: $CLASSPATH"

echo
echo "Running ..."
echo "java -cp $CLASSPATH $MAIN_CLASS $@"
echo

java -cp $CLASSPATH $MAIN_CLASS $@

