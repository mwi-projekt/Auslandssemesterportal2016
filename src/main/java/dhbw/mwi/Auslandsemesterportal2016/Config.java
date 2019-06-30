package dhbw.mwi.Auslandsemesterportal2016;

public class Config {

	public static final String URL = System.getenv("MWI_URL");
	public static final String CAMUNDA_URL = URL + "/camunda";
	public static final String MWI_URL = URL + "/Auslandssemesterportal/WebContent";
	
	public static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_URL= System.getenv("MWI_DB_URL");
	public static final String USER = System.getenv("MWI_DB_USER");
	public static final String PASS = System.getenv("MWI_DB_PW");
	
	public static final String MAIL_HOST = "smtp.gmail.com";
	public static final String MAIL_USER = "mwiausland@gmail.com";
	public static final String MAIL_PASS = "MWIAusland1";
	
	public static final String UPLOAD_DIR = "/var/www/files";
	public static final String UPLOAD_URL = "http://10.3.15.45/files/";
	
}
