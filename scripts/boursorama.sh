#!/usr/bin/env bash
sbt "runMain org.krz.Boursorama data/boursorama-in.csv" | sed '1,16d' | ghead -n -4 > data/boursorama-out.csv
