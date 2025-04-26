:: scripts/stop-all-services.bat
    @echo off
    echo Stopping all microservices...
    for %%p in (8081 8082 8083) do (
        for /f "tokens=5" %%i in ('netstat -aon ^| findstr :%%p ^| findstr LISTENING') do (
            echo Terminating process with PID %%i on port %%p
            taskkill /PID %%i /F
        )
    )
    echo All microservices stopped.
    del user.pid order.pid payment.pid
    pause