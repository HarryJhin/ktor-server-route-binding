#!/bin/sh

set -eu

snapshot_version="${1:-0.0.2-SNAPSHOT}"
case "$snapshot_version" in
  *-SNAPSHOT) ;;
  *)
    echo "Snapshot version must end with -SNAPSHOT: $snapshot_version" >&2
    exit 1
    ;;
esac

gradle_properties_file="${GRADLE_USER_HOME:-$HOME/.gradle}/gradle.properties"

gradle_property() {
  property_name="$1"
  [ -r "$gradle_properties_file" ] || return 0
  awk -F= -v property_name="$property_name" '
    $1 == property_name {
      sub(/^[^=]*=/, "")
      print
      exit
    }
  ' "$gradle_properties_file"
}

if [ -z "${KOTLIN_TOOLCHAIN_MAVEN_CENTRAL_USERNAME:-}" ]; then
  KOTLIN_TOOLCHAIN_MAVEN_CENTRAL_USERNAME="$(gradle_property mavenCentralUsername)"
fi
if [ -z "${KOTLIN_TOOLCHAIN_MAVEN_CENTRAL_PASSWORD:-}" ]; then
  KOTLIN_TOOLCHAIN_MAVEN_CENTRAL_PASSWORD="$(gradle_property mavenCentralPassword)"
fi
if [ -z "${KOTLIN_TOOLCHAIN_SIGNING_KEY:-}" ]; then
  KOTLIN_TOOLCHAIN_SIGNING_KEY="$(printf '%b' "$(gradle_property signingInMemoryKey)")"
fi
if [ -z "${KOTLIN_TOOLCHAIN_SIGNING_KEY_PASSPHRASE:-}" ]; then
  KOTLIN_TOOLCHAIN_SIGNING_KEY_PASSPHRASE="$(gradle_property signingInMemoryKeyPassword)"
fi

export KOTLIN_TOOLCHAIN_MAVEN_CENTRAL_USERNAME
export KOTLIN_TOOLCHAIN_MAVEN_CENTRAL_PASSWORD
export KOTLIN_TOOLCHAIN_SIGNING_KEY
export KOTLIN_TOOLCHAIN_SIGNING_KEY_PASSPHRASE

: "${KOTLIN_TOOLCHAIN_MAVEN_CENTRAL_USERNAME:?Set the Central Portal user-token username}"
: "${KOTLIN_TOOLCHAIN_MAVEN_CENTRAL_PASSWORD:?Set the Central Portal user-token password}"
: "${KOTLIN_TOOLCHAIN_SIGNING_KEY:?Set the ASCII-armored PGP private key}"

snapshot_credentials_file=.central-snapshots.properties
module_files='library.module-template.yaml'
backup_dir="$(mktemp -d)"

cleanup() {
  for module_file in $module_files; do
    cp "$backup_dir/$module_file" "$module_file"
  done
  cp "$backup_dir/$(basename "$snapshot_credentials_file")" "$snapshot_credentials_file"
  rm -rf "$backup_dir"
}

trap cleanup EXIT HUP INT TERM

umask 077
cp "$snapshot_credentials_file" "$backup_dir/$(basename "$snapshot_credentials_file")"
printf 'mavenCentralUsername=%s\nmavenCentralPassword=%s\n' \
  "$KOTLIN_TOOLCHAIN_MAVEN_CENTRAL_USERNAME" \
  "$KOTLIN_TOOLCHAIN_MAVEN_CENTRAL_PASSWORD" > "$snapshot_credentials_file"

for module_file in $module_files; do
  mkdir -p "$backup_dir/$(dirname "$module_file")"
  cp "$module_file" "$backup_dir/$module_file"
  sed -i.bak "s/^    version: .*/    version: $snapshot_version/" "$module_file"
  rm -f "$module_file.bak"
done

./kotlin test
./kotlin publish centralSnapshots
