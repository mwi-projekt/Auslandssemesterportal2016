# How to Install

## Requirements
- Installed [GIT](https://git-scm.com/download/win)
- Installed [Java JDK](https://adoptopenjdk.net/)
- Installed [Docker](https://hub.docker.com/editions/community/docker-ce-desktop-windows/)

## Checkout Code
If you are experienced with GIT and Bash Commands you can clone the repository by using that command:

```
git clone https://github.com/mwi-projekt/Auslandssemesterportal2016.git
```

Otherwise it's recommended to use a UI like [SourceTree](https://www.sourcetreeapp.com/)

## Setup Workspace
This Guide will explain how to setup the project with eclipse. If you are more familiar with another code editor, feel free to use that one.

Be sure that you have installed the Eclipse IDE for Enterprise Java Developers otherwise the project will not build properly. You can download it [here](https://www.eclipse.org/downloads/packages/);

### Configure Eclipse
After you have installed Eclipse, it's important to configure the location of your installed Java JDK.

Open the Eclipse Settings and select on the Sidebar

`Java -> Installed JREs -> Press Add -> Standard VM -> Select the Path -> Done`

Your JDK in windows is usally installed in `C:\Program Files\Java\jdk....`

### Import Project
After you have checkouted the project in the last step, you need to import it in Eclipse.

`File -> Import -> Existing Maven Projects -> Select the path -> Finish`

# Build the Project
We can now build the project for the first time, therfore it's important that Docker is started, because it's needed in the build process.

`Run -> Run as -> Maven install`

# Start the Server locally
If you want to start the Server locally, you can use docker-compose.
First start docker, build your project and then you start the server by using these three commands in the bash:

```
docker-compose build
docker-compose pull
docker-compose up -d

# Dev Deployment
mvn tomcat7:deploy -P dev
```

# Frontend Tooling

Durch den Bundler Parcel.js können Änderungen im Frontend gemacht werden, welche dank Hot-Reloading innerhalb kürzester Zeit im Browser einsehbar sind.

Dafür muss der Parcel-Dev-Server mit <code>npm run parcel</code> gestartet werden. Dieser bündelt alle Dateien und stellt sie auf einem temporären Webserver unter http://localhost:1234/index.html zur Verfügung.

Falls es zu Fehlern mit Parcel kommt, sollten die Ordner <code>.cache</code> und <code>dist</code> gelöscht werden.
Hierfür kann das Skript <code>npm run clean:parcel</code> ausgeführt werden.
