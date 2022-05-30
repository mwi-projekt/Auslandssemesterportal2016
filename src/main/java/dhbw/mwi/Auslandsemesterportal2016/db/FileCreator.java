package dhbw.mwi.Auslandsemesterportal2016.db;

import dhbw.mwi.Auslandsemesterportal2016.Config;

import java.io.File;

public class FileCreator {
    public static File getUploadFolder() {
        return new File(Config.UPLOAD_DIR);
    }
}
