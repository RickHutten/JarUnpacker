# Agent Guidelines

## Repository orientation

- Inspect the repository before changing it. Start with `README.md`, contribution or development documentation, build manifests, CI configuration, and the relevant source and test directories.
- Follow the repository's established language, framework, tooling, naming, and directory conventions. Do not assume a particular stack.
- Read the nearest applicable `AGENTS.md` files. A more deeply nested file takes precedence over a parent file for its directory tree.
- Add a more specific `AGENTS.md` only when a subdirectory genuinely needs rules that do not apply to the rest of the repository.
- Do not edit an existing `AGENTS.md` unless explicitly asked to do so.

## Working principles

- Keep changes focused on the requested task; avoid unrelated refactoring.
- Do not invent requirements. Ask the user when an unresolved decision affects scope, behavior, security, architecture, or data.
- Inspect existing code and configuration before changing them.
- Prefer small, reviewable changes backed by automated tests.
- Fix root causes rather than suppressing errors or hiding warnings.
- Never commit, push, or rewrite Git history unless explicitly asked.
- Never store credentials, tokens, personal data, secrets, or local environment files in Git.
- Preserve existing public contracts unless the requested change explicitly requires a breaking change.

### Scope discipline

- Implement only behavior required by the approved task and exercised by a current caller, user flow, or public boundary.
- Do not add speculative abstractions, future-facing integrations, unused APIs, or code for a later task.
- Before adding a class, function, module, endpoint, adapter, or abstraction, identify the current caller or public boundary it supports and the test that proves its behavior. If neither exists, do not add it.
- Do not use unused-code suppressions to hide dead code. Remove dead code instead.
- Keep future behavior in the issue, story, or implementation plan until the task that consumes it is implemented.

## Decision-making with the user

- Ask before making an impactful decision instead of choosing silently.
- Treat changes to architecture, persistence, public APIs, security, data handling, compatibility, and user-visible behavior as impactful.
- Routine implementation details may follow existing repository conventions without asking.
- If an impactful decision becomes necessary during implementation, pause and ask for direction.

## Planning and implementation workflow

For non-trivial work:

1. Discuss and refine the request and acceptance criteria.
2. Inspect the relevant code, tests, documentation, and configuration.
3. Write a concise implementation plan with concrete, testable steps when the repository has a planning convention; otherwise keep the plan in the task discussion.
4. Ask for approval before implementation when the work involves unresolved product, architecture, scope, security, or behavior decisions.
5. Implement behavior through the smallest suitable change, writing behavior-focused tests first when practical.
6. Refactor only after the required behavior is working and verified.
7. Update documentation when the change introduces a lasting user-facing, operational, or architectural decision.

Do not silently expand scope while planning or implementing. Record unresolved decisions and stop for user input when they affect behavior.

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

- Follow the repository's existing architecture and dependency boundaries.
- Keep domain or business logic separate from delivery mechanisms and infrastructure when the project uses those boundaries.
- Validate input at system boundaries and keep semantic or cross-record validation in the appropriate domain or application layer.
- Keep controllers, handlers, routes, and UI components thin when the architecture separates presentation from application logic.
- Keep external-provider parsing, translation, retries, and provider-specific behavior in the owning integration adapter.
- Prefer type-safe, centralized configuration over scattered literals or environment lookups.
- Use the repository's established dependency-injection, error-handling, logging, persistence, and migration conventions.
- Do not introduce a new library, framework, pattern, or abstraction when existing project facilities are sufficient.

## Testing conventions

- Follow the testing strategy already established by the repository; if none exists, prioritize tests through stable public boundaries.
- Emphasize integration or component tests for behavior crossing meaningful boundaries, use end-to-end tests for critical user journeys, and keep unit tests focused on complex isolated logic.
- Test observable behavior rather than implementation details so tests remain useful during refactoring.
- Keep tests deterministic, isolated, and automated. Do not rely on manual checks for completion.
- Prefer a small number of high-value tests over a large suite of narrow, mock-heavy interaction tests.
- Use small user-defined fakes, stubs, or test adapters when test doubles are needed. Follow the repository's policy on mocking frameworks.
- Reuse composable scenario builders or fixtures for stateful setup when they improve clarity, and ensure each test starts from a known state.
- For behavior changes, write the appropriate functional or integration test before implementation when practical and confirm it fails for the expected reason.

### TDD sequence

For each behavior change, when practical:

1. Write a test for behavior explicitly required by the task and exercised through a public boundary.
2. Run it and confirm it fails for the expected missing-behavior reason.
3. Implement only the code needed to make the test pass.
4. Run the focused tests green.
5. Refactor for clarity and justified reuse without adding new behavior.
6. Add focused unit tests only where the implemented logic is complex or has meaningful input permutations not covered by broader tests.

Do not add production code or tests that lack a current caller, public boundary, or direct connection to implemented behavior.

## Verification and completion

Before calling work complete:

1. Run the repository's relevant tests, linters, formatters, type checks, and build or packaging checks.
2. Report the commands run and their actual results.
3. Inspect the final diff and repository status for unintended changes, secrets, generated files, or missing tests.
4. Update relevant documentation if the change introduced a lasting decision.
5. Mention unresolved risks, limitations, or follow-up work.
