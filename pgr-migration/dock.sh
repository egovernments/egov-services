mvn clean package
docker build -t egovio/pgr-migration:latest .
docker-compose build
docker-compose up
