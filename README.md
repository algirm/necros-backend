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

## Deployment server (dockerhub or server build)
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
IS_DEV=<true/false>
PORT=<port>

POSTGRES_HOST=<localhost/docker_service_db_name>
POSTGRES_DB=<db_name>
POSTGRES_USER=<db_user>
POSTGRES_PASSWORD=<db_password>
```
```yml
services:
  db:
    image: postgres
#    platform: linux/amd64
    restart: always
    container_name: necros-db
    volumes:
      - pg-volume:/var/lib/postgresql/data
    env_file:
      - production.env
    ports:
      - "5432:5432"
    networks:
      - nginx.docker
    healthcheck:
      test: [ "CMD-SHELL", "pg_isready -U $$POSTGRES_USER -d $$POSTGRES_DB" ]
      interval: 5s
  ktor:
    image: algirm/necros-backend:latest
#    image: necros-api:0.0.6
    container_name: necros-backend
    restart: always
    ports:
      - "8080:8080"
    env_file:
      - production.env
    networks:
      nginx.docker:
        ipv4_address: 192.168.100.9
    depends_on:
      db:
        condition: service_healthy

volumes:
    pg-volume: {}
networks:
  nginx.docker:
    name: nginx.docker
    external: true

#  nginx.docker:       # to create network via compose
#    driver: bridge
#    ipam:
#      driver: default
#      config:
#        - subnet: 192.168.100.0/16  # or 0/24
```
2. Create docker network if using `external=true` (ip subnet ur choice)
```renderscript
docker network create --driver=bridge --subnet=192.168.100.0/24 nginx.docker
```
3. Login docker using `docker login` to be able pull image
4. Start container 
```
docker-compose up -d
```
5. Any update from src code after push image, run this command to re-deploy
```renderscript
docker-compose stop ktor && docker-compose rm -f ktor && docker rmi -f algirm/necros-backend:latest && docker-compose pull ktor && docker-compose up -d
```
### -*Alternative*- Build docker on server (without dockerhub)
1. Clone git repo and create Dockerfile (outside repo directory)
```renderscript
FROM gradle:8.4-jdk17-alpine AS build
COPY --chown=gradle:gradle /necros-backend /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean buildFatJar --no-daemon --rerun-tasks --no-build-cache || return 1

FROM openjdk:17-alpine
RUN mkdir /outputJar
COPY --from=build /home/gradle/src/build/libs/*.jar /outputJar/necros-backend-all.jar
#RUN gradle clean
EXPOSE 8080:8080
ENTRYPOINT ["java", "-jar", "/outputJar/necros-backend-all.jar"]
```
2. Build docker image
```renderscript
docker build -t necros-api:<version> .
```
3. Use image on `docker-compose.yml` instead of dockerhub image
```renderscript
services:
  ...
  ktor:
    image: necros-api:<version>
  ...
```
4. Run container docker compose to deploy
```renderscript
docker-compose up -d
```
5. When there is new release to deploy, pull git repo and repeat step 2-4 with updated version.