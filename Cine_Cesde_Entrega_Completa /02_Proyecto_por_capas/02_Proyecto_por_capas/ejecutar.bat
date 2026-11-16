@echo off
setlocal
cd /d "%~dp0"
call mvn compile org.codehaus.mojo:exec-maven-plugin:3.5.0:java -Dexec.mainClass=app.App
pause
