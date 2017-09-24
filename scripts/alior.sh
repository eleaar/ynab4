#!/usr/bin/env bash
sbt "runMain org.krz.Alior data/alior-in.csv $1" | sed '1,16d' | ghead -n -4 > data/alior-out.csv
