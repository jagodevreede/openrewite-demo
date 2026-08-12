#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
WRECKAGE_DIR="$SCRIPT_DIR/wreckage"
APP_DIR="$SCRIPT_DIR/survivor-app"

echo "=== Step 1: Install wreckage library ==="
cd "$WRECKAGE_DIR"
mvn clean install -q

echo ""
echo "=== Step 2: Build and verify survivor-app ==="
cd "$APP_DIR"
mvn clean package

if [[ "${1:-}" == "start" ]]; then
    echo ""
    echo "=== Starting survivor-app ==="
    mvn quarkus:dev
else
    echo ""
    echo "=== Build complete ==="
fi
