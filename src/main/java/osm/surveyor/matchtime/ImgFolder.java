package osm.surveyor.matchtime;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ImgFolder extends ArrayList<ImgFile> {
    private static final long serialVersionUID = 1L;
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
