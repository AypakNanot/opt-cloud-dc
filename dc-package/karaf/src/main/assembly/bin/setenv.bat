@echo off
rem
rem
rem    Licensed to the Apache Software Foundation (ASF) under one or more
rem    contributor license agreements.  See the NOTICE file distributed with
rem    this work for additional information regarding copyright ownership.
rem    The ASF licenses this file to You under the Apache License, Version 2.0
rem    (the "License"); you may not use this file except in compliance with
rem    the License.  You may obtain a copy of the License at
rem
rem       http://www.apache.org/licenses/LICENSE-2.0
rem
rem    Unless required by applicable law or agreed to in writing, software
rem    distributed under the License is distributed on an "AS IS" BASIS,
rem    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
rem    See the License for the specific language governing permissions and
rem    limitations under the License.
rem

rem
rem handle specific scripts; the SCRIPT_NAME is exactly the name of the Karaf
rem script; for example karaf.bat, start.bat, stop.bat, admin.bat, client.bat, ...
rem
rem if "%KARAF_SCRIPT%" == "SCRIPT_NAME" (
rem   Actions go here...
rem )

rem
rem general settings which should be applied for all scripts go here; please keep
rem in mind that it is possible that scripts might be executed more than once, e.g.
rem in example of the start script where the start script is executed first and the
rem karaf script afterwards.
rem

rem
rem The following section shows the possible configuration options for the default 
rem karaf scripts
rem
rem Window name of the windows console
rem SET KARAF_TITLE
rem Location of Java installation
rem SET JAVA_HOME
rem Generic JVM options (for instance, where you can provide memory configuration)
rem SET JAVA_OPTS
rem Additional JVM options
rem SET JAVA_NON_DEBUG_OPTS
rem Additional non-debug JVM options
rem SET EXTRA_JAVA_OPTS 
rem Karaf home folder
rem SET KARAF_HOME
rem Karaf data folder
rem SET KARAF_DATA
rem Karaf base folder
rem SET KARAF_BASE
rem Karaf etc folder
rem SET KARAF_ETC
rem First citizen Karaf options
rem SET KARAF_SYSTEM_OPTS
rem Additional available Karaf options
rem SET KARAF_OPTS
rem Enable debug mode
rem SET KARAF_DEBUG
IF "%JAVA_MAX_MEM%"=="" SET JAVA_MAX_MEM=1024m
@REM SET JAVA_HOME=%~dp0%..\..\common\jre_win_17
SET JAVA_OPT=-Dorg.opendaylight.yangtools.yang.data.impl.schema.builder.retain-child-order=true -Dsystem.core.path=127.0.0.1:38551
SET "JAVA_OPT=%JAVA_OPT% -DopenLog=true"
rem inhibition.capacity.candidate: if set `inhibition.capacity.candidate` value `true`, it's will make device not support candidate on odl when device support candidate
SET "JAVA_OPT=%JAVA_OPT% -Dinhibition.capacity.candidate=true"
rem rekey bytes default = 1024 * 1024 * 1024, 0 disabled
rem SET "JAVA_OPT=%JAVA_OPT% -Drekey-bytes-limit=0"
rem rekey time default 1 hour, 0 disabled
rem SET "JAVA_OPT=%JAVA_OPT% -Drekey-time-limit=0"
rem rekey packets 0 disabled
rem SET "JAVA_OPT=%JAVA_OPT% -Drekey-packets-limit=0"



rem ===========================================================================================
rem  auto memory setting
rem ===========================================================================================

:: 定义一个变量来存储最终的内存大小（GB）
set "totalMemoryGB="

:: 检查系统是否支持 PowerShell
goto :end
powershell -Command "exit 0"
if %errorlevel% neq 0 (
    echo PowerShell is not available. Falling back to end.
    goto :end
)

:: PowerShell 可用，检查其版本
for /f "tokens=2 delims==" %%v in ('powershell -Command "(Get-Module -ListAvailable PowerShell*).Version.Major"') do (
    set "powershellVersion=%%v"
)

:: 根据 PowerShell 版本选择命令
if !powershellVersion! GEQ 3 (
    :: PowerShell 版本 3 或更高，使用 Get-CimInstance
    for /f "tokens=*" %%a in ('powershell -Command "try {Get-CimInstance Win32_PhysicalMemory | Measure-Object -Property Capacity -Sum | ForEach-Object {($_.Sum / 1GB) -as [int]}} catch {exit 1}"') do (
        set "totalMemoryGB=%%a"
    )
) else if !powershellVersion! LSS 3 (
	:: PowerShell 版本 小于3，使用 Get-WmiObject
    for /f "tokens=*" %%a in ('powershell -Command "try {Get-WmiObject Win32_PhysicalMemory | Measure-Object -Property Capacity -Sum | ForEach-Object {($_.Sum / 1GB) -as [int]}} catch {exit 1}"') do (
		set "totalMemoryGB=%%a"
    )
)  else (
	:: PowerShell 版本低于 3，但高于 2，可能需要额外逻辑来处理具体版本
    echo PowerShell version is lower than 3. Using end if available.
    goto :end
)

:: 如果 PowerShell 成功获取了内存大小，则直接退出脚本
if defined totalMemoryGB (
    goto :end
)

:end
:: 检查是否成功获取内存大小
if not defined totalMemoryGB (
    echo Failed to retrieve total memory size.
) else (
    :: 根据内存大小设置 MEY_PROFILE 变量
    if !totalMemoryGB! GEQ 30 (
        set "MEY_PROFILE=HIGH"
    ) else if !totalMemoryGB! GTR 14 (
        set "MEY_PROFILE=MEDIUM"
    ) else if !totalMemoryGB! GTR 7 (
        set "MEY_PROFILE=LOW"
    )
    :: 输出结果
    echo MEY_PROFILE is set to !MEY_PROFILE!
)

rem ==========================================================================================

rem Configure LOW memory
set "MEY_PROFILE_LOW=-server -Xms512m -Xmx1024m -Xmn256m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m"
rem Configure MEDIUM memory
set "MEY_PROFILE_MEDIUM=-server -Xms1024m -Xmx2048m -Xmn512m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=1024m -XX:MinHeapFreeRatio=40 -XX:MaxHeapFreeRatio=70"
set "MEY_PROFILE_MEDIUM=%MEY_PROFILE_MEDIUM% -XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:G1ReservePercent=10 -XX:InitiatingHeapOccupancyPercent=30 -XX:SoftRefLRUPolicyMSPerMB=0"
set "MEY_PROFILE_MEDIUM=%MEY_PROFILE_MEDIUM% -Xss1024k -XX:NewRatio=8 -XX:SurvivorRatio=32"
rem Configure HIGH memory
set "MEY_PROFILE_HIGH=-server -Xms2048m -Xmx4096m -Xmn1024m -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=2048m -XX:MinHeapFreeRatio=40 -XX:MaxHeapFreeRatio=70"
set "MEY_PROFILE_HIGH=%MEY_PROFILE_HIGH% -XX:+UseG1GC -XX:G1HeapRegionSize=32m -XX:G1ReservePercent=10 -XX:InitiatingHeapOccupancyPercent=30 -XX:SoftRefLRUPolicyMSPerMB=0"
set "MEY_PROFILE_HIGH=%MEY_PROFILE_HIGH% -Xss2048k -XX:NewRatio=8 -XX:SurvivorRatio=32"

rem memory setting profile ,LOW、MEDIUM、HIGH, Configure different memory percentages for different settings, with LOW set as the default.
set "_MEY_PROFILE=%MEY_PROFILE%"
echo "memory profile %_MEY_PROFILE%."
if "%_MEY_PROFILE%" == "HIGH" (
    set "JAVA_OPT=%JAVA_OPT% %MEY_PROFILE_HIGH%"
)else if "%_MEY_PROFILE%" == "LOW" (
    set "JAVA_OPT=%JAVA_OPT% %MEY_PROFILE_LOW%"
)else (
    set "JAVA_OPT=%JAVA_OPT% %MEY_PROFILE_MEDIUM%"
)
echo "memory setting %JAVA_OPT%."
set "JAVA_OPTS=%JAVA_OPT%"