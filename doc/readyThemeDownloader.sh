#!/bin/bash

# Copyright (c) 2018, tuxjsmith@gmail.com, paulb@logfarm.net
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are met:
#
# * Redistributions of source code must retain the above copyright notice, this
#   list of conditions and the following disclaimer.
# * Redistributions in binary form must reproduce the above copyright notice,
#   this list of conditions and the following disclaimer in the documentation
#   and/or other materials provided with the distribution.
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
# AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
# IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
# ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
# LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
# CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
# SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
# INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
# CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
# ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
# POSSIBILITY OF SUCH DAMAGE.

# What's this?
#
# This file compiles, builds, optimises and shrinks:
#   javaFXGkrellmThemeDownloader.jar.jar
#
# Needs to be run from a Linux command line.
#
# It requires: proguard :: http://proguard.sourceforge.net/
# Proguard will optimise and shrinks javaFXGkrellmThemeDownloader.jar
# using the configuration file: proguard/javaFXGkrellmThemeDownloader
#
# This file's permissions will need to be changed so:
# chmod 777 readyThemeDownloader.sh
#
# The output file is: javaFXGkrellmThemeDownloader.tar.gz
# To uncompress the output file:
# - Copy javaFXGkrellmThemeDownloader.tar.gz to a computer.
# - Either use an Archive Manager type application such as: file-roller
#   or the command line: tar xvfz javaFXGkrellmThemeDownloader.tar.gz
#
# All necessary library files are in place for logFarmDMS to work with:
# - Linux


PROJECT_NAME="javaFXGkrellmThemeDownloader"
PROJECT_DIR=$HOME/NetBeansProjects/$PROJECT_NAME
#ORIGINAL_LIB_DIR=$PROJECT_DIR/foo

ORIGINAL_DIST=$PROJECT_DIR/dist
NEW_LIB_DIR=$ORIGINAL_DIST/lib
DIST_RENAMED=${PROJECT_NAME}_DIST
RENAMED_DIST_DIR=$PROJECT_DIR/$DIST_RENAMED

DEFAULT_NB_README=$RENAMED_DIST_DIR/README.TXT
README=$PROJECT_DIR/README.md

ANT=$HOME/netbeans-8.2/extide/ant/bin/ant
ANT_COMMAND="$ANT -f /home/paul/NetBeansProjects/$PROJECT_NAME jfx-rebuild"

ZIP=$PROJECT_NAME.tar.gz
TAR="tar cfz $ZIP $DIST_RENAMED"

PROGUARD_DIR=$HOME/stuff/J/proguard5.3.3
PROGUARD_BIN_DIR=$PROGUARD_DIR/bin
PROGUARD=$PROGUARD_BIN_DIR/proguard.sh
PROGUARD_CONFIG="@$PROJECT_DIR/proguard/$PROJECT_NAME"

echo
echo "***********************************"
echo "*                                 *"
echo "*  DONT FORGET                    *"
echo "*                                 *"
echo "*  Change the version number in   *"
echo "*  LFDMS_Constants                *"
echo "*                                 *"
echo "***********************************"
echo

# Remove logFarmDMS_DIST.
if [ -d $RENAMED_DIST_DIR ]; then
		
    echo "Deleting $RENAMED_DIST_DIR"

		rm $RENAMED_DIST_DIR -r
fi


# Remove existing tar.gz file.
if [ -f $PROJECT_DIR/$ZIP ]; then
		
    echo "Deleting $PROJECT_DIR/$ZIP"

		rm $PROJECT_DIR/$ZIP
fi


# Build project.
$ANT_COMMAND


# Copy missing lib files.
#cp $ORIGINAL_LIB_DIR/foo-* $NEW_LIB_DIR


# Optimize the jar file.
$PROGUARD $PROGUARD_CONFIG


# Remove original jar and rename the optimized jar.
if [ -f $ORIGINAL_DIST/$PROJECT_NAME.jar ]; then
		
    echo "Deleting $ORIGINAL_DIST/$PROJECT_NAME.jar"

		rm $ORIGINAL_DIST/$PROJECT_NAME.jar
fi
mv $ORIGINAL_DIST/${PROJECT_NAME}_out.jar $ORIGINAL_DIST/$PROJECT_NAME.jar

# Rename dist dir.
mv $ORIGINAL_DIST $RENAMED_DIST_DIR


# Copy README.
if [ -f $DEFAULT_NB_README ]; then
		
    echo "Deleting $DEFAULT_NB_README"

		rm $DEFAULT_NB_README
fi
cp $README $RENAMED_DIST_DIR


# tar.gz the distribution.
cd $PROJECT_DIR
$TAR


# Remove logFarmDMS_DIST for a second time
# so GIT doesn't try to include it.
if [ -d $RENAMED_DIST_DIR ]; then
		
    echo "Deleting $RENAMED_DIST_DIR"

		rm $RENAMED_DIST_DIR -r
fi
