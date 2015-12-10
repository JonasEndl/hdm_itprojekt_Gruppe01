package de.hdm.itprojekt.server.db;

import java.sql.*;
import java.util.Vector;

/**
 * Mapper-Klasse, die <code>Nachrichten</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verf�gung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gel�scht werden k�nnen. Das Mapping ist bidirektional. D.h., Objekte k�nnen
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * <p>
 * 
 * 
 * @author Thies
 */

public class NachrichtenMapper {

	/**
	 * Die Klasse NachrichtenMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal
	 * f�r s�mtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 */

	private static NachrichtenMapper nachrichtMapper = null;

	/**
	 * Gesch�tzter Konstruktor - verhindert die M�glichkeit, mit new neue
	 * Instanzen dieser Klasse zu erzeugen.
	 * 
	 */

	protected NachrichtenMapper() {
	}

	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>NachrichtenMapper.NachrichtMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie daf�r sorgt, dass nur eine
	 * einzige Instanz von <code>NachrichtenMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> NachrichtMapper sollte nicht mittels <code>new</code>
	 * instantiiert werden, sondern stets durch Aufruf dieser statischen
	 * Methode.
	 * 
	 * @return DAS <code>NachrichtenMapper</code>-Objekt.
	 * @see nachrichtenMapper
	 */

	public static NachrichtenMapper nachrichtMapper() {
		if (nachrichtMapper == null) {
			nachrichtMapper = new NachrichtenMapper();
		}
		return nachrichtMapper;
	}

	/**
	 * Suchen eines Nuters mit vorgegebener ID. Da diese eindeutig ist, wird
	 * genau ein Objekt zur�ckgegeben.
	 * 
	 * @param id
	 *            Prim�rschl�sselattribut (->DB)
	 * @return Nachricht-Objekt, das dem �bergebenen Schl�ssel entspricht, null
	 *         bei nicht vorhandenem DB-Tupel.
	 */

	public Nachricht findById(int id) {
		// DB-Verbindung holen
		Connection con = DBConnection.connection();

		try {
			// Leeres SQL-Statement (JDBC) anlegen
			Statement stmt = con.createStatement();

			// Statement ausf�llen und als Query an die DB schicken
			ResultSet rs = stmt.executeQuery(
					"SELECT id, vorName, nachnameName, eMail FROM nutzer " + "WHERE id=" + id + " ORDER BY nachName");

			/*
			 * Da id Prim�rschl�ssel ist, kann max. nur ein Tupel zur�ckgegeben
			 * werden. Pr�fe, ob ein Ergebnis vorliegt.
			 */
			if (rs.next()) {
				// Ergebnis-Tupel in Objekt umwandeln
				Nutzer na = new Nutzer();
				na.setId(rs.getInt("id"));
				na.setErstellungszeitpunkt(rs.getString("erstellungszeitpunkt"));
				na.setText(rs.getString("text"));

				return na;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	/**
	 * Auslesen aller Nachrichten.
	 * 
	 * @return Ein Vektor mit Nachrichten-Objekten, die s�mtliche Nachrichten
	 *         repr�sentieren. Bei evtl. Exceptions wird ein partiell gef�llter
	 *         oder ggf. auch leerer Vetor zur�ckgeliefert.
	 */

	public Vector<Nachricht> findAll() {
		Connection con = DBConnection.connection();
		// Ergebnisvektor vorbereiten
		Vector<Nachricht> result = new Vector<Nachricht>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM `nachricht` ORDER BY `id`");

			// F�r jeden Eintrag im Suchergebnis wird nun ein Nachricht-Objekt
			// erstellt.
			while (rs.next()) {
				Nachricht na = new Nachricht();
				na.setId(rs.getInt("id"));
				na.setErstellungszeitpunkt(rs.getString("erstellungszeitpunkt"));
				na.setText(rs.getString("text"));

				// Hinzuf�gen des neuen Objekts zum Ergebnisvektor
				result.addElement(na);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Ergebnisvektor zur�ckgeben
		return result;
	}

	/**
	 * Einf�gen eines <code>Nachricht</code>-Objekts in die Datenbank. Dabei
	 * wird auch der Prim�rschl�ssel des �bergebenen Objekts gepr�ft und ggf.
	 * berichtigt.
	 * 
	 * @param c
	 *            das zu speichernde Objekt
	 * @return das bereits �bergebene Objekt, jedoch mit ggf. korrigierter
	 *         <code>id</code>.
	 */

	public Nachricht insert(Nachricht na) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();
			/*
			 * Zun�chst schauen wir nach, welches der momentan h�chste
			 * Prim�rschl�sselwert ist.
			 */
			ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid " + "FROM Nachricht ");
			// Wenn wir etwas zur�ckerhalten, kann dies nur einzeilig sein
			if (rs.next()) {
				/*
				 * na erh�lt den bisher maximalen, nun um 1 inkrementierten
				 * Prim�rschl�ssel.
				 */
				na.setId(rs.getInt("maxid") + 1);

				stmt = con.createStatement();
				// Jetzt erst erfolgt die tats�chliche Einf�geoperation
				stmt.executeUpdate("INSERT INTO Nachricht (id, Erstellungszeitpunkt, text) " + "VALUES (" + na.getId()
						+ ", " + na.Erstellungszeitpunkt() + ", " + na.text() + ", " + ")");
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		/*
		 * R�ckgabe, der evtl. korrigierten Nachricht.
		 * 
		 * HINWEIS: Da in Java nur Referenzen auf Objekte und keine physischen
		 * Objekte �bergeben werden, w�re die Anpassung des Nachricht-Objekts
		 * auch ohne diese explizite R�ckgabe au�erhalb dieser Methode sichtbar.
		 * Die explizite R�ckgabe von n ist eher ein Stilmittel, um zu
		 * signalisieren, dass sich das Objekt evtl. im Laufe der Methode
		 * ver�ndert hat.
		 */

		return na;
	}

	/**
	 * Wiederholtes Schreiben eines Objekts in die Datenbank.
	 * 
	 * @param n
	 *            das Objekt, das in die DB geschrieben werden soll
	 * @return das als Parameter �bergebene Objekt
	 */

	public Nachricht update(Nachricht na) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			// stmt.executeUpdate("UPDATE Nachricht " + "SET text=\"" +
			// n.gettext() + "\" " + "WHERE id=" + n.getId());

			stmt.executeUpdate("UPDATE `nachricht` SET `text`='" + na.getvorname() + "',`Erstellungszeitpunkt`='"
					+ "' WHERE `id`= " + na.getId() + ";");

		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		// Um Analogie zu insert(Nachricht na) zu wahren, geben wir na zur�ck
		return na;
	}

	/**
	 * L�schen der Daten eines <code>Nachricht</code>-Objekts aus der Datenbank.
	 * 
	 * @param na
	 *            das aus der DB zu l�schende "Objekt"
	 */

	public void delete(Nachricht na) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("DELETE FROM nachricht " + "WHERE id=" + na.getId());

		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

}