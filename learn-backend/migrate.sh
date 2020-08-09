cd ..
./build.sh
cd learn-backend
java -jar target/learn-backend-0.0.1-SNAPSHOT.jar db status src/configs/config.yml
java -jar target/learn-backend-0.0.1-SNAPSHOT.jar db migrate src/configs/config.yml
java -jar target/learn-backend-0.0.1-SNAPSHOT.jar db status src/configs/config.yml