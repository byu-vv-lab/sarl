
# TODO: scp this stuff

# this runs on server vsl.
# it executes the checkout and ant on test machine 
#   using ssh and waits for it to complete
# then this scp's the file over to web
# then this updates symlink

# PROJECT : name of project
# WORKER : hostname of worker
# WORK_DIR : project working directory on worker
# WEB_DIR : project web directory on manager
# BRANCH : name of branch (e.g., trunk)
# SSH : ssh command with all options
# SCP : scp command with all options

# /usr/local/openssh-5.3p1/bin/ssh -i /usa/siegel/.ssh/id_dsa_test siegel@johann.cis.udel.edu hostname >>/usa/siegel/HOOK
if [ $# = 0 ]; then
  echo "No revision number specified\n"
  exit 1
fi
source config_manager.sh
REV_NAME=$BRANCH-r#1
WORKING_DIR=$WORK_DIR/$REV_NAME
$SSH $WORKER $WORKER_SCRIPT_DIR/run_worker.sh $1
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

ssh $WORKER rm -rf $WORKING_DIR
ssh $WORKER rm -f $WORK_DIR/svn_out-$REV_NAME.txt
ssh $WORKER rm -f $WORK_DIR/svn_err-$REV_NAME.txt
