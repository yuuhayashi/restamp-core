package osm.surveyor.matchtime;

import java.io.File;
import java.util.Date;

@SuppressWarnings("serial")
public class ImgFile extends File {
	Date imgtime = null;
	Date gpstime = null;

    public ImgFile(File file) {
        super(file.getParentFile(), file.getName());
    }
    
    /*
    public void setEnable(boolean enable) {
    	this.enable = enable;
    }
    
    public boolean isEnable() {
    	return this.enable;
    }
    */
    
    /**
     * 対象は '*.JPG' のみ対象とする
     * @return 
     */
    public boolean isImageFile() {
    	String name = this.getName();
        return ((name != null) && name.toUpperCase().endsWith(".JPG"));
    }
}
