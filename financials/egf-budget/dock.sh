./mvnw clean package
sudo docker build -t egovio/egf-budget:latest .
sudo docker-compose build
sudo docker-compose up
