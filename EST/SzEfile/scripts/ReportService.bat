@echo off
cd %~dp0..
echo  PowerQualityWebService(Java)
setLocal EnableDelayedExpansion
@title PowerQualityWebService(Java)
set CLASSPATH="conf
for /f "tokens=* delims=" %%a in ('dir "*.jar" /b') do (
   set CLASSPATH=!CLASSPATH!;%%a
)
set CLASSPATH=!CLASSPATH!"
java -Xmx512m -Xms1024m -XX:+UseConcMarkSweepGC -Djava.ext.dirs=lib -Dlog4j.configuration=log4j.properties -cp %CLASSPATH% est.szefile.calc.CalcOverload