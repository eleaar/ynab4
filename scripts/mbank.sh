#!/usr/bin/env bash
sbt "runMain org.krz.Mbank data/mbank-in.csv" | sed '1,2d' > data/mbank-out.csv
