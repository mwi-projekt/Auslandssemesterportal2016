package dhbw.mwi.Auslandsemesterportal2016.db;

import dhbw.mwi.Auslandsemesterportal2016.Config;

import java.io.File;

public class FileCreator {

    private FileCreator() {
    }

    public static File getUploadFolder() {
        return new File(Config.UPLOAD_DIR);
    }

    public static File getBPMNFile(String model) {
        ClassLoader classLoader = FileCreator.class.getClassLoader();
        return new File(classLoader.getResource(model + ".bpmn").getFile());
    }
}
