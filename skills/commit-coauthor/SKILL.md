# Commit Co-Author

When making a git commit, always add a co-author line matching the project's existing agent commit convention.

## Procedure

1. Write the commit message normally.
2. Append `Co-authored-by: <self-identified-model>` as the last line of the commit message, where `<self-identified-model>` is derived from the model's own system identity (the model field or system prompt).
3. If the commit was already made without the co-author, amend it:

```bash
echo -e "<commit message>\n\nCo-authored-by: <self-identified-model>" > /tmp/commit_msg.txt
git commit --amend -F /tmp/commit_msg.txt
```

## Notes

- The model must read its own identifier from the system context (e.g., the `model` field in the workstation config or system prompt) and format it to match the project's convention.
- This convention is already established in the project's commit history.
