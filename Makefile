GDIR=ggui-0.5.1

all:binaries

clean:distclean

distclean:
	cd wVisuals; make distclean
	cd wizard; make distclean
	cd ggui; make distclean
	rm -rf bin
	rm -rf *.bz2

binaries:bin/wVisuals.jar bin/wizard.jar bin/ggui.jar

bin/wVisuals.jar:
	cd wVisuals; make

bin/wizard.jar:
	cd wizard; make

bin/ggui.jar:
	cd ggui; make

src-dist:distclean
	(cd .. ; tar jcvf ${GDIR}.tar.bz2 `find ${GDIR} | grep java$$` `find ${GDIR} | grep Makefile` ${GDIR}/README ${GDIR}/AUTHORS ${GDIR}/COPYING ${GDIR}/installdata ${GDIR}/doc)
	mv ../${GDIR}.tar.bz2 .

bin-dist:binaries
	(cd .. ; tar jcvf ${GDIR}_bin.tar.bz2 `find ${GDIR} | grep jar$$` ${GDIR}/README ${GDIR}/AUTHORS ${GDIR}/COPYING ${GDIR}/doc)
	mv ../${GDIR}_bin.tar.bz2 .

full-dist:
	(cd .. ; tar jcvf ${GDIR}.tar.bz2 ${GDIR})
	mv ../${GDIR}.tar.bz2 .
