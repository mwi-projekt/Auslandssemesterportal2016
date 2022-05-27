package dhbw.mwi.Auslandsemesterportal2016.db;

import java.util.Objects;

public class User {

	public int id;
	public int rolle;
	public String nachname;
	public String vorname;
	public String email;
	public String matrikelnummer;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return id == user.id && rolle == user.rolle && Objects.equals(nachname, user.nachname) && Objects.equals(vorname, user.vorname) && Objects.equals(email, user.email) && Objects.equals(matrikelnummer, user.matrikelnummer);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, rolle, nachname, vorname, email, matrikelnummer);
	}
}
