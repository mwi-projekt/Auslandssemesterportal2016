# Build

# Dev Deployment

### Zum Beginn der Entwicklung:
1. Docker starten
2. <code>docker-compose up -d</code>
wie bisher auch

### Bei jeder Änderung:
statt <code>maven install</code> wie bisher, jetzt das ausführen:
<code>mvn tomcat7:deploy -P dev</code>
- <code>docker-compose pull</code>
- <code>docker-compose build</code>
- <code>docker-compose up -d</code>

# Frontend Tooling

Durch den Bundler Parcel.js können Änderungen im Frontend gemacht werden, welche dank Hot-Reloading innerhalb kürzester Zeit im Browser einsehbar sind.

Dafür muss der Parcel-Dev-Server mit <code>npm run parcel</code> gestartet werden. Dieser bündelt alle Dateien und stellt sie auf einem temporären Webserver unter http://localhost:1234 zur Verfügung.

Falls es zu Fehlern mit Parcel kommt, sollten die Ordner <code>.cache</code> und <code>dist</code> gelöscht werden.
Hierfür kann das Skript <code>npm run clean:parcel</code> ausgeführt werden.
