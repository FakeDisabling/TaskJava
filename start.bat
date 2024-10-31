@echo off
cd /d "%~dp0"


echo Компиляция файлов...
javac -d out -sourcepath src\main\java src\main\java\org\example\Main.java

if %ERRORLEVEL% neq 0 (
    echo Ошибка компиляции!
    exit /b %ERRORLEVEL%
)

echo Запуск программы...
java -cp out org.example.Main

pause
