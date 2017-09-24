#!/usr/bin/env bash
sbt "runMain org.krz.Boursorama data/boursorama-in.csv" > data/boursorama-out.csv
