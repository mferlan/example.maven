#Convert projects to plugins (MANIFEST first approach)

Differences:
 - adds Plugin Nature
 - classic java structure <br>
 		+ src<br>
 		+ bin
 - test classes are separated into special project - fragment.
 - Manifest must be maintained manually with nice editor support
 - Declarative service XML must be maintained manually.  (eclipse 4.7 supports annotations in DS)

- target platform - pool of bundles and features. similar as maven repository.
	Defined target based on running installation of eclipse with addition of 3rd party dependencies downloaded using maven copy-dependencies goal.

- product based on equinox.application just added newly created bundles with their dependencies
 
