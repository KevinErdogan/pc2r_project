AC_INIT(PR, 0.0.0.1,[bugs@bugs.org])

AM_INIT_AUTOMAKE([-Wall subdir-objects foreign])

test -z "$CXXFLAGS" && CXXFLAGS=

CXXFLAGS="-std=c++1y $CXXFLAGS"

AC_PROG_CC
AC_PROG_CXX
AC_LANG(C++)

AC_CONFIG_FILES([ Makefile src/Makefile])
				   
AC_OUTPUT