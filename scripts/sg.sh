#!/usr/bin/env bash
sbt "runMain org.krz.SocieteGenerale data/sg-in.csv" | sed '1,16d' | ghead -n -4 > data/sg-out.csv
