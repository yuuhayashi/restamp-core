package osm.surveyor.util;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Exif extends Thread {
    public static final String TIME_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String EXIF_DATE_TIME_FORMAT_STRING = "yyyy:MM:dd HH:mm:ss";
    
    /**
     * 対象は '*.JPG' のみ対象とする
     * @return 
     * @param name
     */
    public static boolean checkFile(String name) {
        return ((name != null) && name.toUpperCase().endsWith(".JPG"));
    }

    /**
     * DateをEXIFの文字列に変換する。
     * 注意：EXiFの撮影時刻はUTC時間ではない
     * @param localdate
     * @return
     */
    public static String toEXIFString(Date localdate) {
    	DateFormat dfUTC = new SimpleDateFormat(EXIF_DATE_TIME_FORMAT_STRING);
    	return dfUTC.format(localdate);
    }
    
    /**
     * EXIFの文字列をDateに変換する。
     * 注意：EXiFの撮影時刻はUTC時間ではない
     * @param timeStr
     * @return
     * @throws ParseException
     */
    public static Date toEXIFDate(String timeStr) throws ParseException {
    	DateFormat dfUTC = new SimpleDateFormat(EXIF_DATE_TIME_FORMAT_STRING);
    	//dfUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
    	return dfUTC.parse(timeStr);
    }
	
    public static String toUTCString(Date localdate) {
    	DateFormat dfUTC = new SimpleDateFormat(TIME_FORMAT_STRING);
    	dfUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
    	return dfUTC.format(localdate);
    }
	
    public static Date toUTCDate(String timeStr) throws ParseException {
    	DateFormat dfUTC = new SimpleDateFormat(TIME_FORMAT_STRING);
    	dfUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
    	return dfUTC.parse(timeStr);
    }
	
    static String getShortPathName(File dir, File iFile) {
        String dirPath = dir.getAbsolutePath();
        String filePath = iFile.getAbsolutePath();
        if (filePath.startsWith(dirPath)) {
            return filePath.substring(dirPath.length()+1);
        }
        else {
            return filePath;
        }
    }
    
    /**
     * JPEGファイルフィルター
     * @author yuu
     */
    class JpegFileFilter implements FilenameFilter {
    	@Override
        public boolean accept(File dir, String name) {
            return name.toUpperCase().matches(".*\\.JPG$");
    	}
    }
}