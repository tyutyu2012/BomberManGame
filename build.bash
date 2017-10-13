#!/bin/bash
set -ue

mkdir -p bin
find . -name "*.java" | xargs javac -d bin

