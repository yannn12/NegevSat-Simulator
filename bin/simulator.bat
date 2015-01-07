@ECHO OFF

@echo Before you start, are you sure you have Java 8 32bit with UART jars inside the bin folder (also JAVA_HOME should be defined) ?
SET /P ANSWER=Do you want to continue (Y/N)? 
echo You chose: %ANSWER%
 
if /i {%ANSWER%}=={y} (goto :yes) 
if /i {%ANSWER%}=={yes} (goto :yes)
if /i {%ANSWER%}=={n} (goto :END)
if /i {%ANSWER%}=={no} (goto :END)

echo Wrong choice, please next time choose y/n
goto END

:yes
if "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto END

SET /P port=Please enter serial port name:
IF %port%=="" GOTO PortNotDefined
"%JAVA_HOME%\bin\java.exe" -cp simulator-1.0-SNAPSHOT.jar Simulator "%port%"
goto END

:noJavaHome
@echo please set up JAVA_HOME environment variable - java should be version 8 and 32bit 
goto END

:PortNotDefined
@echo didn't enter port, will use COM1 as default
"%JAVA_HOME%\bin\java.exe" -cp simulator-1.0-SNAPSHOT.jar Simulator
goto END

:END

