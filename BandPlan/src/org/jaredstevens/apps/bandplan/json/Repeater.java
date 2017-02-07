package org.jaredstevens.apps.bandplan.json;

import java.io.Serializable;

public class Repeater implements Serializable {
	private static final long serialVersionUID = -2643583108587251245L;
	public enum Status {
		ACTIVE,
		INACTIVE
	}
	
	public enum Availability {
		OPEN,
		CLOSED
	}
	
    private long id;
    private Status status;
    private Availability availability;
    private String band;
    private float input;
    private float output;
    private float ctcssIn;
    private float ctcssOut;
    private String dcsCode;
    private String callsign;
    private String sponsor;
    private float latitude;
    private float longitude;
    private boolean closed;
    
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the resultStatus
	 */
	public Status getStatus() {
		return status;
	}
	/**
	 * @param resultStatus the resultStatus to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	/**
	 * @return the availability
	 */
	public Availability getAvailability() {
		return availability;
	}
	/**
	 * @param availability the availability to set
	 */
	public void setAvailability(Availability availability) {
		this.availability = availability;
	}
	/**
	 * @return the band
	 */
	public String getBand() {
		return band;
	}
	/**
	 * @param band the band to set
	 */
	public void setBand(String band) {
		this.band = band;
	}
	/**
	 * @return the input
	 */
	public float getInput() {
		return input;
	}
	/**
	 * @param input the input to set
	 */
	public void setInput(float input) {
		this.input = input;
	}
	/**
	 * @return the output
	 */
	public float getOutput() {
		return output;
	}
	/**
	 * @param output the output to set
	 */
	public void setOutput(float output) {
		this.output = output;
	}
	/**
	 * @return the ctcssIn
	 */
	public float getCtcssIn() {
		return ctcssIn;
	}
	/**
	 * @param ctcssIn the ctcssIn to set
	 */
	public void setCtcssIn(float ctcssIn) {
		this.ctcssIn = ctcssIn;
	}
	/**
	 * @return the ctcssOut
	 */
	public float getCtcssOut() {
		return ctcssOut;
	}
	/**
	 * @param ctcssOut the ctcssOut to set
	 */
	public void setCtcssOut(float ctcssOut) {
		this.ctcssOut = ctcssOut;
	}
	/**
	 * @return the dcsCode
	 */
	public String getDcsCode() {
		return dcsCode;
	}
	/**
	 * @param dcsCode the dcsCode to set
	 */
	public void setDcsCode(String dcsCode) {
		this.dcsCode = dcsCode;
	}
	/**
	 * @return the callsign
	 */
	public String getCallsign() {
		return callsign;
	}
	/**
	 * @param callsign the callsign to set
	 */
	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}
	/**
	 * @return the sponsor
	 */
	public String getSponsor() {
		return sponsor;
	}
	/**
	 * @param sponsor the sponsor to set
	 */
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	/**
	 * @return the latitude
	 */
	public float getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public float getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the closed
	 */
	public boolean isClosed() {
		return closed;
	}
	/**
	 * @param closed the closed to set
	 */
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
}
