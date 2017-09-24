#!/usr/bin/env bash

INPUT=data/alior-in.csv
OUTPUT=data/alior-out.csv
MAINCLASS=org.krz.Alior

sbt "runMain $MAINCLASS $INPUT $1" | sed '1,12d' | ghead -n -4 > $OUTPUT
if [ `wc -l < $INPUT` != `wc -l < $OUTPUT` ]; then
  echo "Some lines were dropped!"
fi
