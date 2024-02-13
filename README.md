## Deployment
1. Build
```renderscript
./gradlew build
```
2. Copy `.jar` file on directory `/build/libs/necros-backend-all.jar`
3. Run server by execute this command (require jdk)
```renderscript
java -jar necros-backend-all.jar -port=8080
```