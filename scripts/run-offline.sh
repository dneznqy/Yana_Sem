#!/usr/bin/env bash
set -euo pipefail

REPO_PATH=${1:-".maven-cache"}

if [ ! -d "$REPO_PATH" ]; then
  echo "Локальный репозиторий зависимостей '$REPO_PATH' не найден." >&2
  echo "Создайте его на машине с доступом к интернету: mvn -Dmaven.repo.local=$REPO_PATH dependency:go-offline" >&2
  echo "После копирования каталога сюда запустите: $(basename "$0") $REPO_PATH" >&2
  exit 1
fi

mvn -Dmaven.repo.local="$REPO_PATH" spring-boot:run
