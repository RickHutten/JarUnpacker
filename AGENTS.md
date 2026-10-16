# Agent Guidelines

## Repository orientation

- Inspect the repository before changing it. Start with `README.md`, contribution or development documentation, build manifests, CI configuration, and the relevant source and test directories.
- Follow the repository's established language, framework, tooling, naming, and directory conventions. Do not assume a particular stack.
- Read the nearest applicable `AGENTS.md` files. A more deeply nested file takes precedence over a parent file for its directory tree.
- Add a more specific `AGENTS.md` only when a subdirectory genuinely needs rules that do not apply to the rest of the repository.
- Do not edit an existing `AGENTS.md` unless explicitly asked to do so.

## Working principles

- Inspect existing code and configuration before changing them; follow established conventions.
- Keep changes focused, reviewable, and backed by automated tests; fix root causes rather than hiding warnings.
- Do not invent requirements or silently expand scope. Ask when an unresolved decision affects behavior, architecture, security, data, or compatibility.
- Never commit, push, or rewrite Git history. The user handles commits and pushes.
- Never store credentials, personal data, secrets, or local environment files in Git.
- Preserve existing public contracts unless a breaking change is explicitly approved.

### Scope discipline

- Implement only behavior required by the approved task and exercised by a current caller, user flow, or public boundary.
- Do not add speculative abstractions, future-facing integrations, unused APIs, or code for a later task.
- Before adding an abstraction, identify its caller or public boundary and the test that proves it. If neither exists, do not add it.
- Do not use unused-code suppressions to hide dead code. Remove dead code instead.
- Keep future behavior in the issue, story, or implementation plan until the task that consumes it exists.

## Decision-making with the user

- Ask before making an impactful decision. This includes architecture, persistence, public APIs, security, data handling, compatibility, and user-visible behavior.
- Routine implementation details may follow existing repository conventions.
- If an impactful decision becomes necessary during implementation, pause and ask for direction.

## Planning and implementation workflow

For non-trivial work:

1. Refine the request and acceptance criteria.
2. Inspect the relevant code, tests, documentation, and configuration.
3. Make a concise, testable plan and get approval for unresolved impactful decisions.
4. Implement the smallest suitable change, test it, then refactor and update lasting documentation.

Record unresolved decisions and stop for user input when they affect behavior.

## Orchestrator-first development

When the user wants an orchestration workflow, the main session is the coordinator rather than the default implementer. Keep scope, user decisions, synthesis, integration, and final verification in the main session.

- Classify work as direct, delegated, or parallel before editing.
- Delegate non-trivial implementation and review work when independent context-heavy work would materially help.
- Give each implementation agent a focused objective, acceptance criteria, relevant context, and exact verification commands.
- Use isolated workspaces for code-editing agents whenever possible. If the user explicitly allows edits in the main working tree, serialize code-editing agents so they never edit it concurrently.
- After a child reports completion, inspect the actual workspace, status, and diff. A child report is not evidence that the expected files changed in the expected location.
- Use a fresh reviewer after implementation; do not use the implementation agent as its own reviewer.
- Integrate only reviewed, in-scope changes, then run final verification from the main workspace.
- Use parallel reconnaissance only when it reduces meaningful uncertainty. Do not split a tightly coupled change into unnecessary agents.

### Delegated implementation and review

For every approved non-trivial coding task or multi-scenario test improvement:

1. Keep the main session responsible for scope, user decisions, synthesis, integration, and final verification.
2. Delegate implementation with the approved requirements, relevant files, constraints, and test commands. Require a concise, structured report.
3. Treat substantial test-only work as eligible for delegation when it adds multiple behavior cases, changes test support, or requires meaningful fixture refactoring.
4. Delegate review to a fresh agent with the requirements, changed files or diff, and available test results.
5. Require the reviewer to inspect the actual changes and run relevant checks. Its report should state the verdict, findings, missing tests, regressions, and required changes.
6. Resolve review findings in the main session and perform final verification before reporting completion.

## Code and architecture conventions

- Follow existing architecture and dependency boundaries; keep domain logic separate from delivery and infrastructure where those boundaries exist.
- Validate at system boundaries, keep delivery components thin, and place semantic or cross-record validation in the appropriate domain/application layer.
- Keep provider-specific parsing, translation, retries, and errors in the owning adapter.
- Prefer centralized, type-safe configuration and established DI, logging, persistence, migration, and error-handling conventions.
- Do not introduce a new library, framework, pattern, or abstraction when existing project facilities are sufficient.

## Testing conventions

- Follow the repository's strategy; prefer integration/component tests through stable public boundaries, with end-to-end tests for critical journeys and unit tests for complex isolated logic.
- Test observable behavior, not implementation details. Keep tests deterministic, isolated, automated, and focused on high-value cases.
- Prefer user-defined fakes, stubs, fixtures, and scenario builders over narrow mock-heavy tests.
- For behavior changes, write a failing test first when practical, implement the smallest change, run focused tests, then refactor.
- Do not add production code or tests without a current caller, public boundary, or direct connection to implemented behavior.

## Verification and completion

Before calling work complete, run the relevant tests, linters, formatters, type checks, and build/packaging checks; report their actual results. Inspect the final diff and status for unintended changes, secrets, generated files, and missing tests. Update lasting documentation and mention unresolved risks or follow-up work.

## Project-specific guidance

- This is a Kotlin IntelliJ Platform plugin targeting IDEA `2026.2.2`; use `./gradlew` and preserve plugin ID `io.github.rickhutten.jarunpacker`.
- Supported verification: `./gradlew test`, `./gradlew build`, `./gradlew verifyPluginStructure`, `./gradlew buildPlugin`, and `./gradlew verifyPlugin`. `./gradlew runIde` is manual exploratory testing only.
- Register actions through supported `plugin.xml` extension points. Use Project View and VFS APIs, keep `update()` cheap and side-effect-free, and preserve normal JAR double-click behavior.
- Do not perform archive extraction or filesystem traversal on the UI thread; refresh Project View/VFS through the appropriate platform mechanisms.
- Keep user-facing text localizable and do not add dependencies when an existing IntelliJ Platform or JDK API is sufficient.

For JAR extraction, test selection eligibility, nested files, clean replacement, traversal/absolute-entry protection, failure handling, Project View refresh, and non-UI-thread execution. Prefer deterministic platform/component tests with small JAR fixtures.

Do not change the plugin ID, vendor, target version, or distribution metadata without explicit approval.
