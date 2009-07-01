package org.wiztools.util.pomodorotimerui;

/**
 *
 * @author subhash
 */
public class TimeUtil {
    private TimeUtil(){}

    public static String getSecondsFormatted(final int seconds){
        // Print variables:
        String pHr = null, pMin = null, pSec = null;

        // All the dirty computations:
        int min = seconds / 60;
        int sec = seconds - (min * 60);
        pSec = sec<10? "0"+sec: String.valueOf(sec);

        if(min < 61){ // no hours part
            pMin = min<10? "0"+min: String.valueOf(min);
        }
        else{ // hours part available :( [more computation!]
            int hr = min / 60;
            int hrMin = min - (hr*60);
            pMin = hrMin<10? "0"+hrMin: String.valueOf(hrMin);
            pHr = String.valueOf(hr);
        }

        // Build the final String:
        StringBuilder sb = new StringBuilder();
        if(pHr != null){
            sb.append(pHr).append(":");
        }
        sb.append(pMin).append(":");
        sb.append(pSec);
        return sb.toString();
    }
}
