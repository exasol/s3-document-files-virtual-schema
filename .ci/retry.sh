#!/bin/bash
set -euo pipefail

readonly num_retries=3
readonly retry_delay=1

NUM=$num_retries
until "$@"
do
  1>&2 echo "failure ... retrying $NUM more times"
  sleep $retry_delay
  NUM=$((NUM-1))
  if [ "$NUM" -eq 0 ]
  then
    1>&2 echo "command was not successful after $num_retries tries"
    exit 1
  fi
done
echo "success!"