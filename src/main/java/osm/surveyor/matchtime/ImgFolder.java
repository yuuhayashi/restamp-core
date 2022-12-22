package osm.jp.gpx;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.xml.sax.SAXException;

public class ImgFolder extends ArrayList<ImgFile> {
    AppParameters params;
    Path imgDir;
	
	public ImgFolder(Path imgDir) {
		this.imgDir = imgDir;
        File[] files = imgDir.toFile().listFiles(new ImgFileFilter());
        Arrays.sort(files, new FileSort());
        for (File file : files) {
        	this.add(new ImgFile(file));
        }
	}
	
	public Path getImgDir() {
		return this.imgDir;
	}
	
    /**
     * ファイル名の順序に並び替えるためのソートクラス
     * 
     */
    static class FileSort implements Comparator<File> {
        @Override
        public int compare(File src, File target){
            int diff = src.getName().compareTo(target.getName());
            return diff;
        }
    }
}
