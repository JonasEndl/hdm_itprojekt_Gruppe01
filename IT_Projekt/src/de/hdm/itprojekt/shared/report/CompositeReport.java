package de.hdm.itprojekt.shared.report;

import java.io.Serializable;
import java.util.*;


/**
 * Ein zusammengesetzter Report. Kann aus einer Menge von Teil-Reports bestehen.
 */

public abstract class CompositeReport extends Report implements Serializable {


  private static final long serialVersionUID = 1L;
  
 /**
  * Menge der Teil-Reports.
  */

	private Vector<Report> subReports = new Vector<Report>();

/**
 * Hinzuf�gen eines Teil-Reports
 * 
 * @param r der hinzuf�gende Teil-Report.
 */

	public void addSubReport(Report r) {
		this.subReports.addElement(r);
	}
	
/**
 * Entfernen eines Teil-Reports.
 * 
 * @param r der zu entfernende Teil-Report.
 */

	public void removeSubReport(Report r) {
		this.subReports.removeElement(r);
	}

/**
 * Auslesen der Anzahl von Teil-Reports.
 * 
 * @return int Anzahl der Teil-Reports.
*/
	public int getNumSubReports() {
		return this.subReports.size();
	}
	
/**
 * Auslesen eines einzelnen Teil-Reports.
 * 
 * @param i Position des Teilreports. Bei n Elementen l�uft der Index i von 0 bis n-1.
 * 
 * @return Position des Teil-Reports.
 */
	
	public Report getSubReportAt(int i) {
		return this.subReports.elementAt(i);
	}
}