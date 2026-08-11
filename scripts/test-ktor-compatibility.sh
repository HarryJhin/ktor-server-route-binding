#!/bin/sh

set -eu

ktor_version="${1:?Usage: $0 <ktor-version>}"
template_file=ktor.module-template.yaml
backup_dir="$(mktemp -d)"

cleanup() {
  cp "$backup_dir/$template_file" "$template_file"
  rm -rf "$backup_dir"
}

trap cleanup EXIT HUP INT TERM

cp "$template_file" "$backup_dir/$template_file"
sed -i.bak "s/^  - bom: io.ktor:ktor-bom:.*/  - bom: io.ktor:ktor-bom:$ktor_version/" "$template_file"
rm -f "$template_file.bak"

./kotlin test -m core
./kotlin test -m serialization-gson
./kotlin test -m serialization-jackson
./kotlin test -m serialization-kotlinx-json
