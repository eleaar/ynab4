#!/usr/bin/env bash

INPUT=data/revolut-in.csv
OUTPUT=data/revolut-out.csv
MAINCLASS=org.krz.Revolut

sbt "runMain $MAINCLASS $INPUT $1" | sed '1,12d' | ghead -n -4 > $OUTPUT
if [ `wc -l < $INPUT` != `wc -l < $OUTPUT` ]; then
  echo "Some lines were dropped!"
fi
