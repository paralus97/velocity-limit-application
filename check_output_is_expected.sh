#!/bin/bash

# 1. Hardcode the file paths
FILE1="./assets/expected-output.txt"
FILE2="output.txt"

# 2. Check if both files exist before comparing
if [[ ! -f "$FILE1" ]]; then
    echo "Error: $FILE1 does not exist."
    exit 1
fi

if [[ ! -f "$FILE2" ]]; then
    echo "Error: $FILE2 does not exist."
    exit 1
fi


diff -q "$FILE1" "$FILE2" > /dev/null


if [ $? -eq 0 ]; then
    echo "Success: The files are identical."
    exit 0
else
    echo "Failure: The files are different."
    exit 1
fi