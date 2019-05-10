import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * @author Halim Delic
 * @version final_1
 *          <p>
 *          Klasa koja predstavlja knjige biblioteke
 *          </p>
 *
 */
@SuppressWarnings("serial")
public class Knjiga extends Zapisnik implements Serializable {

	private int brojKnjige;
	private String imeKnjige;
	private boolean statusKnjige;

	private static Zapisnik zapisnik = new Zapisnik();
	private static ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();

	/**
	 * 
	 * <p>
	 * Metoda koja ucitaje fajl knjige.txt i dodaje knjige u array listu
	 * </p>
	 * 
	 */

	public static void loading() {

		try {

			File f = new File("knjige.txt");

			FileInputStream in = new FileInputStream(f);
			@SuppressWarnings("resource")
			ObjectInputStream oin = new ObjectInputStream(in);

			while (true)
				knjige.add((Knjiga) oin.readObject());

		} catch (EOFException ex) {
		} catch (Exception e) {
		}
	}

	public Knjiga() {

	}

	/**
	 * 
	 * @param brojKnjige
	 * @param imeKnjige
	 * @param statusKnjige
	 * @throws IOException
	 *                     <p>
	 *                     Konstruktor za inicijalizaciju polja objekta tipa Knjiga
	 *                     </p>
	 */

	public Knjiga(int brojKnjige, String imeKnjige, boolean statusKnjige) throws IOException {

		if (provjeraZaKreacijuKnjige(brojKnjige)) {

			this.brojKnjige = brojKnjige;
			this.imeKnjige = imeKnjige;
			this.statusKnjige = statusKnjige;

			knjige.add(this);

			zapisnik.zapisiKreacijuKnjige(brojKnjige, imeKnjige);

			System.out.println("Knjiga je uspjesno kreirana!");

		}

	}

	/**
	 * 
	 * @param brojKnjige
	 * @return boolean
	 *         <p>
	 *         Metoda koja provjerava da li knjiga moze biti kreirana sa
	 *         proslijedjenim poljima
	 *         </p>
	 */

	private boolean provjeraZaKreacijuKnjige(int brojKnjige) {

		if (brojKnjige < 0) {
			System.out.println("Unos broja knjige ne moze biti negativan broj. Knjiga nije uspjesno kreirana!");
			return false;
		}

		for (int i = 0; i < knjige.size(); i++)
			if (knjige.get(i).brojKnjige == brojKnjige) {
				System.out.println("Unijeti broj knjige vec postoji. Knjiga nije uspjesno kreirana!");
				return false;
			}

		return true;

	}

	/**
	 * 
	 * @param brojRacuna
	 * @param brojKnjige
	 * @param datumPodizanja
	 * @throws IOException
	 *                     <p>
	 *                     Metoda za izvrsavanje podizanja knjige i spremanja
	 *                     podataka u zapisnik
	 *                     </p>
	 */

	public static void podizanjeKnjige(int brojRacuna, int brojKnjige, Date datumPodizanja) throws IOException {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String datumPodizanjaString = dateFormat.format(datumPodizanja);

		if (provjeraZaPodizanjeKnjige(brojRacuna, brojKnjige)) {

			Knjiga.getKnjiga(brojKnjige).statusKnjige = true;
			Racun.getRacun(brojRacuna).setBrojPosudenihKnjiga(Racun.getRacun(brojRacuna).getBrojPosudenihKnjiga() + 1);
			System.out.println("Knjiga je uspjesno podignuta!");

			zapisnik.zapisiPodizanjeKnjige(brojKnjige, Knjiga.getKnjiga(brojKnjige).imeKnjige, brojRacuna,
					Racun.getRacun(brojRacuna).getImeMusterije(), datumPodizanjaString);

		}

	}

	/**
	 * @param brojKnjige
	 * @return null
	 *         <p>
	 *         Getter za knjigu
	 *         </p>
	 */

	private static Knjiga getKnjiga(int brojKnjige) {

		int i = 0;

		for (i = 0; i < knjige.size(); i++)
			if (knjige.get(i).brojKnjige == brojKnjige)
				return knjige.get(i);

		return null;
	}

	/**
	 * 
	 * @param brojRacuna
	 * @param brojKnjige
	 * @return boolean
	 *         <p>
	 *         Metoda koja provjerava da li moze biti izvrseno podizanje knjige sa
	 *         proslijedjenim poljima
	 *         </p>
	 */
	private static boolean provjeraZaPodizanjeKnjige(int brojRacuna, int brojKnjige) {

		Racun trenutniRacun = Racun.getRacun(brojRacuna);

		if (trenutniRacun == null) {
			System.out.println("Unijeti racun nije pronadjen. Knjiga nije uspjesno podignuta!");
			return false;
		}

		Knjiga trenutnaKnjiga = Knjiga.getKnjiga(brojKnjige);

		if (trenutnaKnjiga == null) {
			System.out.println("Trazena knjiga nije pronadjena!");
			return false;
		}

		if (trenutniRacun.getBrojPosudenihKnjiga() == 3) {
			System.out.println("Unijeti racun vec ima tri podignute knjige. Knjiga nije uspjesno podignuta!");
			return false;
		}

		if (trenutnaKnjiga.statusKnjige) {
			System.out.println("Trazena knjiga je vec podignuta!");
			return false;
		}

		return true;

	}

	/**
	 * 
	 * @param brojRacuna
	 * @param brojKnjige
	 * @param datumVracanja
	 * @throws IOException
	 *                     <p>
	 *                     Metoda za izvrsavanje vracanja knjige i spremanja
	 *                     podataka u zapisnik
	 *                     </p>
	 */
	public static void vracanjeKnjige(int brojRacuna, int brojKnjige, Date datumVracanja) throws IOException {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String datumVracanjaString = dateFormat.format(datumVracanja);

		if (provjeraZaVracanjeKnjige(brojRacuna, brojKnjige)) {

			Knjiga.getKnjiga(brojKnjige).statusKnjige = false;
			Racun.getRacun(brojRacuna).setBrojPosudenihKnjiga(Racun.getRacun(brojRacuna).getBrojPosudenihKnjiga() - 1);
			System.out.println("Knjiga je uspjesno vracena!");

			zapisnik.zapisiVracanjeKnjige(brojKnjige, Knjiga.getKnjiga(brojKnjige).imeKnjige, brojRacuna,
					Racun.getRacun(brojRacuna).getImeMusterije(), datumVracanjaString);

		}

	}

	/**
	 * 
	 * @param brojRacuna
	 * @param brojKnjige
	 * @return boolean
	 *         <p>
	 *         Metoda koja provjerava da li moze biti izvrseno vracanje knjige sa
	 *         proslijedjenim poljima
	 *         </p>
	 */

	private static boolean provjeraZaVracanjeKnjige(int brojRacuna, int brojKnjige) {

		Racun trenutniRacun = Racun.getRacun(brojRacuna);

		if (trenutniRacun == null) {
			System.out.println("Unijeti racun nije pronadjen. Knjiga nije uspjesno vracena!");
			return false;
		}

		Knjiga trenutnaKnjiga = Knjiga.getKnjiga(brojKnjige);

		if (trenutnaKnjiga == null) {
			System.out.println("Trazena knjiga nije pronadjena!");
			return false;
		}

		if (!trenutnaKnjiga.statusKnjige) {
			System.out.println("Trazena knjiga nije podignuta!");
			return false;
		}

		return true;

	}

	/**
	 * 
	 * @throws IOException
	 *                     <p>
	 *                     Metoda koja pohranjuje objekte knjige u file knjige.txt
	 *                     </p>
	 */

	public static void save() throws IOException {
		FileOutputStream in = new FileOutputStream("knjige.txt");
		ObjectOutputStream oin = new ObjectOutputStream(in);

		for (int i = 0; i < knjige.size(); i++)
			oin.writeObject(knjige.get(i));

		oin.close();
		System.out.println("Goodbye...");
	}

}
