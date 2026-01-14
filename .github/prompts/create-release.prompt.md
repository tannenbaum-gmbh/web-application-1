Create a new GitHub Release by using #actions_run_trigger tool to trigger the Create Release workflow. As input is needed a short description and a semver number. Always pass the version formatted as `vx.y.z` if the user input missed the format, format it. 

Here an example of the tool input:

```
{ "inputs": { "release_name": "alpha", "version_tag": "v0.0.1" }, "method": "run_workflow", "owner": "tannenbaum-gmbh", "ref": "main", "repo": "web-application-1", "workflow_id": "create-release.yml" }
```