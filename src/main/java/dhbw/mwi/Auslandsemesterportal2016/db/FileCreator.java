package dhbw.mwi.Auslandsemesterportal2016.db;

import dhbw.mwi.Auslandsemesterportal2016.Config;

import java.io.File;

public class FileCreator {

    private FileCreator() {
    }

    public static File getUploadFolder() {
        return new File(Config.UPLOAD_DIR);
    }

    public static File getFileInUploadFolder(String fileName) {return new File(Config.UPLOAD_DIR + "/"  + fileName);}

    public static File getBPMNFile(String model, ClassLoader classLoader) {
        return new File(classLoader.getResource(model + ".bpmn").getFile());
    }
}
