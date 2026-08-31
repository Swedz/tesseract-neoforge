# Project Setup

Tesseract API resides in the ModMaven repository. Below is the gradle repository configuration needed.

```groovy
repositories {
	maven {
		name "Modmaven"
		url "https://modmaven.dev"
		content {
			includeGroup "net.swedz"
		}
	}
}
```

Then you can include Tesseract in your project simply by including the dependency. The below code assumes that you have
a property in `gradle.properties` named `tesseract_version` that is the version of Tesseract to depend on.

```groovy
dependencies {
	implementation "net.swedz:tesseract-api-neoforge:${tesseract_version}"
}
```

Finally, in your mod TOML file, you ought to include Tesseract as a mod dependency. This ensures that your mod loads
_after_ Tesseract, preserving proper ordering and avoiding load order issues.

This assumes that your project has the fields `mod_id` and `tesseract_version_range` configured to be substituted into
your mod TOML file. You may write the values manually if you really choose to.

```
[[dependencies.${ mod_id }]]
modId = "tesseract_api"
type = "required"
versionRange = "${tesseract_version_range}"
ordering = "AFTER"
side = "BOTH"
```