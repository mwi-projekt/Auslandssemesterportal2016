package dhbw.mwi.Auslandsemesterportal2016;

import edu.emory.mathcs.backport.java.util.Arrays;
import java.util.List;

public class Config {

	public static final String URL = System.getenv("MWI_URL");
	public static final String PARCEL_URL = System.getenv("PARCEL_URL");
	public static final String CAMUNDA_URL = URL + "/camunda";
	public static final String MWI_URL = URL;
	public static final List<String> AllowedOrigins =  Arrays.asList(new String[]{URL,PARCEL_URL});
	
	public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
	public static final String DB_URL= System.getenv("MWI_DB_URL");
	
	public static final String MAIL_HOST = "smtp.gmail.com";
	public static final String MAIL_USER = "mwiausland@gmail.com";
	public static final String MAIL_PASS = "MWIAusland1";
	
	public static final String UPLOAD_DIR = "/camunda/upload";
	public static final String UPLOAD_URL = URL + "/files/";
	
}
