./mvnw clean package
sudo docker build -t egovio/financials:latest .
sudo docker-compose build
sudo docker-compose up
