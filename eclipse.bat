@echo off
call mvn eclipse:clean
call mvn eclipse:eclipse -DdownloadSources=true
@pause