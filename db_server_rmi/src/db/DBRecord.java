package db;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Base DB record object (one row)
 * 
 * @author pavel
 * 
 */
public class DBRecord implements Comparable<DBRecord> {

	/**
	 * Key for the record
	 */
	private int key;

	/**
	 * Record message
	 */
	private String message;

	/*
	 * Timestamps - '2011-10-02 14:34:57'
	 */
	private String tscreate;

	private String tsmodify;

	/**
	 * Default constructor, creates record without aby data
	 */
	public DBRecord() {
	}

	/**
	 * Creates db record with this properties
	 * 
	 * @param key
	 * @param message
	 * @param tscreate
	 * @param tsmodify
	 */
	public DBRecord(int key, String message, String tscreate, String tsmodify) {
		this.key = key;
		this.message = message;
		this.tscreate = tscreate;
		this.tsmodify = tsmodify;
	}

	/**
	 * This constructor is used for creaton of the new record, creates record
	 * with timestamps
	 * 
	 * @param key
	 * @param message
	 */
	public DBRecord(int key, String message) {
		this.key = key;
		this.message = message;
		// create timestamp
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = df.format(cal.getTime());

		// first record -> modify == create
		this.tscreate = this.tsmodify = dateString;
	}

	/**
	 * Create DBRecord from the csv
	 * 
	 * @param csvLine
	 *            - CSV string to insert
	 */
	public DBRecord(String csvLine) {
		String[] parsedLine = csvLine.split(";");
		String tscreate = parsedLine[0];
		String tsmodify = parsedLine[1];
		int key = Integer.parseInt(parsedLine[2]);
		String message = parsedLine[3];
		// create Record%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		this.key = key;
		this.message = message;
		this.tscreate = tscreate;
		this.tsmodify = tsmodify;
	}

	/**
	 * Returns record key
	 * 
	 * @return
	 */
	public int getKey() {
		return key;
	}

	/**
	 * Returns key message
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/*
	 * Set record message
	 */
	public void setMessage(String message) {
		this.message = message;
		this.updateModTS();
	}

	/**
	 * Get ts create
	 * 
	 * @return ts create string "yyyy-MM-dd HH:mm:ss"
	 */
	public String getTscreate() {
		return tscreate;
	}

	/**
	 * Returns last modify time
	 * 
	 * @return "yyyy-MM-dd HH:mm:ss" ts
	 */
	public String getTsmodify() {
		return tsmodify;
	}

	/**
	 * Change modify ts
	 */
	private void updateModTS() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = df.format(cal.getTime());
		// ////////////////////////////////////////
		this.tsmodify = dateString;
	}

	@Override
	public String toString() {
		return (this.key + ";" + this.message + ";" + this.tscreate + ";" + this.tsmodify);
	}

	/**
	 * Return CSV representation
	 * 
	 * @return CSV string
	 */
	public String getCSVRecord() {
		String retString = this.tscreate + ";" + this.tsmodify + ";" + this.key
				+ ";" + message;
		return retString;
	}

	/**
	 * Key comparator
	 */
	@Override
	public int compareTo(DBRecord o) {
		if (this.key > o.getKey()) {
			return 1;
		}

		if (this.key < o.getKey()) {
			return -1;
		}

		return 0;
	}

}
