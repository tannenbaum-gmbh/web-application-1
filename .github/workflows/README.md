# GitHub Actions Workflows

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
Installs GitHub CLI (if not present) and the GitHub Copilot CLI extension:
- Adds GitHub CLI repository
- Installs `gh` package
- Installs or upgrades `gh-copilot` extension

#### 4. Run Code Compliance Review
Executes the code-compliance custom agent:
- Changes to the `web-application/` directory
- First checks for compliance check scripts in the codecompliance repository:
  - `run-compliance-check.sh`
  - `compliance-check.sh`
- If no script found, attempts to run GitHub Copilot CLI with custom agent
- The agent/script analyzes the code against compliance rules
- Generates `compliance-findings.md` with analysis results

If neither script nor agent generates the file, a placeholder is created with:
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
- `GITHUB_TOKEN`: Automatically provided by GitHub Actions
- Used for:
  - Authenticating with GitHub CLI
  - Accessing the codecompliance repository
  - Posting PR comments

### Prerequisites

#### Repository Requirements
1. GitHub Actions must be enabled on the repository
2. The workflow file must be in `.github/workflows/` directory
3. Access to the `tannenbaum-gmbh/codecompliance` repository
   - If the codecompliance repository is **public**: Default `GITHUB_TOKEN` is sufficient
   - If the codecompliance repository is **private**: Add a repository secret named `CODECOMPLIANCE_ACCESS_TOKEN` containing a Personal Access Token (PAT) with `repo` scope

#### Code-Compliance Agent Requirements
The `tannenbaum-gmbh/codecompliance` repository should contain:
- Compliance rules and standards
- GitHub Copilot CLI custom agent configuration
- The agent should be configured to:
  - Analyze code in the target repository
  - Generate a `compliance-findings.md` file with results

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
