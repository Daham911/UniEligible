@echo off
setlocal
if exist "C:\Users\induw\.maven\apache-maven-3.9.9\bin\mvn.cmd" (
    "C:\Users\induw\.maven\apache-maven-3.9.9\bin\mvn.cmd" %*
) else (
    mvn %*
)

