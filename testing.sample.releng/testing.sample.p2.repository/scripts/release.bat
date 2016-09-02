export VERSION=<version>
export NEWVERSION=<version+1>

# Change the version number to <version>
mvn -Dtycho.mode=maven org.eclipse.tycho:tycho-versions-plugin:0.25.0:set-version -DnewVersion=$VERSION

# make sure everything compiles
mvn clean install

# Commit and push the changes:
git commit -a -m "Changed version number for $VERSION release ($VERSION)"
git tag V_$VERSION
git push origin
git push origin V_$VERSION

# Change the version number to <version+1>-SNAPSHOT and commit:
mvn -Dtycho.mode=maven org.eclipse.tycho:tycho-versions-plugin:0.25.0:set-version -DnewVersion=$NEWVERSION-SNAPSHOT
git commit -a -m "Changed version to $NEWVERSION-SNAPSHOT"
git push