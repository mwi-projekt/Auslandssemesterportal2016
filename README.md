# Build

- docker-compose pull
- docker-compose build
- docker-compose up -d

# Dev Deployment

### Zum begin der Entwicklung:
1. Docker starten
2. docker-compose up -d
wie bisher auch

### Bei jeder Änderung:
statt maven install wie bisher, jetzt das ausführen:
mvn tomcat7:deploy -P dev