#!/usr/bin/env bash

INPUT=data/boursorama-in.csv
OUTPUT=data/boursorama-out.csv
MAINCLASS=org.krz.Boursorama

sbt "runMain $MAINCLASS $INPUT" | sed '1,12d' | ghead -n -4 > $OUTPUT
if [ `wc -l < $INPUT` != `wc -l < $OUTPUT` ]; then
  echo "Some lines were dropped!"
fi