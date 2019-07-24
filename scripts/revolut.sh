#!/usr/bin/env bash

INPUT=data/revolut-in.csv
OUTPUT=data/revolut-out.csv
MAINCLASS=org.krz.Revolut

sbt "runMain $MAINCLASS $INPUT $1" | sed '1,5d' > $OUTPUT # | ghead -n -4 
if [ `wc -l < $INPUT` != `wc -l < $OUTPUT` ]; then
  echo "Some lines were dropped!"
fi
