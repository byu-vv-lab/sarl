#!/bin/sh

# run_worker.sh: this script is half of the pair of scripts
# for doing automated build and test.  It is invoked by
# the manager.  It does the real work: checking out the code,
# building, testing, and generating reports.

# This script takes one argument: the revision number to check
# out.

# The following must be defined in config_worker.sh:

# WORK_DIR : project working directory on worker
# BRANCH : name to use for this part of project (e.g., trunk)
# REPO : what to check out
# PROPS : name of properties file

if [ $# = 0 ]; then
  echo "No revision number specified\n"
  exit 1
fi
source config_worker.sh
if ! [ -d $WORK_DIR ]; then
  mkdir -p $WORK_DIR
fi
if ! [ -d $WORK_DIR ]; then
  echo "Error: could not create work directory $WORK_DIR"
  exit 1
fi
cd $WORK_DIR
REV_NAME=$BRANCH-r$1
WORKING_DIR=$WORK_DIR/$REV_NAME
rm -rf $WORKING_DIR
echo "Checking out revision $1..."
if ! svn checkout -r $1 $REPO $WORKING_DIR >svn_out-$REV_NAME.txt 2>svn_err-$REV_NAME.txt; then
  echo "Error: svn checkout failed"
  exit 1
fi
echo "Checkout succeeded"
cd $WORKING_DIR
if ! cp properties/$PROPS ./build.properties; then
  echo "Error: could not copy property file $PROPS"
  exit 1
fi
echo "Running ant -Drevision=$1 all ..."
ant -Drevision=$1 all > ant_out.txt 2>ant_err.txt
echo "Done."
