            SARL: Symbolic Algebra and Reasoning Library
            
=============================== ABOUT =================================

SARL is a Java library for symbolic algebra reasoning.  It supports
the creation and manipulation of typed symbolic expressions,
and the ability to verify the validity of first order formulas.
It uses ("under the hood") automated theorem provers (currently
just CVC3) to assist in the verification tasks.  It also is
very effective at placing symbolic expressions in a canonical form,
in order to easily tell if two expressions are equivalent.  It
supports real (mathematical) integers and reals, as well as
Herbrand integers and reals (where the numeric operations are
treated as uninterpreted functions).  

SARL is written by Stephen Siegel, University of Delaware.
Please send comments, bug reports, feature requests, etc., to
siegel@udel.edu.
            
============================ INSTALLATION =============================

Eventually everything will be distributed in a single JAR file.
For now, you have to do a little work.
First, you need to download and build/install the dependencies:

 - Java SE 7 SDK (Java 6 MIGHT work, but I'm not sure):
   http://www.oracle.com/technetwork/java/javase/downloads/index.html
 - Apache Ant (build tool):
   http://ant.apache.org 
 - CVC3 (automated theorem prover):
   http://www.cs.nyu.edu/acsys/cvc3/download.html
 - clj-ds (Clojure's persistent data structures in pure Java):
   https://github.com/krukow/clj-ds
 - PCollections (another persistent data structure library):
   http://code.google.com/p/pcollections/
 - JUnit (automated testing framework):
   https://github.com/KentBeck/junit/downloads
 - JaCoCo (Java code coverage analysis tool):
   http://www.jacoco.org

Also, apparenty Hamcrest is now needed with JUnit 4.11 and later:
 - Hamcrest (a library of "matchers"):
   https://github.com/hamcrest/JavaHamcrest
   
See file build.properties.examples for some hints on
what to download and how to build these packages.

Next, you need to create a file named build.properties by
copying build.properties.examples and editing it to give
the paths to the dependencies on your system, as indicated
in the comments in the file.  This file goes in the main
SARL directory, i.e., the same directory as
build.properties.examples.

Now you should be able to compile & jar the code
by just typing "ant" in the main directory.  You can
also try "ant test" to run a suite of JUnit tests, and
"ant javadoc" to generate javadoc documentation in doc/javadoc.
The documentation can be read using any web browser to open
doc/javadoc/index.html.

Scripts:

The scripts (in directory "scripts") should not be of interest
to most developers.  They are used mainly on the server side
to automate the testing and coverage analysis after each
commit and to send the data to a web page.


 