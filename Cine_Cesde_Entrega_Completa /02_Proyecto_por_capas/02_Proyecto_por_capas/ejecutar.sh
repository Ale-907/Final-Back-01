#!/usr/bin/env bash
set -eu
cd "$(dirname "$0")"
mvn compile org.codehaus.mojo:exec-maven-plugin:3.5.0:java -Dexec.mainClass=app.App
