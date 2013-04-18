#!/bin/sh

# PROJECT : name of project
# WORKER : hostname of worker
# WORK_DIR : project working directory on worker
# BRANCH : name of branch (e.g., trunk)
# REPO
# PROPS
 

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
REV_NAME=$BRANCH-r#1
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
