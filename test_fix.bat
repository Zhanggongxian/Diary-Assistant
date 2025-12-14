@echo off
echo 测试修复后的日记助手功能...
echo.
echo 1. 检查服务状态
java -jar target/diary-assistant-1.0.0.jar --spring.shell.interactive.enabled=false --command="检查服务"
echo.
echo 2. 测试开始日记功能
java -jar target/diary-assistant-1.0.0.jar --spring.shell.interactive.enabled=false --command="开始日记"
echo.
echo 3. 测试记录功能
java -jar target/diary-assistant-1.0.0.jar --spring.shell.interactive.enabled=false --command="记录 测试早上十点半起床"
echo.
echo 4. 测试结束日记功能
java -jar target/diary-assistant-1.0.0.jar --spring.shell.interactive.enabled=false --command="结束日记"
echo.
echo 测试完成！