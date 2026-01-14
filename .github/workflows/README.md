# GitHub Actions Workflows

## Create Release Workflow

### Overview
The **Create Release Workflow** (`create-release.yml`) automates the process of creating GitHub releases with AI-generated release notes. This workflow is manually triggered and creates a release branch, analyzes changes since the last release using GitHub Copilot CLI, and publishes a new release.

### Trigger Events
The workflow is triggered manually via `workflow_dispatch` with required inputs:
- `release_name` - Human-readable name for the release (e.g., "Version 1.0.0")
- `version_tag` - Semver-formatted version tag (e.g., "v1.0.0")

### Workflow Steps

#### 1. Checkout Repository
```yaml
- name: Checkout repository
  uses: actions/checkout@v4
  with:
    fetch-depth: 0
```
Checks out the repository with full history (required for comparing with previous releases).

#### 2. Validate Version Tag Format
Ensures the version tag follows semantic versioning (vX.Y.Z):
- Pattern: `^v[0-9]+\.[0-9]+\.[0-9]+$`
- Example valid tags: v1.0.0, v2.1.3, v10.20.30
- Exits with error if format is invalid

#### 3. Check if Tag Already Exists
Verifies the tag doesn't already exist in the repository to prevent duplicates.

#### 4. Create Release Branch
Creates and pushes a new branch with pattern `release/{version-tag}`:
- Example: `release/v1.0.0`
- This branch will be the target for the release

#### 5. Install GitHub Copilot CLI
Installs GitHub Copilot CLI using the official npm package:
- Uses `npm install -g @github/copilot`
- See: https://docs.github.com/en/copilot/how-tos/set-up/install-copilot-cli

#### 6. Get Last Release Tag
Identifies the previous release tag for comparison:
- Uses `git tag --sort=-version:refname` to find the latest tag
- If no previous release exists, analyzes all commits from the beginning

#### 7. Generate Changes Summary with GitHub Copilot CLI
Uses AI to analyze changes and create comprehensive release notes:
- Extracts commit log between last release and current HEAD
- Generates diff statistics
- Creates a prompt asking Copilot CLI to analyze:
  1. Brief summary of the release
  2. Key features and improvements
  3. Bug fixes
  4. Breaking changes (if any)
  5. Technical changes
- Produces markdown-formatted release notes
- Falls back to basic commit log if Copilot CLI fails

#### 8. Create Release Notes File
Ensures release notes exist with a final fallback mechanism in case previous steps failed.

#### 9. Create GitHub Release
Creates the release using GitHub CLI:
- Tag: User-specified version tag
- Title: User-specified release name
- Body: AI-generated release notes
- Target: The created release branch

#### 10. Summary
Displays workflow execution summary with release details.

### Permissions Required
```yaml
permissions:
  contents: write       # Create branches, tags, and releases
  pull-requests: read   # Read PR information (if needed)
```

### Environment Variables and Secrets
- `COPILOT_GITHUB_TOKEN`: Required secret for GitHub Copilot CLI authentication
  - Used for generating AI-powered release notes
  - Must be a Personal Access Token (PAT) with Copilot access
- `GITHUB_TOKEN`: Automatically provided by GitHub Actions
  - Used for creating the release

### Prerequisites

#### Repository Requirements
1. GitHub Actions must be enabled on the repository
2. The workflow file must be in `.github/workflows/` directory
3. **Required**: Add a repository secret named `COPILOT_GITHUB_TOKEN` containing a Personal Access Token (PAT) with Copilot access

#### Version Tag Format
Tags must follow semantic versioning:
- Format: `vMAJOR.MINOR.PATCH`
- Examples: v1.0.0, v2.3.1, v10.0.0
- Invalid: 1.0.0 (missing 'v'), v1.0 (missing patch), v1.0.0-beta (no pre-release)

### Triggering the Workflow

#### Using GitHub CLI
```bash
# Basic usage
gh workflow run create-release.yml -f release_name="Version 1.0.0" -f version_tag="v1.0.0"

# With more descriptive name
gh workflow run create-release.yml \
  -f release_name="Version 1.0.0 - Major Release" \
  -f version_tag="v1.0.0"
```

#### Using GitHub Web Interface
1. Navigate to your repository on GitHub
2. Click on the "Actions" tab
3. Select "Create Release" workflow from the left sidebar
4. Click "Run workflow" button
5. Fill in the required inputs:
   - Release name: e.g., "Version 1.0.0"
   - Version tag: e.g., "v1.0.0"
6. Click "Run workflow" to start

### Expected Output

#### Release Branch
A new branch will be created with the pattern:
```
release/v1.0.0
release/v2.1.0
```

#### Release Notes Format
AI-generated release notes will include:
```markdown
# Release Version 1.0.0

## Summary
[AI-generated summary of the release]

## Key Features and Improvements
- Feature 1
- Feature 2

## Bug Fixes
- Fix 1
- Fix 2

## Breaking Changes
- Breaking change 1 (if any)

## Technical Changes
- Technical change 1
- Technical change 2
```

#### GitHub Release
A published release with:
- Tag: v1.0.0
- Title: Version 1.0.0
- Body: AI-generated release notes
- Target: release/v1.0.0 branch

### Triggered Workflows

After creating the release, the **Release Pipeline** workflow (`release.yml`) will automatically trigger to:
- Build and test the application
- Deploy to Azure (simulated)
- Report deployment status

### Troubleshooting

#### Workflow Not Available
- Verify the workflow file exists in `.github/workflows/` directory
- Check that GitHub Actions is enabled for the repository
- Ensure you have permission to trigger workflows

#### Version Tag Validation Fails
- Ensure tag follows format: `vX.Y.Z` (e.g., v1.0.0)
- Check for typos (common: missing 'v' prefix)
- Verify using only numbers and dots

#### Tag Already Exists Error
- Check existing tags: `git tag -l`
- Choose a different version number
- Delete existing tag if needed: `git tag -d v1.0.0 && git push origin :refs/tags/v1.0.0`

#### Branch Creation Fails
- Verify repository permissions
- Check if branch already exists
- Ensure GITHUB_TOKEN has write permissions

#### Copilot CLI Installation Fails
- Check runner has internet access
- Verify npm registry is accessible
- Review installation logs for specific errors

#### Release Notes Not Generated
- Check `COPILOT_GITHUB_TOKEN` secret is configured
- Verify token has Copilot access
- Review Copilot CLI execution logs
- Workflow will use fallback (basic commit log) if Copilot fails

#### Release Creation Fails
- Verify `contents: write` permission is set
- Check `GITHUB_TOKEN` is valid
- Ensure tag doesn't already exist
- Verify release branch exists

### Customization

#### Modify Version Tag Pattern
To support different versioning schemes:
```yaml
- name: Validate version tag format
  run: |
    VERSION_TAG="${{ inputs.version_tag }}"
    # Example: Allow pre-release tags
    if [[ ! "$VERSION_TAG" =~ ^v[0-9]+\.[0-9]+\.[0-9]+(-[a-z0-9]+)?$ ]]; then
      echo "Error: Invalid version tag format"
      exit 1
    fi
```

#### Customize Release Notes Prompt
Modify the prompt in the "Generate changes summary" step:
```yaml
cat > analysis_prompt.txt <<'EOF'
Analyze the following changes and create release notes focusing on:
1. User-facing changes
2. Performance improvements
3. Security updates
Format as a bullet list suitable for non-technical users.
Commits:
EOF
```

#### Add Additional Release Assets
Extend the "Create GitHub Release" step:
```yaml
gh release create "$VERSION_TAG" \
  --title "$RELEASE_NAME" \
  --notes-file release-notes.md \
  --target "$BRANCH_NAME" \
  target/*.jar \
  docs/*.pdf
```

### Integration with CI/CD

This workflow integrates with the release pipeline:
1. **Create Release Workflow** (manual) → Creates release
2. **Release Pipeline** (automatic) → Triggered by release creation
   - Builds application
   - Runs tests
   - Deploys to Azure

### Best Practices

1. **Semantic Versioning**: Follow semver principles (MAJOR.MINOR.PATCH)
   - MAJOR: Breaking changes
   - MINOR: New features (backward compatible)
   - PATCH: Bug fixes (backward compatible)

2. **Release Naming**: Use consistent naming conventions
   - Good: "Version 1.0.0", "Release 2.1.0", "v3.0.0"
   - Avoid: "New release", "Latest", "Bob's changes"

3. **Timing**: Create releases at stable points
   - After successful testing
   - At end of sprint/milestone
   - When features are complete

4. **Review Release Notes**: Check AI-generated notes before publishing
   - Verify accuracy
   - Add important context if needed
   - Update manually in GitHub if necessary

5. **Branch Management**: Keep release branches
   - Useful for hotfixes
   - Enable patch releases
   - Maintain release history

### Support and Documentation

- **Main README**: [README.md](../../README.md)
- **GitHub Actions Docs**: https://docs.github.com/en/actions
- **Semantic Versioning**: https://semver.org/

### Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2026-01-14 | Initial workflow implementation |

---

## Compliance Review Workflow

### Overview
The **Compliance Review Workflow** (`compliance-review.yml`) automatically validates code changes against compliance standards on every pull request.

### Trigger Events
The workflow runs on:
- `pull_request:opened` - When a new PR is created
- `pull_request:synchronize` - When new commits are pushed to an existing PR
- `pull_request:reopened` - When a closed PR is reopened

Target branches: `main`, `master`

### Workflow Steps

#### 1. Checkout Current Repository
```yaml
- name: Checkout current repository
  uses: actions/checkout@v4
  with:
    path: web-application
```
Checks out the current repository with PR changes into `web-application/` directory.

#### 2. Checkout CodeCompliance Repository
```yaml
- name: Checkout codecompliance repository
  uses: actions/checkout@v4
  with:
    repository: tannenbaum-gmbh/codecompliance
    path: codecompliance
    token: ${{ secrets.CODECOMPLIANCE_ACCESS_TOKEN || secrets.GITHUB_TOKEN }}
```
Checks out the `tannenbaum-gmbh/codecompliance` repository containing compliance rules and the code-compliance custom agent. Uses `CODECOMPLIANCE_ACCESS_TOKEN` if available (for private repos), otherwise falls back to `GITHUB_TOKEN` (for public repos).

#### 3. Install GitHub Copilot CLI
Installs GitHub Copilot CLI using the official npm package:
- Uses `npm install -g @githubnext/github-copilot-cli`
- Requires `GITHUB_COPILOT_TOKEN` secret for authentication
- See: https://docs.github.com/en/copilot/how-tos/set-up/install-copilot-cli

#### 4. Run Code Compliance Review
Executes the code-compliance custom agent:
- Changes to the `web-application/` directory
- Copies codecompliance repository content to the root folder
- Invokes the custom agent with: `copilot --agent code-compliance --model claude-opus-4.5 -i "review my code and store the findings in a file called compliance-findings.md"`
- The agent analyzes the code against compliance rules
- Generates `compliance-findings.md` with analysis results

If the agent doesn't generate the file, a placeholder is created with:
- Review status
- Repository and PR information
- Basic compliance check confirmation

#### 5. Read Compliance Findings
Reads the generated `compliance-findings.md` file and stores it in a GitHub Actions output variable using heredoc for proper multiline handling.

#### 6. Post PR Comment
Uses `actions/github-script@v7` to:
- Retrieve existing PR comments
- Find any previous compliance review comment (identifies by "# Compliance Review Findings" header)
- Update existing comment if found, or create a new one
- Post the compliance findings as a markdown-formatted comment on the PR

### Permissions Required
```yaml
permissions:
  contents: read        # Read repository contents
  pull-requests: write  # Post comments on PRs
```

### Environment Variables
- `GITHUB_TOKEN`: Automatically provided by GitHub Actions - Used for posting PR comments
- `GITHUB_COPILOT_TOKEN`: Required secret for GitHub Copilot CLI authentication
- Used for:
  - Installing GitHub Copilot CLI
  - Running the code-compliance custom agent
  - Accessing the codecompliance repository (optional fallback)

### Prerequisites

#### Repository Requirements
1. GitHub Actions must be enabled on the repository
2. The workflow file must be in `.github/workflows/` directory
3. Access to the `tannenbaum-gmbh/codecompliance` repository
   - If the codecompliance repository is **public**: Default `GITHUB_TOKEN` is sufficient
   - If the codecompliance repository is **private**: Add a repository secret named `CODECOMPLIANCE_ACCESS_TOKEN` containing a Personal Access Token (PAT) with `repo` scope
4. **Required**: Add a repository secret named `GITHUB_COPILOT_TOKEN` containing a Personal Access Token (PAT) with Copilot access

#### Code-Compliance Agent Requirements
The `tannenbaum-gmbh/codecompliance` repository should contain:
- Compliance rules and standards
- GitHub Copilot CLI custom agent configuration (`.github/copilot/agents/code-compliance/` directory)
- The agent configuration should specify:
  - How to analyze code in the target repository
  - Instructions to generate a `compliance-findings.md` file with results
- When invoked with `copilot --agent code-compliance --model claude-opus-4.5`, the agent should review code and create findings file

### Expected Output

#### compliance-findings.md Format
The generated file should follow this structure:
```markdown
# Compliance Review Findings

**Status**: [Pass/Fail/Warning]

## Summary
[High-level overview of the compliance review]

## Review Details
- Repository: [repo name]
- PR Number: [PR number]
- Branch: [branch name]
- Commit: [commit SHA]

## Findings
[Detailed list of compliance issues, violations, or confirmations]

### Critical Issues
[List of critical compliance violations]

### Warnings
[List of compliance warnings]

### Recommendations
[Suggested improvements]

## Conclusion
[Final assessment and next steps]
```

#### PR Comment Example
The workflow posts findings as a comment on the PR. The comment will include:
- Compliance review status
- Summary of findings
- Detailed analysis results
- Recommendations for improvements

### Troubleshooting

#### Workflow Not Triggering
- Verify the workflow file is in `.github/workflows/` directory
- Check that GitHub Actions is enabled for the repository
- Ensure PR targets `main` or `master` branch

#### CodeCompliance Repository Access Denied
- Verify the repository exists: `tannenbaum-gmbh/codecompliance`
- Check that `GITHUB_TOKEN` has access to the repository
- Ensure the repository is public or the token has appropriate permissions

#### GitHub Copilot CLI Installation Fails
- Check runner has internet access
- Verify GitHub CLI repository is accessible
- Review installation logs for specific errors

#### compliance-findings.md Not Generated
- Verify the code-compliance agent is properly configured
- Check if the agent has necessary permissions
- Review agent execution logs
- The workflow creates a placeholder if file is missing

#### PR Comment Not Posted
- Verify `pull-requests: write` permission is set
- Check `GITHUB_TOKEN` is valid
- Ensure the PR exists and is not closed
- Review GitHub Script action logs

### Customization

#### Modify Trigger Events
To run on different PR events:
```yaml
on:
  pull_request:
    types: [opened, synchronize, reopened, ready_for_review]
```

#### Change Target Branches
To target different branches:
```yaml
on:
  pull_request:
    branches:
      - main
      - develop
      - release/*
```

#### Customize Agent Execution
Modify the compliance check step to use a specific script or command:
```yaml
- name: Run Code Compliance Review
  run: |
    cd web-application
    ../codecompliance/scripts/run-compliance-check.sh
```

#### Customize Comment Format
Modify the GitHub Script step to format the comment differently:
```yaml
script: |
  const findings = `${{ steps.compliance.outputs.findings }}`;
  const commentBody = `
  ## 🔍 Automated Compliance Review
  
  ${findings}
  
  ---
  *Generated by Compliance Review Workflow*
  `;
```

### Integration with CI/CD

This workflow can be combined with other CI/CD workflows:
- Run after successful build and test
- Block merge if critical violations found
- Integrate with status checks

Example branch protection rule:
- Require "compliance-check" job to pass before merge
- Require at least one approval after compliance review

### Maintenance

#### Updating the Workflow
1. Edit `.github/workflows/compliance-review.yml`
2. Test changes on a feature branch
3. Create PR with workflow changes
4. Verify workflow runs correctly on the PR
5. Merge after validation

#### Updating Compliance Rules
1. Update rules in `tannenbaum-gmbh/codecompliance` repository
2. Rules take effect immediately on next workflow run
3. No changes needed in this repository

### Best Practices

1. **Review Compliance Findings**: Always review compliance feedback before merging
2. **Address Critical Issues**: Fix critical violations before merge
3. **Keep Rules Updated**: Regularly update compliance rules to reflect best practices
4. **Monitor Workflow**: Check workflow runs for failures or issues
5. **Document Exceptions**: If legitimate exceptions exist, document in PR description

### Support and Documentation

- **Main README**: [README.md](../../README.md)
- **Compliance Guide**: [COMPLIANCE_CHECKING_GUIDE.md](../../COMPLIANCE_CHECKING_GUIDE.md)
- **Compliance Rules**: [COMPLIANCE_RULES.md](../../COMPLIANCE_RULES.md)
- **GitHub Actions Docs**: https://docs.github.com/en/actions

### Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2026-01-14 | Initial workflow implementation |

---

**Questions or Issues?**
- Review workflow logs in the Actions tab
- Check compliance documentation
- Contact repository maintainers
