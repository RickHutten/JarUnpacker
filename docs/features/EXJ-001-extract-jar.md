# EXJ-001: Extract a JAR from the Project View

- **Status:** Approved
- **Priority:** High

## User story

As an IntelliJ IDEA user, I want to right-click a local JAR file in the Project View and choose **Extract JAR**, so that I can unpack its contents next to the archive without leaving the IDE.

## Scope

This story covers extracting one local `.jar` file selected in IntelliJ's Project View. The extracted directory is a clean replacement of the destination for every invocation.

## Acceptance criteria

1. The **Extract JAR** action is available when exactly one local regular `.jar` file is selected in the Project View.
2. Invoking the action extracts the archive into a sibling directory named `<filename>.jar-extracted`.
3. For `example.jar`, the destination is `example.jar-extracted/` in the same parent directory.
4. If the destination already exists, all of its previous contents are removed before the current archive is extracted.
5. The extracted directory contains the files and nested directories represented by the current JAR, with no stale files from an earlier extraction after a successful run.
6. Archive entries cannot write outside the destination directory through path traversal or equivalent entry names.
7. Extraction does not block IntelliJ's user interface while archive contents are being read or written.
8. IntelliJ's Project View is refreshed after the extraction so the destination becomes visible without restarting the IDE.
9. The plugin does not change the normal double-click behavior for JAR files.
10. Opening or selecting the extracted directory automatically is not part of this story and remains optional follow-up work.

## Out of scope

- Automatic extraction on double-click.
- Opening or selecting the extracted directory after extraction.
- Extracting multiple selected JAR files in one action.
- Extracting library, remote, or non-local virtual JARs.
- Configurable destination names or overwrite policies.

## Notes

- The destination is cleared before extraction. If extraction fails, the destination may be empty or partially populated; transactional rollback is not required for this story.
- The implementation must use supported IntelliJ Platform APIs for action registration and Project View integration rather than internal Project View mouse handlers.
