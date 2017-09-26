#!/usr/bin/env bash

./gradlew toolslibrary:clean

./gradlew toolslibrary:build

./gradlew bintrayUpload -PbintrayUser=jusenr -PbintrayKey=eac60de807dbad4fe6baedfb70b95fdf9ecaeb8d -PdryRun=false