#!/usr/bin/env bash
sbt "runMain org.krz.Mbank data/mbank-in.csv" | sed '1,16d' | ghead -n -4 > data/mbank-out.csv
