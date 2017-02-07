package org.jaredstevens.servers.utilities;

public class Utilities {
	public static float parseFrequency(String inFreq) {
		float retVal = 0;
		if(inFreq != null && inFreq.length() > 0) {
			try {
				retVal = Float.parseFloat(inFreq);
			} catch(NumberFormatException e) {
				System.out.println("Failed to parse this frequency: ["+inFreq+"]");
				retVal = 0;
			}
		}
		else retVal = 0;
		return retVal;
	}
	
	public static float parseLatLong(String inCoord) {
		float retVal = 0;
		if(inCoord != null && inCoord.length() > 0) {
			try {
				retVal = Float.parseFloat(inCoord);
			} catch(NumberFormatException e) {
				System.out.println("Failed to parse this coordinate: ["+inCoord+"]");
				retVal = 0;
			}
		}
		else retVal = 0;
		return retVal;
	}
}
