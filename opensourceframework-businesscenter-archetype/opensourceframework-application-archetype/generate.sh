#!/usr/bin/env bash
mvn install

cd ..
mvn archetype:generate -DarchetypeCatalog=local