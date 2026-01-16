# Release v0.0.3 (Alpha 3) - Instructions

## Overview
This document provides instructions for creating the v0.0.3 release (alpha 3) for the web-application-1 repository.

## Current State
- Latest release tag: `v0.0.2`
- Target release: `v0.0.3` (alpha 3)
- Workflow: `.github/workflows/create-release.yml`

## Workflow Trigger Method

According to `.github/prompts/create-release.prompt.md`, the release should be created by triggering the GitHub Actions workflow using the MCP tool.

### Required Workflow Inputs
```json
{
  "inputs": {
    "release_name": "alpha 3",
    "version_tag": "v0.0.3"
  },
  "method": "run_workflow",
  "owner": "tannenbaum-gmbh",
  "ref": "main",
  "repo": "web-application-1",
  "workflow_id": "create-release.yml"
}
```

### Alternative: GitHub CLI
If the MCP tool is not available, the workflow can be triggered using the GitHub CLI:

```bash
gh workflow run create-release.yml \
  --ref main \
  -f release_name="alpha 3" \
  -f version_tag="v0.0.3"
```

### Alternative: GitHub UI
1. Navigate to: https://github.com/tannenbaum-gmbh/web-application-1/actions/workflows/create-release.yml
2. Click "Run workflow"
3. Fill in the inputs:
   - `release_name`: alpha 3
   - `version_tag`: v0.0.3
4. Click "Run workflow"

## What the Workflow Will Do

The `create-release.yml` workflow will:
1. Validate the version tag format (v0.0.3)
2. Check if the tag already exists
3. Create a release branch: `release/v0.0.3`
4. Get the last release tag (v0.0.2)
5. Generate a changes summary from v0.0.2 to HEAD
6. Create a GitHub Release with:
   - Tag: v0.0.3
   - Title: alpha 3
   - Generated release notes
   - Target branch: release/v0.0.3

## Verification
After triggering the workflow, verify:
1. Workflow completes successfully in GitHub Actions
2. Tag `v0.0.3` is created
3. Release branch `release/v0.0.3` is created
4. GitHub Release is published with proper release notes

## Notes
- The sandboxed development environment does not have the necessary authentication to trigger GitHub workflows directly
- The workflow must be triggered by a user with appropriate permissions or through an authenticated MCP tool
