package osm.surveyor.matchtime;

import java.io.File;
import java.io.FilenameFilter;

/**
 * JPEGファイルフィルター
 * @author yuu
 */
public class ImgFileFilter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
        return name.toUpperCase().matches(".*\\.JPG$");
	}

}
