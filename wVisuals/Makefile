JARNAME=wVisuals

all:
	javac -d . *.java
	mkdir -p ../bin
	jar cf ../bin/${JARNAME}.jar com

distclean:
	rm -rf com
	rm -f ../bin/${JARNAME}.jar
