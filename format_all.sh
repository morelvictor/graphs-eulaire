#!/bin/bash

find . -name '*.java' | while read -r FILE; do
	uncrustify -l java -c uncrustify.cfg --no-backup "$FILE"
done
