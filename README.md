# Build

- docker-compose pull
- docker-compose build
- docker-compose up -d

# Frontend Tooling

Durch den Bundler Parcel.js können Änderungen im Frontend gemacht werden, welche dank Hot-Reloading innerhalb kürzester Zeit im Browser einsehbar sind.

Dafür muss der Parcel-Dev-Server mit <code>npm run parcel</code> gestartet werden. Dieser bündelt alle Dateien und stellt sie auf einem temporären Webserver unter http://localhost:1234 zur Verfügung.

Falls es zu Fehlern mit Parcel kommt, sollten die Ordner <code>.cache</code> und <code>dist</code> gelöscht werden.
Hierfür kann das Skript <code>npm run clean:parcel</code> ausgeführt werden.