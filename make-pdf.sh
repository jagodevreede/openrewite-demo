#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
INPUT="${1:-$SCRIPT_DIR/DEMO-RUNBOOK.md}"

if [[ ! -f "$INPUT" ]]; then
    echo "ERROR: Markdown file not found: $INPUT" >&2
    exit 1
fi

echo "Generating PDF from $INPUT"
npx --yes md-to-pdf "$INPUT"

echo "PDF generated next to the Markdown source"
