@ECHO OFF
SETLOCAL ENABLEDELAYEDEXPANSION
REM Change to the packages directory starting from the directory of this script:
cd %~dp0/..
SET ROOT_FOLDER=%cd%

echo "Running publish.exe from %ROOT_FOLDER%"
pushd %ROOT_FOLDER%

set ftime=%TIME: =0%
set BUILD_QUALIFIER=%date:~-4%%date:~3,2%%date:~0,2%_%ftime:~0,2%%ftime:~3,2%
echo "Using build qualifier %BUILD_QUALIFIER%"
SET REPO_VERSION=1.0.0
echo "Repository version %REPO_VERSION%" 

SET DOWNLOAD_FOLDER=\\git\platform\downloads\testing.sample.p2.repository
IF not exist %DOWNLOAD_FOLDER% (
REM we are not on the eclipsesource build machine. for testing, let's
REM deploy the build inside the builds folder of git\platform\downloads\testing.sample.p2.repository
   SET DOWNLOAD_FOLDER=%ROOT_FOLDER%\target\downloads\testing.sample.p2.repository
   ECHO Creating %DOWNLOAD_FOLDER%  in %ROOT_FOLDER% 
)
SET DOWNLOAD_FOLDER=%DOWNLOAD_FOLDER%
  IF not exist %DOWNLOAD_FOLDER% (
  	echo creating folder %DOWNLOAD_FOLDER%
  	mkdir %DOWNLOAD_FOLDER%
  )          
SET BUILD_VERSION=v%REPO_VERSION%
echo "Creating p2 repo dir %DOWNLOAD_FOLDER%\%BUILD_VERSION%" 
mkdir %DOWNLOAD_FOLDER%\%BUILD_VERSION%
       
echo "Copying repo to %DOWNLOAD_FOLDER%\%BUILD_VERSION%" 
xcopy /e /v "%ROOT_FOLDER%\target\repository" "%DOWNLOAD_FOLDER%\%BUILD_VERSION%"

popd