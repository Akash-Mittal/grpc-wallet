
echo *************** STARTING DATABASE ************
timeout 5
call start-grpc-mysql.bat
echo *************** WAITING FOR DATABASE TO STARTUP ************

timeout 180
echo *************** STARTING SERVER ************
timeout 5
call start-grpc-wallet.bat
timeout 5
echo *************** STATUS OF STARTUP ************
timeout 5
docker ps
timeout 5
