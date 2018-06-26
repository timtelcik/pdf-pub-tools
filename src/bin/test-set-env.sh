#! /bin/sh
############
##
## Purpose: Test setup environment script.
##
## @author: Tim Telcik <telcik@gmail.com>
##
############

PRG="$0"
echo "PRG: $PRG"

PRG_DIR=`dirname "$PRG"`
echo "PRG_DIR: $PRG_DIR"

# Only set PDF_PUB_TOOLS_HOME if not already set
#if [ -z "$PDF_PUB_TOOLS_HOME" ]; then
#   PDF_PUB_TOOLS_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`
#fi
#echo "PDF_PUB_TOOLS_HOME: $PDF_PUB_TOOLS_HOME"

# . $PDF_PUB_TOOLS_HOME/bin/set-env.sh
. $PRG_DIR/set-env.sh

