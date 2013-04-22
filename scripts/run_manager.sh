#!/bin/sh

# run_manager.sh: this script does automatic build and test
# in a distributed way.  There is a manager on which the
# SVN and web servers run, and there is a remote worker that
# will do the actual testing.  This is the manager's script;
# the worker has its own script.

# This script takes one argument: the revision number to
# check out.

# The following must be defined in a file name config_manager.sh:

# PROJECT : name of project (used in web page)
# WORKER : the worker (e.g., siegel@johann.cis.udel.edu)
# WORKER_SCRIPT_DIR : where worker has his script
# WORK_DIR : project working directory on worker
# WEB_DIR : project web directory on manager
# BRANCH : name to use for the part that will be tested (e.g., trunk)
# SSH : ssh command with options
# SCP : scp command with options

# Note that the manager must have the ability to ssh to the
# worker without a password.  This needs to be set up by
# generating a private-public key pair on the manager
# and copying the public key to the worker's authorized hosts
# file.

# TODO: ideally, would like to check if the part of the
# project you are checking out has changed since last
# revision.  Need Perl.  Need to do svn info $REV,
# look for line like "Last Changed Rev: 89", then look
# up "latest" link for this script and get its revision
# number, compare the two.  If the latest is greater
# than or equal to the last changed number, nothing to
# do.

if [ $# = 0 ]; then
  echo "No revision number specified\n"
  exit 1
fi
source config_manager.sh
REV_NAME=$BRANCH-r$1
WORKING_DIR=$WORK_DIR/$REV_NAME
$SSH $WORKER "cd $WORKER_SCRIPT_DIR ; ./run_worker.sh $1"
WEB_REP=$WEB_DIR/test/$REV_NAME
rm -rf $WEB_REP
mkdir -p $WEB_REP
cd $WEB_REP
$SCP $WORKER:$WORKING_DIR/ant_out.txt .
$SCP $WORKER:$WORKING_DIR/ant_err.txt .
$SCP -r $WORKER:$WORKING_DIR/junit/reports junit
$SCP -r $WORKER:$WORKING_DIR/coverage coverage
$SCP -r $WORKER:$WORKING_DIR/doc/javadoc javadoc
INDEXHTML=index.html
cat > $INDEXHTML <<EOF
<html>
<head><title>$PROJECT $BRANCH Revision $1</title></head>
<body>
<center>
<h1>$PROJECT $BRANCH Revision $1</h1>
</center>
<h3>Test Reports and Javadocs</h3>
<ul>
<li><a href="junit/index.html">JUnit Report</a></li>
<li><a href="coverage/index.html">Coverage Report</a></li>
<li><a href="javadoc/index.html">Javadocs</a></li>
</ul>
<p></p>
<h3>Revision Information</h3>
<div style="height:120px;width:600px;overflow:scroll;
border:1px solid #666;background-color:#ffffcc;padding:8px;">
<pre>
EOF
svn info -r $1 >> $INDEXHTML
cat >> $INDEXHTML <<EOF
</pre>
</div>
<p></p>
<h3>Build output</h3>
<p>Stdout:</p>
<div style="height:120px;width:600px;overflow:scroll;
border:1px solid #666;background-color:#ffffcc;padding:8px;">
<pre>
EOF
cat ant_out.txt >> $INDEXHTML
cat >> $INDEXHTML <<EOF
</pre>
</div>
<p></p>
<p>Stderr:</p>
<div style="height:120px;width:600px;overflow:scroll;
border:1px solid #666;background-color:#ffffcc;padding:8px;">
<pre>
EOF
cat ant_err.txt >> $INDEXHTML
cat >> $INDEXHTML <<EOF
</pre>
</div>
<br>
<br>
</body>
</html>
EOF

cd $WEB_DIR/test
perl $SCRIPTS/update_symlink.pl $1
echo "Done."

ssh $WORKER "rm -rf $WORKING_DIR $WORK_DIR/svn_out-$REV_NAME.txt $WORK_DIR/svn_err-$REV_NAME.txt"
