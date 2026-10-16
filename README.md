# JarUnpacker

JarUnpacker is an IntelliJ IDEA plugin for extracting JAR files directly from the Project View.

## MVP

The first release adds an **Extract JAR** action to the context menu for a single local `.jar` file. The action extracts the archive into a sibling directory named after the original file:

```text
example.jar → example.jar-extracted/
```

If the destination already exists, its complete contents are removed before the current archive is extracted. The MVP does not change normal double-click behavior and does not automatically open or select the extracted folder.

## Later improvements

Opening or selecting the extracted folder in the Project View after a successful extraction is intentionally optional follow-up work.

## Development

The project uses Kotlin, Gradle, and the IntelliJ Platform Gradle Plugin.

```sh
./gradlew runIde       # launch a sandbox IntelliJ IDEA instance
./gradlew buildPlugin  # build the plugin distribution
./gradlew verifyPlugin # verify plugin compatibility
```
