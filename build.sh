#!/bin/bash

mvn clean install -Djgitver.skip=true -Pbuild-tools && \
mvn clean install $*
