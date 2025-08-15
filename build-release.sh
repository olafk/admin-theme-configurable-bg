#!/bin/bash

function revert_it_all {
    echo reverting changes
    cp ../../gradle.properties.bak ../../gradle.properties
    mv build.gradle.bak build.gradle
}

function build_it_all {
    echo Building $2
    sed -i.bak "s/release.dxp.api/$1/" build.gradle
    sed -i.bak "s/DXP/$2/" bnd.bnd
    sed -i.bak "s/liferay.workspace.product/\#liferay.workspace.product/" ../../gradle.properties
    printf "\nliferay.workspace.product=$3" >> ../../gradle.properties
    ../../gradlew clean jar
    if [ -f build/libs/*.jar ]; then
        mkdir -p dist/$2
        mv build/libs/*.jar dist/$2/
        zip -j dist/$2.zip dist/$2/*.jar
    else
        echo "**************************************"
        echo "** Build Problem? Can't find output **"
        echo "**************************************"
        sleep 3
    fi
    echo Reverting files after executing steps for $2
    revert_it_all
    sleep 2
}



build_it_all release.dxp.api DXP-2025-Q2 dxp-2024.q2.0
build_it_all release.dxp.api DXP-2025-Q1 dxp-2025.q1.0-lts
build_it_all release.portal.api CE-GA132 portal-7.4-ga132
