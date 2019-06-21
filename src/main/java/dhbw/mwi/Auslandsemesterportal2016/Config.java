package dhbw.mwi.Auslandsemesterportal2016;

public class Config {

	public static final String URL = "http://10.3.15.45:8080";
	public static final String CAMUNDA_URL = URL + "/camunda";
	public static final String MWI_URL = URL + "/Auslandssemesterportal/WebContent";
	
	public static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_URL="jdbc:mysql://10.3.15.45:3306/mwi?autoReconnect=true";
	public static final String USER = "mwi";
	public static final String PASS = "mwi2014";
	
	public static final String UPLOAD_DIR = "/var/www/files";
	public static final String UPLOAD_URL = "http://10.3.15.45/files/";
	
}
