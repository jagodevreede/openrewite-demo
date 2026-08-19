#!/bin/bash

# Ensure we’re inside a Git repository
if ! git rev-parse --git-dir > /dev/null 2>&1; then
  echo "Not a git repository!"
  exit 1
fi

# Get the current commit message
current_msg=$(git log -1 --pretty=%B)

# Extract the step number using grep/sed
current_step=$(echo "$current_msg" | grep -oE 'STEP [0-9]+' | grep -oE '[0-9]+')

if [ -z "$current_step" ]; then
  echo "Could not find current STEP in commit message."
  exit 1
fi

# Calculate the next step
next_step=$((current_step + 1))

# Find the commit hash of the next step
next_commit=$(git log --all --grep="STEP $next_step\b" --pretty=format:"%H" -n 1)

if [ -z "$next_commit" ]; then
  echo "No commit found for STEP $next_step"
  exit 1
fi

# Perform a hard checkout
echo "Checking out STEP $next_step..."
git reset --hard "$next_commit"
git clean -xdf ./src
