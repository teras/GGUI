JARNAME=ggui
DEPREC=-deprecation

all:
	javac -d . -classpath .:../wVisuals:../wizard ${DEPREC} *.java
	mkdir -p ../bin
	echo >${JARNAME}.manifest "Main-Class: com.panayotis.ggui.main"
	echo >>${JARNAME}.manifest "Class-Path: wVisuals.jar wizard.jar"
	jar cmf ${JARNAME}.manifest ../bin/${JARNAME}.jar com -C ../ installdata
	rm -f ${JARNAME}.manifest

distclean:
	rm -rf com
	rm -f ../bin/ggui.jar

run:
	cd ../bin ; java -jar ggui.jar unnamed

empty:
	cd ../bin ; java -jar ggui.jar

help:
	cd ../bin ; java -jar ggui.jar --help

edit:
	cd ../bin ; java -jar ggui.jar -edit
