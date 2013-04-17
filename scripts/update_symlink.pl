# Name: update_symlink.pl
# Author: Stephen F. Siegel
# Written: 05-Mar-2012
# Last modified: 05-Mar-2012

# Updates symlink "latest" to point to the new revision number ONLY IF
# the new one is greater than the old.

# Takes one command line parameter: revision number

# Must be called from within "test" directory

$lockFile = "SYMLINK_UPDATE_LOCK";
$newRevision = $ARGV[0];
system("touch $lockFile") unless -e $lockFile;
open(LOCK, "+< $lockFile") || die "Could not open $lockFile";
flock(LOCK, 2) || die "Can not flock $lockFile";
$lsReturn = `ls -l latest`;
chomp($lsReturn);
($oldRevision) = ($lsReturn =~ /.*r(\d+)$/);
print "oldRevision=$oldRevision   newRevision=$newRevision\n";
if (!defined($oldRevision) || $oldRevision < $newRevision) {
  system("rm -f latest; ln -s r$newRevision latest");
}
close(LOCK) || die "Could not close $lockFile";
0;
