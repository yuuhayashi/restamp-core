package osm.surveyor.matchtime;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.apache.commons.codec.digest.DigestUtils;
import static org.junit.Assert.*;

/**
 *
 * @author yuu
 */
public class RestampTest {
    
    public RestampTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        dirPath = "./target/test-classes/data/images";
        outPath = "./target/test-classes/out";
        Path dir = Paths.get(outPath);
        if (Files.exists(dir) && Files.isDirectory(dir) && Files.isWritable(dir)) {
            try {
                Files.delete(dir);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @After
    public void tearDown() {
        try {
            Path dir = Paths.get(outPath);
            if (Files.exists(dir) && Files.isDirectory(dir) && Files.isWritable(dir)) {
                Files.list(dir).forEach(f -> delFile(f));
                Files.delete(dir);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void delFile(Path p) {
        try {
            Files.delete(p);
        }
        catch(IOException e) {
            e.printStackTrace();
        };
    }

    String dirPath;
    String outPath;

    @Test
    public void testMain() {
        String[] ans = {
            "2019-09-01 16:26:51 JST", 
            "2019-09-01 16:26:56 JST", 
            "2019-09-01 16:27:01 JST", 
            "2019-09-01 16:27:06 JST", 
            "2019-09-01 16:27:11 JST", 
            "2019-09-01 16:27:16 JST", 
            "2019-09-01 16:27:21 JST", 
            "2019-09-01 16:27:26 JST", 
            "2019-09-01 16:27:31 JST", 
        };
        
        try {
            String[] argv = new String[]{
                dirPath,
                "00001.jpg",
                "2019-09-01 16:26:51 JST",
                "00003.jpg",
                "2019-09-01 16:27:01 JST",
                outPath
            };
            HashMap<String, File> before = getMd5(Paths.get(dirPath));
            Restamp.main(argv);
            check(new File(outPath), ans);
            checkUnchanged(before, Paths.get(dirPath));
        }
        catch (Exception e) {
            fail();
        }
    }
    
    @Test
    public void testMain_2() {
        String[] ans = {
            "2019-09-02 16:26:53 JST",  // 0.0 sec
            "2019-09-02 16:26:56 JST",  // 3.0 sec
            "2019-09-02 16:26:58 JST",  // 2.0 sec
            "2019-09-02 16:27:01 JST",  // 3.0 sec
            "2019-09-02 16:27:03 JST",  // 2.0 sec
            "2019-09-02 16:27:06 JST",  // 3.0 sec
            "2019-09-02 16:27:08 JST",  // 2.0 sec
            "2019-09-02 16:27:11 JST",  // 3.0 sec
            "2019-09-02 16:27:13 JST",  // 2.0 sec
        };
        
        try {
            String[] argv = new String[]{
                dirPath,
                "00002.jpg",
                "2019-09-02 16:26:56 JST",
                "00004.jpg",
                "2019-09-02 16:27:01 JST"
            };
            Restamp.main(argv);
            check(new File(dirPath), ans);
        }
        catch (Exception e) {
            fail();
        }
    }
    
    @Test
    public void testMain_3() {
        String[] ans = {
            "2019-09-03 16:26:53 JST",  // 0.0
            "2019-09-03 16:26:55 JST",  // 2.0
            "2019-09-03 16:26:58 JST",  // 3.0
            "2019-09-03 16:27:00 JST",  // 2.0
            "2019-09-03 16:27:03 JST",  // 3.0
            "2019-09-03 16:27:05 JST",  // 2.0
            "2019-09-03 16:27:08 JST",  // 3.0
            "2019-09-03 16:27:10 JST",  // 2.0
            "2019-09-03 16:27:13 JST",  // 3.0
        };
        
        try {
            String[] argv = new String[]{
                dirPath,
                "00001.jpg",
                "2019-09-03 16:26:53 JST",
                "00003.jpg",
                "2019-09-03 16:26:58 JST"
            };
            Restamp.main(argv);
            check(new File(dirPath), ans);
        }
        catch (Exception e) {
            fail();
        }
    }
    
    @Test
    public void testMain_4() {
        String[] ans = {
            "2019-09-04 16:26:53 JST",  // 0.0
            "2019-09-04 16:26:55 JST",  // 2.0
            "2019-09-04 16:26:58 JST",  // 3.0
            "2019-09-04 16:27:00 JST",  // 2.0
            "2019-09-04 16:27:03 JST",  // 3.0
            "2019-09-04 16:27:05 JST",  // 2.0
            "2019-09-04 16:27:08 JST",  // 3.0
            "2019-09-04 16:27:10 JST",  // 2.0
            "2019-09-04 16:27:13 JST",  // 3.0
        };
        
        try {
            String[] argv = new String[]{
                dirPath,
                "00003.jpg",
                "2019-09-04 16:26:58 JST",
                "00005.jpg",
                "2019-09-04 16:27:03 JST"
            };
            Restamp.main(argv);
            check(new File(dirPath), ans);
        }
        catch (Exception e) {
            fail();
        }
    }
    
    @Test
    public void testMain_5() {
        String[] ans = {
            "2019-09-04 16:26:53 JST",  // 0.0
            "2019-09-04 16:26:55 JST",  // 2.0
            "2019-09-04 16:26:58 JST",  // 3.0
            "2019-09-04 16:27:00 JST",  // 2.0
            "2019-09-04 16:27:03 JST",  // 3.0
            "2019-09-04 16:27:05 JST",  // 2.0
            "2019-09-04 16:27:08 JST",  // 3.0
            "2019-09-04 16:27:10 JST",  // 2.0
            "2019-09-04 16:27:13 JST",  // 3.0
        };
        
        try {
            String[] argv = new String[]{
                dirPath,
                "00003.jpg",
                "2019-09-04 16:26:58 JST",
                "00005.jpg",
                "2019-09-04 16:27:03 JST",
                outPath
            };
            Restamp.main(argv);
            check(new File(outPath), ans);
        }
        catch (Exception e) {
            fail();
        }
    }
    
    void check(File imgDir, String[] ans) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

        File[] files = imgDir.listFiles();
        assertThat(files.length, is(ans.length));
        
        java.util.Arrays.sort(files, (File file1, File file2) -> file1.getName().compareTo(file2.getName()));
        
        int i = 0;
        for (File jpgFile : files) {
            long msec = jpgFile.lastModified();
            String lastModifiedStr = df.format(new Date(msec));
            assertThat(lastModifiedStr, is(ans[i]));
            i++;
        }
    }
    
    void checkUnchanged(HashMap<String,File> map, Path dir) {
        assertThat(Files.isDirectory(dir), is(true));
        try {
            File[] files = dir.toFile().listFiles();
            assertThat(files.length, is(map.size()));
            
            for (String key : map.keySet()) {
                File sfile = map.get(key);
                Path ofile = Paths.get(dir.toString(), sfile.getName());
                assertNotNull(ofile);
                
                FileInputStream fis = new FileInputStream(ofile.toFile());
                String md5 = DigestUtils.md5Hex(fis);
                assertThat(key, is(md5));
            }
        }
        catch (Exception e) {
            fail();
        }
    }
    
    /**
     * フォルダ内のファイルとファイルのMD5リストを作成
     * @param dir
     * @return 
     */
    HashMap<String,File> getMd5(Path dir) {
        HashMap<String, File> map = new HashMap<>();
        try {
            File[] files = dir.toFile().listFiles();
            for (File f : files) {
                FileInputStream fis = new FileInputStream(f);
                map.put(DigestUtils.md5Hex(fis), f);
            }
        }
        catch (Exception e) {
            fail();
        }
        return map;
    }

}
