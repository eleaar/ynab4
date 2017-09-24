#!/usr/bin/env bash
sbt "runMain org.krz.Alior data/alior-in.csv $1" | sed '1,2d' > data/alior-out.csv
