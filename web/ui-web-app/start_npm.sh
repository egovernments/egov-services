#!/bin/bash

CWD=${PWD}

if [ -L ${CWD}/src/development ]; then

  unlink ${CWD}/src/development
fi

if [ -d ${CWD}/src/development ]; then
  echo "${CWD}/src/development exist. This is intended for module development. Please move it to another name and try."
  exit 1
fi

ln -s ../../../npm-modules src/development

npm start
