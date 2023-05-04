import java.text.DecimalFormat;

public class Utilitys {
	protected String SafeGet(String[] T, int INDEX) {
		try {
			return T[INDEX];
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}
	protected int Clamp(int x, int min, int max) {
		return Math.max(min, Math.min(x, max)); // Reference from Lua, limit x by a range min and max.
	}
	protected String Comma(double number) {
	    DecimalFormat formatter = new DecimalFormat("#,###.##");
	    return formatter.format(number);
	}
	protected static String SecondToClock(int seconds) {
		if (seconds < 60) {
	        return seconds + "s";
	    } else if (seconds < 3600) {
	        int minutes = seconds / 60;
	        int remainingSeconds = seconds % 60;
	        if (remainingSeconds == 0) {
	            return minutes + "m";
	        } else {
	            return minutes + "m " + remainingSeconds + "s";
	        }
	    } else if (seconds == 3600) {
	        return (seconds / 3600) + "h";
	    } else {
	        return (seconds / 3600) + "h " + SecondToClock(seconds % 3600);
	    }
	}
}