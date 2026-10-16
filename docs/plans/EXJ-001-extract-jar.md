# EXJ-001 implementation plan: Extract a JAR from the Project View

- **Status:** Approved
- **Story:** [EXJ-001: Extract a JAR from the Project View](../features/EXJ-001-extract-jar.md)

## Approach

Add a supported IntelliJ Platform action to the Project View popup menu. Keep the action responsible for selection validation, background-task orchestration, and Project View refresh; keep archive extraction in a small application-independent component that can be tested against temporary directories. Use the platform VFS and action APIs rather than Project View mouse handlers or internal APIs.

## Implementation steps

1. **Register the action**
   - Add an action class under `src/main/kotlin/...` and register it in `src/main/resources/META-INF/plugin.xml`.
   - Place it in the supported `ProjectViewPopupMenu` group without changing the existing double-click handling.

2. **Implement selection and presentation rules**
   - Read the current selection through the action data context.
   - Enable and show the action only for exactly one local, regular `.jar` file.
   - Run `update()` on the background thread permitted by the IntelliJ Action System because the check reads VFS state.

3. **Implement safe extraction**
   - Derive the sibling destination as `<jar filename>.jar-extracted`.
   - Clear the destination before creating the new extraction, deleting entries without following symlinks and allowing the story’s documented partial/empty destination state if extraction later fails.
   - Read entries with the JDK ZIP/JAR APIs, create directories and files as needed, and stream file contents rather than loading the archive into memory.
   - Normalize each resolved entry path and reject entries that are absolute or do not remain below the destination root, preventing traversal through `../`, equivalent normalized paths, or absolute entry names.

4. **Keep extraction off the UI thread**
   - Start extraction from `actionPerformed()` through IntelliJ’s supported background-task mechanism.
   - Refresh the destination through the asynchronous VFS refresh API after a successful extraction so the Project View sees the new directory.
   - Use the platform’s standard failure reporting for extraction errors; do not add rollback or automatic opening/selection of the destination.

5. **Add behavior-focused tests**
   - Add extractor tests under `src/test/kotlin/...` using temporary directories and JDK-created test JARs.
   - Cover nested files/directories, the exact sibling destination, stale-content removal, traversal/absolute-entry rejection, and extraction of an empty archive.
   - Add an IntelliJ platform action test for registration/presentation eligibility and the local-single-JAR boundary. Exercise the background scheduling seam with a user-defined fake rather than a mocking framework, and verify that a successful action refreshes the destination through the platform boundary where the test fixture permits it.

## Technical decisions

- Use `VirtualFile`/action-system data rather than filesystem paths to determine the selected Project View item; convert to a local `Path` only at the extraction boundary.
- Use `Task.Backgroundable` (or the equivalent supported background task API for the selected IntelliJ Platform version) for archive I/O. The extraction component itself should not depend on Swing or Project View classes.
- Treat archive entry names as untrusted input. The destination root is normalized once, and every entry is resolved and checked against that root before any write occurs.
- Keep the destination naming and replacement policy fixed to the approved story; do not add configuration, multi-selection, double-click behavior, or automatic navigation.

## Verification

Run the focused Kotlin/platform tests first, then the repository checks:

```sh
./gradlew test
./gradlew buildPlugin
./gradlew verifyPlugin
```

The implementation is ready for review only when the tests cover all ten acceptance criteria, the plugin builds, and plugin verification passes for the configured IntelliJ Platform version.
