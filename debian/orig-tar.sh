#!/bin/sh

URL=http://svn.apache.org/repos/asf/labs/noggit/

SVN=/usr/bin/svn
AWK=/usr/bin/awk
TAR=/bin/tar

DEB_VERSION=$(dpkg-parsechangelog | awk '/^Version: /{print $2}')
UPSTREAM_VERSION=$(echo $DEB_VERSION | sed -rn 's/([[:digit:]\.[:digit:]]*).*/\1/p')
REVISION=$(echo $DEB_VERSION | sed -rn 's/.*~svn([[:digit:]]+).*/\1/p')

SOURCE_NAME=noggit-${UPSTREAM_VERSION}r$REVISION

TEMP_DIR=$(mktemp -d)
DEST_DIR=$TEMP_DIR/$SOURCE_NAME

$SVN export -r $REVISION $URL $DEST_DIR

DIST_TAR=$(dirname $PWD)/$SOURCE_NAME.orig.tar.gz
$TAR --remove-files -C $TEMP_DIR -czf $DIST_TAR $SOURCE_NAME

rm -r $TEMP_DIR
