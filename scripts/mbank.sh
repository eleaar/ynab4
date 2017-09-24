#!/usr/bin/env bash

INPUT=data/mbank-in.csv
OUTPUT=data/mbank-out.csv
MAINCLASS=org.krz.Mbank

sbt "runMain $MAINCLASS $INPUT" | sed '1,12d' | ghead -n -4 > $OUTPUT
if [ `wc -l < $INPUT` != `wc -l < $OUTPUT` ]; then
  echo "Some lines were dropped!"
fi
