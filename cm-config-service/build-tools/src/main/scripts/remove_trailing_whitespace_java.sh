find . -type f -name "*.java" -print | xargs sed -i 's/[[:space:]]*$//'
