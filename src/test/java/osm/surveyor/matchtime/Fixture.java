package osm.surveyor.matchtime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.experimental.theories.DataPoints;

public class Fixture {
    String[] args;
    String[] ans;
    HashMap<String, File> before;

    public Fixture(
        String[] args,
        String[] ans
    ) {
        this.args = args;
        this.ans = ans;
        this.before = new HashMap<>();
    }
    
    /**
     * フォルダ内のファイルとファイルのMD5リストを作成
     * @param dir
     * @return 
     */
    public void setUp() throws IOException {
    	if (args.length > 0) {
        	String dirPath = args[0];
            File[] files = Paths.get(dirPath).toFile().listFiles();
            for (File f : files) {
                FileInputStream fis = new FileInputStream(f);
                this.before.put(DigestUtils.md5Hex(fis), f);
            }
    	}
    }
    
    /**
     * 
     * @param imgDir
     * @param ans
     */
    public void check() {
    	if (args.length > 5) {
        	String dirPath = args[0];
        	File imgDir = Paths.get(dirPath).toFile();
        	if (this.args.length >= 6) {
        		imgDir = Paths.get(args[5]).toFile();
        	}
        	
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
    }
    
    /**
     * 
     * @param map
     * @param dir
     */
    public void checkUnchanged() {
    	if (this.args.length < 6) {
    		return;
    	}
    	String outStr = args[5];
    	Path dir = Paths.get(outStr);
        assertThat(Files.isDirectory(dir), is(true));
        try {
            File[] files = dir.toFile().listFiles();
            assertThat(files.length, is(this.before.size()));
            
            for (String key : this.before.keySet()) {
                File sfile = this.before.get(key);
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

    
    @Override
    public String toString() {
        String msg = "テストパターン";
    	for (String arg : args) {
    		msg += String.format("\n%s", arg);
    	}
        return msg;
    }
    

    @DataPoints
    public static Fixture[] datas = {
		new Fixture(
			new String[]{},
	        new String[] {}
		),
		new Fixture(
			new String[]{
				"./target/test-classes/images"
            },
	        new String[] {
                "2019-09-01 16:26:51 JST", 
                "2019-09-01 16:26:56 JST", 
                "2019-09-01 16:27:01 JST", 
                "2019-09-01 16:27:06 JST", 
                "2019-09-01 16:27:11 JST", 
                "2019-09-01 16:27:16 JST", 
                "2019-09-01 16:27:21 JST", 
                "2019-09-01 16:27:26 JST", 
                "2019-09-01 16:27:31 JST"
	        }
		),
		new Fixture(
			new String[]{
				"./target/test-classes/images",
                "00001.jpg",
                "2019-09-01 16:26:51 JST",
                "00003.jpg",
                "2019-09-01 16:27:01 JST",
                "./target/test-classes/out"
            },
	        new String[] {
                "2019-09-01 16:26:51 JST", 
                "2019-09-01 16:26:56 JST", 
                "2019-09-01 16:27:01 JST", 
                "2019-09-01 16:27:06 JST", 
                "2019-09-01 16:27:11 JST", 
                "2019-09-01 16:27:16 JST", 
                "2019-09-01 16:27:21 JST", 
                "2019-09-01 16:27:26 JST", 
                "2019-09-01 16:27:31 JST", 
	        }
		),
		new Fixture(
			new String[]{
				"./target/test-classes/images",
                "00002.jpg",
                "2019-09-02 16:26:56 JST",
                "00004.jpg",
                "2019-09-02 16:27:01 JST"
            },
	        new String[] {
                "2019-09-02 16:26:53 JST",  // 0.0 sec
                "2019-09-02 16:26:56 JST",  // 3.0 sec
                "2019-09-02 16:26:58 JST",  // 2.0 sec
                "2019-09-02 16:27:01 JST",  // 3.0 sec
                "2019-09-02 16:27:03 JST",  // 2.0 sec
                "2019-09-02 16:27:06 JST",  // 3.0 sec
                "2019-09-02 16:27:08 JST",  // 2.0 sec
                "2019-09-02 16:27:11 JST",  // 3.0 sec
                "2019-09-02 16:27:13 JST",  // 2.0 sec
	        }
		),
		new Fixture(
			new String[]{
				"./target/test-classes/images",
                "00001.jpg",
                "2019-09-03 16:26:53 JST",
                "00003.jpg",
                "2019-09-03 16:26:58 JST"
            },
	        new String[] {
                "2019-09-03 16:26:53 JST",  // 0.0
                "2019-09-03 16:26:55 JST",  // 2.0
                "2019-09-03 16:26:58 JST",  // 3.0
                "2019-09-03 16:27:00 JST",  // 2.0
                "2019-09-03 16:27:03 JST",  // 3.0
                "2019-09-03 16:27:05 JST",  // 2.0
                "2019-09-03 16:27:08 JST",  // 3.0
                "2019-09-03 16:27:10 JST",  // 2.0
                "2019-09-03 16:27:13 JST",  // 3.0
	        }
		),
		
		// 4
		new Fixture(
			new String[]{
				"./target/test-classes/images",
                "00003.jpg",
                "2019-09-04 16:26:58 JST",
                "00005.jpg",
                "2019-09-04 16:27:03 JST"
            },
	        new String[] {
                "2019-09-04 16:26:53 JST",  // 0.0
                "2019-09-04 16:26:55 JST",  // 2.0
                "2019-09-04 16:26:58 JST",  // 3.0
                "2019-09-04 16:27:00 JST",  // 2.0
                "2019-09-04 16:27:03 JST",  // 3.0
                "2019-09-04 16:27:05 JST",  // 2.0
                "2019-09-04 16:27:08 JST",  // 3.0
                "2019-09-04 16:27:10 JST",  // 2.0
                "2019-09-04 16:27:13 JST",  // 3.0
	        }
		),
		
		// 5
		new Fixture(
			new String[]{
				"./target/test-classes/images",
                "00003.jpg",
                "2019-09-04 16:26:58 JST",
                "00005.jpg",
                "2019-09-04 16:27:03 JST",
                "./target/test-classes/out"
            },
	        new String[] {
                "2019-09-04 16:26:53 JST",  // 0.0
                "2019-09-04 16:26:55 JST",  // 2.0
                "2019-09-04 16:26:58 JST",  // 3.0
                "2019-09-04 16:27:00 JST",  // 2.0
                "2019-09-04 16:27:03 JST",  // 3.0
                "2019-09-04 16:27:05 JST",  // 2.0
                "2019-09-04 16:27:08 JST",  // 3.0
                "2019-09-04 16:27:10 JST",  // 2.0
                "2019-09-04 16:27:13 JST",  // 3.0
	        }
		),
    };

}
