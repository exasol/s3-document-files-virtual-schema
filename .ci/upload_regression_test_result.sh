#!/bin/bash
set -euxo pipefail
curl https://www.exasol.com/support/secure/attachment/167973/EXASOL_JDBC-7.1.2.tar.gz -o jdbc.tar.gz
echo "3e77613fd28daab94257de9cea9fd072fe48d18aef2d5307a4d826ddd3bd49dbd780ba04d78a2513b9c181c1c3f621800f433da32fd7e3c6f7ad622385aed243  jdbc.tar.gz" | sha512sum -c
tar -xvzf jdbc.tar.gz
cp target/performanceLog-*.csv target/performanceLog.csv
# add release name column ($ matches end of line)
sed "s/\$/,\"$RELEASE_NAME\"/" target/performanceLog.csv > target/performanceLogWithReleaseName.csv
./.ci/retry.sh \
  EXASOL_JDBC-7.1.2/exajload -c demodb.exasol.com -u "$DEMODB_LOG_SUBMIT_USER" -P "$DEMODB_LOG_SUBMIT_PASS" \
    -sql "IMPORT INTO EXASOL_JABR.REGRESSION_TEST_RESULTS  FROM LOCAL CSV FILE './target/performanceLogWithReleaseName.csv' COLUMN SEPARATOR = ',' SKIP = 1;"