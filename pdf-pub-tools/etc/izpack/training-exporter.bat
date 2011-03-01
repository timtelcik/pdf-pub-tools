@echo off

IF "%JAVA_HOME%"=="" SET LOCAL_JAVA=java
IF NOT "%JAVA_HOME%"=="" SET LOCAL_JAVA=%JAVA_HOME%\bin\java

set basedir=%~f0
:strip
set removed=%basedir:~-1%
set basedir=%basedir:~0,-1%
if NOT "%removed%"=="\" goto strip
set APP_HOME=%basedir%


@rem Run with no command window. This may not work with older versions of Windows. Use the command above then.
start "Liferay Training Exporter" /B "%LOCAL_JAVA%w" -Xmx256m -jar TrainingExporter.jar


