## Deployment local
1. Build
```renderscript
./gradlew build
```
2. Copy `.jar` file on directory `/build/libs/necros-backend-all.jar`
3. Run server by execute this command (require jdk)
```renderscript
java -jar necros-backend-all.jar -port=8080
```

## Deployment server (dockerhub)
### Docker config (to push fatJar into dockerhub)
* Create docker hub repository
* Adjust configuration docker on `build.gradle.kts` root project of the repo name
& Setup environment 
<br/><i>from gradle task configuration </i>
```renderscript
DOCKER_HUB_USERNAME=<docker_usernane>;DOCKER_HUB_PASSWORD=<docker_password>
```
<i>or by the command </i>

```
set VARIABLE_NAME=value
```
* Run gradle task
```
./gradlew publishImage
```
* If error occured, crosscheck if docker environment above is set correctly in gradle task configuration

### Server side config
1. Setup env and docker-compose.yml
```renderscript
POSTGRES_DB=<db_name>
POSTGRES_USER=<db_user>
POSTGRES_PASSWORD=<db_password>
```
```renderscript
services:
  db:
    image: postgres
    restart: always
    container_name: necros-db
    volumes:
      - pg-volume:/var/lib/postgresql/data
    env_file:
      - postgres.env
    ports:
      - "5432:5432"
    healthcheck:
      test: [ "CMD-SHELL", "pg_isready -U $$POSTGRES_USER -d $$POSTGRES_DB" ]
      interval: 5s
  ktor:
    image: algirm/necros-backend:latest
    container_name: necros-backend
    restart: always
    ports:
      - "8080:8080"
    env_file:
      - postgres.env
    depends_on:
      db:
        condition: service_healthy

volumes:
    pg-volume: {}
```
2. Login docker using `docker login` to be able pull image
3. Start container 
```
docker-compose up -d
```
4. Any update from src code after push image, run this command to re-deploy
```renderscript
docker-compose stop ktor && docker-compose rm -f ktor && docker rmi -f algirm/necros-backend:latest && docker-compose pull ktor && docker-compose up -d
```