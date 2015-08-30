JARNAME=wizard

all:
	javac -d . -classpath .:../wVisuals *.java
	echo >${JARNAME}.manifest "Class-Path: wVisuals.jar"
	mkdir -p ../bin
	jar cf ../bin/${JARNAME}.jar com
	rm -f ${JARNAME}.manifest

distclean:
	rm -rf com
	rm -f ../bin/${JARNAME}.jar
