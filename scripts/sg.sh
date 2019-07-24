#!/usr/bin/env bash

INPUT=data/sg-in.csv
OUTPUT=data/sg-out.csv
MAINCLASS=org.krz.SocieteGenerale

sbt "runMain $MAINCLASS $INPUT" | sed '1,5d' > $OUTPUT # | sed '1,12d' | ghead -n -4 
if [ `wc -l < $INPUT` != `wc -l < $OUTPUT` ]; then
  echo "Some lines were dropped!"
fi
