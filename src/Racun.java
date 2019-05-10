import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author Halim Delic
 * @version final_1
 *          <p>
 *          Klasa koja predstavlja racun biblioteke
 *          </p>
 */
@SuppressWarnings("serial")
public class Racun extends Zapisnik implements Serializable {

	private int brojRacuna;
	private String imeMusterije;
	private int brojPosudenihKnjiga;

	private static Zapisnik zapisnik = new Zapisnik();
	private static ArrayList<Racun> racuni = new ArrayList<Racun>();

	/**
	 * 
	 * <p>
	 * Metoda koja ucitaje fajl racuni.txt i dodaje racune u array listu
	 * </p>
	 * 
	 */

	public static void loading() {

		try {

			File f = new File("racuni.txt");

			FileInputStream in = new FileInputStream(f);
			@SuppressWarnings("resource")
			ObjectInputStream oin = new ObjectInputStream(in);

			while (true)
				racuni.add((Racun) oin.readObject());

		} catch (EOFException ex) {
		} catch (Exception e) {
		}
	}

	public Racun() {

	}

	/**
	 * 
	 * @param brojRacuna
	 * @param imeMusterije
	 * @param brojPosudenihKnjiga
	 * @throws IOException
	 *                     <p>
	 *                     Konstruktor za inicijalizaciju polja objekta tipa Racun
	 *                     </p>
	 */

	public Racun(int brojRacuna, String imeMusterije, int brojPosudenihKnjiga) throws IOException {

		if (provjeraZaKreacijuRacuna(brojRacuna)) {

			this.brojRacuna = brojRacuna;
			this.imeMusterije = imeMusterije;
			this.brojPosudenihKnjiga = brojPosudenihKnjiga;

			racuni.add(this);

			zapisnik.zapisiKreacijuRacuna(brojRacuna, imeMusterije);

			System.out.println("Racun je uspjesno kreiran!");

		}

	}

	/**
	 * 
	 * @param brojRacuna
	 * @return poruku
	 *         <p>
	 *         Metoda koja ispisuje racun
	 *         </p>
	 */

	public static String ispisRacuna(int brojRacuna) {

		for (int i = 0; i < racuni.size(); i++)
			if (brojRacuna == racuni.get(i).brojRacuna)
				return "Broj racuna: " + racuni.get(i).brojRacuna + "\nIme musterije: " + racuni.get(i).imeMusterije
						+ "\nBroj posudjenih knjiga: " + racuni.get(i).brojPosudenihKnjiga;

		return "Unijeti broj racuna ne postoji.";

	}

	/**
	 * 
	 * @param brojRacuna
	 * @return boolean
	 *         <p>
	 *         Metoda koja provjerava da li racun moze biti kreiran sa
	 *         proslijedjenim poljima
	 *         </p>
	 */

	private boolean provjeraZaKreacijuRacuna(int brojRacuna) {

		if (brojRacuna < 0) {
			System.out.println("Unos broja racuna ne moze biti negativan broj. Racun nije uspjesno kreiran!");
			return false;
		}

		for (int i = 0; i < racuni.size(); i++)
			if (racuni.get(i).brojRacuna == brojRacuna) {
				System.out.println("Unijeti broj racuna vec postoji. Racun nije uspjesno kreiran!");
				return false;
			}

		return true;

	}

	/**
	 * 
	 * @param brojRacuna
	 * @return null
	 *         <p>
	 *         Getter za Racun
	 *         </p>
	 */

	public static Racun getRacun(int brojRacuna) {

		int i = 0;

		for (i = 0; i < racuni.size(); i++)
			if (racuni.get(i).brojRacuna == brojRacuna)
				return racuni.get(i);

		return null;
	}

	/**
	 * 
	 * @return brojPosudenihKnjiga
	 *         <p>
	 *         Getter za brojPosudenihKnjiga
	 *         </p>
	 */
	public int getBrojPosudenihKnjiga() {
		return brojPosudenihKnjiga;
	}

	/**
	 * 
	 * @param brojPosudenihKnjiga
	 *                            <p>
	 *                            Setter za brojPosudenihKnjiga
	 *                            </p>
	 */

	public void setBrojPosudenihKnjiga(int brojPosudenihKnjiga) {
		this.brojPosudenihKnjiga = brojPosudenihKnjiga;
	}

	/**
	 * 
	 * @return imeMusterije
	 *         <p>
	 *         Getter za imeMusterije
	 *         </p>
	 */
	public String getImeMusterije() {
		return imeMusterije;
	}

	/**
	 * 
	 * @throws IOException
	 *                     <p>
	 *                     Metoda koja pohranjuje objekte racuna u file racuni.txt
	 *                     </p>
	 */

	public static void save() throws IOException {
		FileOutputStream in = new FileOutputStream("racuni.txt");
		ObjectOutputStream oin = new ObjectOutputStream(in);

		for (int i = 0; i < racuni.size(); i++)
			oin.writeObject(racuni.get(i));

		oin.close();
	}

}
