#!/bin/bash

numRetries=30
retryDelay=10

NUM=$numRetries
until "$@"
do
  1>&2 echo "failure ... retrying $NUM more times"
  sleep $retryDelay
  ((NUM--))
  if [ $NUM -eq 0 ]
  then
    1>&2 echo "command was not successful after $numRetries tries"
    exit 1
  fi
done
echo "success!"