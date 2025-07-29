#!/bin/bash

# Ensure you're starting clean
git checkout main || exit 1

# Define array of branches and their cherry-pick commits
declare -A branches
branches["feature/input-validation"]="d77940e cd505c9 af2cff3 d7ae50f 4540d3b c7edf74 3fce02b"
branches["feature/duplicate-checks"]="e394095 649a279 d629293 8277928 6168f45 c49a6f7 92d1511 0d1dee4 8505e8b f313ab4 639dee1"
branches["feature/update-flow"]="8aba6df f55f7a6 27fac0d 3c1f79c 15e32d4 8505e8b"
branches["feature/navigation-enhancement"]="8e0c65d 64d1575 232efff 2963d70"
branches["refactor/main-cleanup"]="e6d8177 febcbe8 dc60cd9 d25c60d 3821430 e5a35ec 51cc791"
branches["bugfix/database-lock"]="5cf8385"
branches["docs/readme-updates"]="2963d70 2aa75f2 03cb4e3 ea5249a 84c904a"

for branch in "${!branches[@]}"; do
  echo "⏳ Working on $branch ..."
  git checkout main || exit 1
  git branch -D $branch 2>/dev/null
  git checkout -b $branch || exit 1

  for commit in ${branches[$branch]}; do
    git cherry-pick "$commit" || {
      echo "⚠️ Conflict on commit $commit in $branch"
      echo "Resolve it, then run: git cherry-pick --continue"
      exit 1
    }
  done

  # Merge to dev
  git checkout dev || exit 1
  git merge --no-ff $branch -m "Merge $branch into dev"
done

echo "✅ All branches created, cherry-picked, and merged into dev."