package osm.surveyor.matchtime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
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
    boolean ret;
    HashMap<String, File> before;

    public Fixture(
        String[] args,
        String[] ans,
        Boolean ret
    ) {
        this.args = args;
        this.ans = ans;
        this.ret = ret;
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
    public void check(boolean ret) {
    	if (this.ret) {
    		assertTrue(ret);
    	}
    	else {
    		assertFalse(ret);
    	}
    	if (args.length > 5) {
        	String dirPath = args[0];
        	File imgDir = Paths.get(dirPath).toFile();
        	if (this.args.length >= 6) {
        		imgDir = Paths.get(args[5]).toFile();
        	}
        	
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");

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
    	// 0 : NG
		new Fixture(
			new String[]{},
	        new String[] {},
	        new Boolean(false)
		),

		// 1 : NG
		new Fixture(
			new String[]{
				"./target/test-classes/images"
            },
	        new String[] {
                "2019-09-01 16:26:51.000 JST", 
                "2019-09-01 16:26:56.000 JST", 
                "2019-09-01 16:27:01.000 JST", 
                "2019-09-01 16:27:06.000 JST", 
                "2019-09-01 16:27:11.000 JST", 
                "2019-09-01 16:27:16.000 JST", 
                "2019-09-01 16:27:21.000 JST", 
                "2019-09-01 16:27:26.000 JST", 
                "2019-09-01 16:27:31.000 JST"
	        },
	        new Boolean(false)
		),
		
		// 2 : OK
		new Fixture(
			new String[]{
				"./target/test-classes/images",
                "00001.jpg",
                "2019-09-02 16:26:51 JST",
                "00003.jpg",
                "2019-09-02 16:27:01 JST",
                "./target/test-classes/out"
            },
	        new String[] {
                "2019-09-02 16:26:51.000 JST", 
                "2019-09-02 16:26:56.000 JST", 
                "2019-09-02 16:27:01.000 JST", 
                "2019-09-02 16:27:06.000 JST", 
                "2019-09-02 16:27:11.000 JST", 
                "2019-09-02 16:27:16.000 JST", 
                "2019-09-02 16:27:21.000 JST", 
                "2019-09-02 16:27:26.000 JST", 
                "2019-09-02 16:27:31.000 JST", 
	        },
	        new Boolean(true)
		),
		
		// 3 : OK
		new Fixture(
			new String[]{
				"./target/test-classes/images",
                "00002.jpg",
                "2019-09-03 16:26:56 JST",
                "00004.jpg",
                "2019-09-03 16:27:01 JST",
                "./target/test-classes/out"
            },
	        new String[] {
                "2019-09-03 16:26:53.500 JST",  //  -2.5 sec
                "2019-09-03 16:26:56.000 JST",  //   0.0 sec
                "2019-09-03 16:26:58.500 JST",  //   2.5 sec
                "2019-09-03 16:27:01.000 JST",  //   5.0 sec
                "2019-09-03 16:27:03.500 JST",  //   7.5 sec
                "2019-09-03 16:27:06.000 JST",  //  10.0 sec
                "2019-09-03 16:27:08.500 JST",  //  12.5 sec
                "2019-09-03 16:27:11.000 JST",  //  15.0 sec
                "2019-09-03 16:27:13.500 JST",  //  17.5 sec
	        },
	        new Boolean(true)
		),
		
		// 4 : OK
		new Fixture(
			new String[]{
				"./target/test-classes/images",
                "00001.jpg",
                "2019-09-04 16:26:53 JST",
                "00003.jpg",
                "2019-09-04 16:26:58 JST",
                "./target/test-classes/out"
            },
	        new String[] {
                "2019-09-04 16:26:53.000 JST",  //  0.0
                "2019-09-04 16:26:55.500 JST",  //  2.5
                "2019-09-04 16:26:58.000 JST",  //  5.0
                "2019-09-04 16:27:00.500 JST",  //  7.5
                "2019-09-04 16:27:03.000 JST",  // 10.0
                "2019-09-04 16:27:05.500 JST",  // 12.5
                "2019-09-04 16:27:08.000 JST",  // 15.0
                "2019-09-04 16:27:10.500 JST",  // 17.5
                "2019-09-04 16:27:13.000 JST",  // 20.0
	        },
	        new Boolean(true)
		),
		
		// 5 : OK
		new Fixture(
			new String[]{
				"./target/test-classes/images",
                "00003.jpg",
                "2019-09-05 16:26:58 JST",
                "00005.jpg",
                "2019-09-05 16:27:03 JST",
                "./target/test-classes/out"
            },
	        new String[] {
                "2019-09-05 16:26:53.000 JST",  // 0.0
                "2019-09-05 16:26:55.500 JST",  // 2.0
                "2019-09-05 16:26:58.000 JST",  // 0.0
                "2019-09-05 16:27:00.500 JST",  // 2.0
                "2019-09-05 16:27:03.000 JST",  // 3.0
                "2019-09-05 16:27:05.500 JST",  // 2.0
                "2019-09-05 16:27:08.000 JST",  // 3.0
                "2019-09-05 16:27:10.500 JST",  // 2.0
                "2019-09-05 16:27:13.000 JST",  // 3.0
	        },
	        new Boolean(true)
		),
		
		// 6 : OK
		new Fixture(
			new String[]{
				"./target/test-classes/images",
                "00003.jpg",
                "2019-09-06 16:26:58 JST",
                "IMG_0092.JPG",
                "2019-09-06 16:26:59 JST",
                "./target/test-classes/out"
            },
	        new String[] {
                "2019-09-06 16:26:57.334 JST",  // 0.0
                "2019-09-06 16:26:57.667 JST",  // 2.0
                "2019-09-06 16:26:58.000 JST",  // 0.0
                "2019-09-06 16:26:58.333 JST",  // 2.0
                "2019-09-06 16:26:58.666 JST",  // 3.0
                "2019-09-06 16:26:58.999 JST",  // 2.0
                "2019-09-06 16:26:59.332 JST",  // 3.0
                "2019-09-06 16:26:59.665 JST",  // 2.0
                "2019-09-06 16:26:59.998 JST",  // 3.0
	        },
	        new Boolean(true)
		),
		
		// 7 : OK
		new Fixture(
			new String[]{
				"./target/test-classes/images",
                "00003.jpg",
                "2019-09-07 16:26:58 JST",
                "00005.jpg",
                "2019-09-07 16:27:03 JST",
                "./target/test-classes/out"
            },
	        new String[] {
                "2019-09-07 16:26:53.000 JST",  // 0.0
                "2019-09-07 16:26:55.500 JST",  // 2.0
                "2019-09-07 16:26:58.000 JST",  // 3.0
                "2019-09-07 16:27:00.500 JST",  // 2.0
                "2019-09-07 16:27:03.000 JST",  // 3.0
                "2019-09-07 16:27:05.500 JST",  // 2.0
                "2019-09-07 16:27:08.000 JST",  // 3.0
                "2019-09-07 16:27:10.500 JST",  // 2.0
                "2019-09-07 16:27:13.000 JST",  // 3.0
	        },
	        new Boolean(true)
		),
    };

}
