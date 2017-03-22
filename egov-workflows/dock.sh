./mvnw clean package
sudo docker build -t egovio/egov-workflows:latest .
sudo docker-compose build
sudo docker-compose up
