./mvnw clean package
sudo docker build -t egovio/egov-common-workflows:latest .
sudo docker-compose build
sudo docker-compose up
