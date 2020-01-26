package osm.surveyor.matchtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@SuppressWarnings("serial")
public class AppParameters extends Properties {
    static final String FILE_PATH = "AdjustTime.ini";

    // GPX: ファイル更新時刻 yyyy:MM:dd HH:mm:ss
    public static String IMG_TIME = "IMG.TIME";

    // 対象IMGフォルダ:(位置情報を付加したい画像ファイルが格納されているフォルダ)
    public static String IMG_SOURCE_FOLDER = "IMG.SOURCE_FOLDER";

    // 基準時刻画像(正確な撮影時刻が判明できる画像)
    public static String IMG_BASE_FILE = "IMG.BASE_FILE";

    // 出力フォルダ:(変換した画像ファイルとGPXファイルを出力するフォルダ)
    public static String IMG_OUTPUT_FOLDER = "IMG.OUTPUT_FOLDER";

    // 出力OverwriteToSource: 入力ファイルに上書きする {ON | OFF}
    public static String OUTPUT_OVERWRITE_TO_SOURCE= "IMG.OUTPUT_OVERWRITE_TO_SOURCE";

    File file;

    public AppParameters() throws FileNotFoundException, IOException {
        super();
        this.file = new File(FILE_PATH);
        syncFile();
    }

    public AppParameters(Properties defaults) throws FileNotFoundException, IOException {
        super(defaults);
        this.file = new File(FILE_PATH);
        syncFile();
    }

    public AppParameters(String iniFileName) throws FileNotFoundException, IOException {
        super();
        this.file = new File(iniFileName);
        syncFile();
    }

    private void syncFile() throws FileNotFoundException, IOException {
        boolean update = false;

        if (this.file.exists()) {
            // ファイルが存在すれば、その内容をロードする。
            this.load(new FileInputStream(file));
        }
        else {
            update = true;
        }

        //------------------------------------------------
        // 対象フォルダ:(位置情報を付加したい画像ファイルが格納されているフォルダ)
        String valueStr = this.getProperty(IMG_SOURCE_FOLDER);
        if (valueStr == null) {
            update = true;
            this.setProperty(IMG_SOURCE_FOLDER, (new File(".")).getAbsolutePath());
        }

        //------------------------------------------------
        // 基準時刻画像(正確な撮影時刻が判明できる画像)
        valueStr = this.getProperty(IMG_BASE_FILE);
        if (valueStr == null) {
            update = true;
            this.setProperty(IMG_BASE_FILE, "");
        }

        //------------------------------------------------
        // 出力フォルダ:(変換した画像ファイルとGPXファイルを出力するフォルダ)
        valueStr = this.getProperty(IMG_OUTPUT_FOLDER);
        if (valueStr == null) {
            update = true;
            this.setProperty(IMG_OUTPUT_FOLDER, (new File(".")).getAbsolutePath());
        }

        //------------------------------------------------
        // 出力: 入力ファイルに上書きする {ON | OFF}
        valueStr = this.getProperty(OUTPUT_OVERWRITE_TO_SOURCE);
        if (valueStr == null) {
            update = true;
            this.setProperty(OUTPUT_OVERWRITE_TO_SOURCE, String.valueOf(false));
        }

        if (update) {
            // ・ファイルがなければ新たに作る
            // ・項目が足りない時は書き足す。
            //this.store(new FileOutputStream(this.file), "defuilt settings");
        }
    }

    public void store() throws FileNotFoundException, IOException {
        this.store(new FileOutputStream(this.file), "by Restamp");
    }
}
