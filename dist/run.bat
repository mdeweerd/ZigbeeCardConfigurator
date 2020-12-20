set       MYROOTPATH=%~dp0
set           MYDIST=%MYROOTPATH%dist
set            MYLIB=%MYROOTPATH%lib
set         RXTXPATH=%MYLIB%\rxtx-2.2pre2-bins
set MYSYSTEMRXTXBINS=%RXTXPATH%\Windows\win64
set             PATH=%MYSYSTEMRXTXBINS%;%MYROOTPATH%;%JAVA_HOME%\bin;%SystemRoot%\SysWOW64;%PATH%
#set        CLASSPATH=%MYDIST%\BT747_j2se.jar;%MYLIB%\swingx-ws.jar;%MYLIB%\swingx.jar;%RXTXPATH%\RXTXcomm.jar;%MYDIST%\libBT747.jar;%MYLIB%\swing-layout-1.0.3.jar;%MYLIB%\jopt-simple-3.1.jar;%MYLIB%\jchart2d-3.1.0.jar;%CLASSPATH%;

SET CLASSPATH=%MYROOTPATH%2igbee Tool.jar;%MYLIB%\RXTXcomm.jar;%MYLIB%\appframework-1.0.3.jar;%MYLIB%\swing-worker-1.1.jar;%MYLIB%\toplink-essentials-agent.jar;%MYLIB%\toplink-essentials.jar;%CLASSPATH%;
REM java --class-path "%CLASSPATH%" -jar "Zigbee Tool.jar"
rem java  -Djava.library.path=%MYLIB%
java -jar "%MYROOTPATH%ZigBee Tool.jar" zigbeetool.ZigBeeToolApp
rem java --list-modules -cp "%MYROOTPATH%Zigbee Tool.jar" zigbeetool.ZigBeeToolApp
