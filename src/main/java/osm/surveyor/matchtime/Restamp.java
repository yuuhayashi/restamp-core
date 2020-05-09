package osm.surveyor.matchtime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 動画から一定間隔で切り出したIMAGEファイルの更新日時を書き換える
 * 
 * @author yuu
 */
public class Restamp extends Thread {
    static public final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss z";

    /**
     * 実行中に発生したExceptionを保持する場所
     */
    public Exception ex = null;
	
    /**
     * ログ設定プロパティファイルのファイル内容
     */
    protected static final String LOGGING_PROPERTIES_DATA
           = "handlers=java.util.logging.ConsoleHandler\n"
           + ".level=FINEST\n"
           + "java.util.logging.ConsoleHandler.level=INFO\n"
           + "java.util.logging.ConsoleHandler.formatter=osm.jp.gpx.YuuLogFormatter";

    /**
     * メイン
     * 動画から一定間隔で切り出したIMAGEのファイル更新日時を書き換える
     * 
     * ・画像ファイルの更新日付を書き換えます。(Exi情報は無視します)
     *    ※ 指定されたディレクトリ内のすべての'*.jpg'ファイルを処理の対象とします
     * ・画像は連番形式（名前順に並べられること）の名称となっていること
     * 
     * 1.予め、動画から画像を切り出す
     * ソースファイル（mp4ファイル）; 「-i 20160427_104154.mp4」
     * 出力先: 「-f image2 img/%06d.jpg」 imgフォルダに６桁の連番ファイルを差出力する
     * 切り出し開始秒数→ 「-ss 0」 （ファイルの０秒から切り出し開始）
     * 切り出し間隔； 「-r 30」 (１秒間隔=３０fps間隔)
     * ```
     *  $ cd /home/yuu/Desktop/OSM/20180325_横浜新道
     *  $ ffmpeg -ss 0  -i 20160427_104154.mp4 -f image2 -r 15 img/%06d.jpg
     *  ```
     * 
     *  2. ファイルの更新日付を書き換える
     *  ```
     *  $ cd /home/yuu/Desktop/workspace/AdjustTime/importPicture/dist
     *  $ java -cp .:AdjustTime2.jar osm.jp.gpx.Restamp /home/yuu/Desktop/OSM/20180325_横浜新道/img 000033.jpg 2018-03-25_12:20:32 003600.jpg  2018-03-25_13:20:09
     *  ```
     * 
     *  exp) $ java -jar Restamp.jar argv[0] argv[1] argv[2] argv[3] argv[4]
     *  exp) $ java -jar Restamp.jar argv[0] argv[1] argv[2] argv[3] argv[4] argv[5]
     * 
     * @param argv
     * argv[0] = 画像ファイルが格納されているディレクトリ		--> imgDir
     * argv[1] = 時刻補正の基準とする画像ファイル			--> baseFile1
     * argv[2] = 基準画像ファイルの精確な撮影日時 "yyyy-MM-dd HH:mm:ss z" --> baseTime1
     * argv[3] = 時刻補正の基準とする画像ファイル			--> baseFile2
     * argv[4] = 基準画像ファイルの精確な撮影日時 "yyyy-MM-dd HH:mm:ss z" --> baseTime2
     * argv[5] = (option)変換済み画像ファイルの出力フォルダ.省略した場合は元画像を直接上書きする --> outputDir
     * @throws ImageReadException 
     */
    public static void main(String[] argv) throws Exception
    {
        if (argv.length < 5) {
            System.out.println("java osm.surveyor.matchtime.Restamp <imgDir> <baseFile1> <timeStr1> <baseFile2> <timeStr2>");
            System.out.println("java osm.surveyor.matchtime.Restamp <imgDir> <baseFile1> <timeStr1> <baseFile2> <timeStr2> <output dir>");
            return;
        }
        
        Path imgDir = Paths.get(argv[0]);
        
        Path outDir = imgDir;
        if (argv.length >= 6) {
            outDir = Paths.get(argv[5]);
        }
        
        Path baseFile1 = Paths.get(imgDir.toString(), argv[1]);
        
        DateFormat df1 = new SimpleDateFormat(TIME_PATTERN);
    	Date baseTime1 = df1.parse(argv[2]);
    	Date baseTime2 = df1.parse(argv[4]);

        Path baseFile2 = Paths.get(imgDir.toString(), argv[3]);
        
        Restamp obj = new Restamp();
        if (obj.setUp(imgDir, baseFile1, baseTime1, baseFile2, baseTime2, outDir)) {
            obj.start();
            try {
                obj.join();
            } catch(InterruptedException end) {}
            if (obj.ex != null) {
                throw obj.ex;
            }
        }
    }
    
    Path imgDir;
    Path outDir;
    Date baseTime1;
    Date baseTime2;
    Path baseFile1;
    Path baseFile2;
    public static ResourceBundle i18n = ResourceBundle.getBundle("i18n");
	
    /**
     * パラメータの設定とチェック
     * @param imgDir
     * @param baseFile1
     * @param baseTime1
     * @param baseFile2
     * @param baseTime2
     * @param outDir
     * @return パラメータチェックに合格すればtrue
     * @throws Exception 
     */
    public boolean setUp(
            Path imgDir, 
            Path baseFile1, Date baseTime1,  
            Path baseFile2, Date baseTime2,
            Path outDir) throws Exception {
        this.imgDir = imgDir;
        this.outDir = outDir;
        this.baseTime1 = baseTime1;
        this.baseTime2 = baseTime2;
        this.baseFile1 = baseFile1;
        this.baseFile2 = baseFile2;
        
        // <imgDir:ソースフォルダ>のチェック
        if (!Files.exists(imgDir)) {
            // "[error] <imgDir>が存在しません。"
            System.out.println(i18n.getString("msg.200"));
            return false;
        }
        if (!Files.isDirectory(imgDir)) {
            // "[error] <imgDir>がフォルダじゃない"
            System.out.println(i18n.getString("msg.210"));
            return false;
        }
        
        // <outDir>のチェック
        if (Files.exists(outDir)) {
            if (!Files.isDirectory(outDir)) {
                // "[error] <出力先フォルダ>はフォルダじゃない"
                System.out.println(i18n.getString("msg.270"));
                return false;
            }
        }
        else {
            // "[error] <outDir>は存在しません。"
            try {
                Files.createDirectories(outDir);
            }
            catch (IOException e) {
                System.out.println(i18n.getString("msg.275"));
                return false;
            }
        }
        if (!Files.isWritable(outDir)) {
            // "[error] <出力先フォルダ>には書き込みできません"
            System.out.println(i18n.getString("msg.275"));
            return false;
        }
        
        // <baseFile1>のチェック
        if (!Files.exists(baseFile1)) {
            // "[error] <baseFile1>が存在しません。"
            System.out.println(i18n.getString("msg.220"));
            return false;
        }
        if (!Files.isRegularFile(baseFile1)) {
            // "[error] <baseFile1>がファイルじゃない"
            System.out.println(i18n.getString("msg.230"));
            return false;
        }

        // <baseFile2>のチェック
        if (!Files.exists(baseFile2)) {
            // "[error] <baseFile2>が存在しません。"
            System.out.println(i18n.getString("msg.240"));
            return false;
        }
        if (!Files.isRegularFile(baseFile2)) {
            // "[error] <baseFile2>がファイルじゃない"
            System.out.println(i18n.getString("msg.250"));
            return false;
        }
        
        return true;
    }
    
    @Override
    public void run() {
        int bCount1 = 0;
        int bCount2 = 0;
        boolean base1 = false;
        boolean base2 = false;
        ArrayList<Path> jpgFiles = new ArrayList<>();

        // 指定されたディレクトリ内のJPEGファイルすべてを対象とする
        try (Stream<Path> files = Files.list(Paths.get(imgDir.toString()))){
            List<Path> sortedList = files.sorted(Comparator.naturalOrder()).collect(Collectors.toList());

            for (Path p : sortedList) {
                if (Files.exists(p) && Files.isRegularFile(p)) {
                    String filename = p.getFileName().toString();
                    if (filename.toUpperCase().endsWith(".JPG")) {
                        jpgFiles.add(p);
                        bCount1 += (base1 ? 0 : 1);
                        bCount2 += (base2 ? 0 : 1);
                        if (p.getFileName().equals(baseFile1.getFileName())) {
                            base1 = true;
                        }
                        if (p.getFileName().equals(baseFile2.getFileName())) {
                            base2 = true;
                        }
                    }
                }
            }
            if (!jpgFiles.isEmpty()) {
                DateFormat df2 = new SimpleDateFormat(TIME_PATTERN);

                // imgDir内の画像ファイルを処理する
                long span = this.baseTime2.getTime() - this.baseTime1.getTime();
                span = span / (bCount2 - bCount1);
                int i = 0;
                System.out.println("-------------------------------");
                System.out.println("Update last modified date time.");
                for (Path jpgFile : jpgFiles) {
                    long deltaMsec = (i - (bCount1 -1)) * span;
                    i++;
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(this.baseTime1);
                    cal.add(Calendar.MILLISECOND, (int) deltaMsec);
                    System.out.println(String.format("\t%s --> %s", df2.format(cal.getTime()), jpgFile.getFileName()));

                    //------------------------------------------
                    // ファイルをコピーして更新日時を変更する
                    //------------------------------------------
                    try {
                        Path outFile;
                        if (outDir.equals(imgDir)) {
                            outFile = jpgFile;
                        }
                        else {
                            outFile = Paths.get(outDir.toString(), jpgFile.toFile().getName());
                            Files.copy(jpgFile, outFile);
                        }
                        outFile.toFile().setLastModified(cal.getTimeInMillis());
                    }
                    catch (Exception e) {
                        System.out.println("[ERROR] Can not convert."+ e.toString());
                    }
                }
                System.out.println("-------------------------------");
            }
        }
        catch(IOException e) {
            e.printStackTrace();
            this.ex = new Exception(e);
        }
    }
}