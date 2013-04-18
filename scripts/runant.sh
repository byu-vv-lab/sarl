#!/bin/sh
# runant.sh: simple shell script for automatic build/test/...
# using Ant.  Takes one command line argument: the revision
# number to check out and test.  This script is primarily
# intended to be called as a post-commit hook from a 
# version control system.

# The following must be defined in config.sh (with sample values shown):

# WORK_DIR : directory in which all work will take place.
#   Temporary directories will be created, code will be checked
#   out and compiled, executed, etc.
# WEB_DIR : directory into which output will be sent.  This is the
#   root of the web directory for the project.  New directories
#   will be created in here under test/.
# REPO : URL for repository that will be checked out (typically trunk)
# PROPS : name of the properties file that will be copied from
#   directory "properties" to the main directory before executing Ant.
# SCRIPTS : directory containing this and other scripts 

# ROOT_DIR=/home/www/data/12S
# WEB_DIR=$ROOT_DIR/www/team0
# WORK_DIR=$ROOT_DIR/work/team0
# REPO="file:///repos/team0/trunk"
# PROPS="build.properties-cisc475"
# SCRIPTS=$WORK_DIR/scripts

if [ $# = 0 ]; then
  echo "No revision number specified\n"
  exit 1
fi
source config.sh
if ! [ -d $WORK_DIR ]; then
  mkdir -p $WORK_DIR
fi
if ! [ -d $WORK_DIR ]; then
  echo "Error: could not create work directory $WORK_DIR"
  exit 1
fi
cd $WORK_DIR
rm -rf trunk-$1
echo "Checking out revision $1..."
if ! svn checkout -r $1 $REPO trunk-$1 >svn_out-$1.txt 2>svn_err-$1.txt; then
  echo "Error: svn checkout failed"
  exit 1
fi
echo "Checkout succeeded"
cd trunk-$1
if ! cp properties/$PROPS ./build.properties; then
  echo "Error: could not copy property file $PROPS"
  exit 1
fi
echo "Running ant -Drevision=$1 all ..."
ant -Drevision=$1 all > ant_out.txt 2>ant_err.txt
echo "Done."
echo "Copying files to web site..."

WEB_REP=$WEB_DIR/test/r$1
INDEXHTML=$WEB_REP/index.html
rm -rf $WEB_REP
mkdir -p $WEB_REP
cp ant_out.txt $WEB_REP
cp ant_err.txt $WEB_REP
cp -r junit/reports $WEB_REP/junit
cp -r coverage $WEB_REP/coverage
cp -r doc/javadoc $WEB_REP/javadoc
cat > $INDEXHTML <<EOF
<html>
<head><title>SARL Revision $1</title></head>
<body>
<center>
<h1>SARL Revision $1</h1>
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
echo "Done."
cd $WORK_DIR
rm -rf trunk-$1
rm -rf svn_out-$1.txt svn_err-$1.txt
cd $WEB_DIR/test
perl $SCRIPTS/update_symlink.pl $1
