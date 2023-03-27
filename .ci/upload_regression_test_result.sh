#!/bin/bash
set -euo pipefail

if [[ -z "${RELEASE_NAME+x}" || -z "${REGRESSION_TEST_RESULT_SUBMIT_USER+x}" || -z "${REGRESSION_TEST_RESULT_SUBMIT_PASSWORD+x}" ]] ; then
  echo "Environment variabless RELEASE_NAME, REGRESSION_TEST_RESULT_SUBMIT_USER and REGRESSION_TEST_RESULT_SUBMIT_PASSWORD must be defined"
  exit 1
fi

readonly jdbc_client_version="7.1.19"
readonly jdbc_client_url="https://x-up.s3.amazonaws.com/7.x/7.1.19/EXASOL_JDBC-$jdbc_client_version.tar.gz"
readonly jdbc_client_sha256_sum="f5bea58629a1d50c695e947792cb5e52a74667687d79c95a7593ef52035728b3"

base_dir="$( cd "$(dirname "$0")/.." >/dev/null 2>&1 ; pwd -P )"
readonly base_dir
readonly target_dir="$base_dir/target"
readonly jdbc_client_file="$target_dir/EXASOL_JDBC.tar.gz"
readonly jdbc_client_dir="$target_dir/EXASOL_JDBC-$jdbc_client_version"

mkdir -p "$target_dir"

cd "$target_dir"

if [[ ! -d "$jdbc_client_dir" ]]; then
    echo "Downloading JDBC client from $jdbc_client_url"
    curl $jdbc_client_url -o "$jdbc_client_file"
    echo "$jdbc_client_sha256_sum  $jdbc_client_file" | sha256sum --check
    echo "Unpacking $jdbc_client_file"
    tar -xvzf "$jdbc_client_file"
else
    echo "JDBC client already unpacked at $jdbc_client_dir"
fi

readonly performance_log_file="$target_dir"/performanceLog.csv
readonly performance_log_file_with_release_name="$target_dir"/performanceLogWithReleaseName.csv

cp "$target_dir"/performanceLog-*.csv "$performance_log_file"
# add release name column ($ matches end of line)
sed "s/\$/,\"$RELEASE_NAME\"/" "$performance_log_file" > "$performance_log_file_with_release_name"
"$base_dir"/.ci/retry.sh \
  "$jdbc_client_dir"/exajload \
    -c demodb.exasol.com \
    -u "$REGRESSION_TEST_RESULT_SUBMIT_USER" \
    -P "$REGRESSION_TEST_RESULT_SUBMIT_PASSWORD" \
    -sql "IMPORT INTO EXASOL_INTEGRATION_TEAM_MONITORING.REGRESSION_TEST_RESULTS FROM LOCAL CSV FILE '$performance_log_file_with_release_name' COLUMN SEPARATOR = ',' SKIP = 1;"
