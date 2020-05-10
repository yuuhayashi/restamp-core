package osm.surveyor.matchtime;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.experimental.runners.*;

@RunWith(Enclosed.class)
public class AppParametersTest {
	static final String DEFUALT_FILE_NAME = "ReStamp.properties";

    public static class 定義ファイルが存在しない場合 {

        @Before
        public void setUp() throws Exception {
        	delTestData(DEFUALT_FILE_NAME);
        }

        @After
        public void tearDown() throws Exception {
        	delTestData(DEFUALT_FILE_NAME);
        	delTestData("ReStamp.nosource.properties");
        	delTestData("ReStamp.xx.properties");
        }


        @Test
        public void 引数なしで起動されたとき() {
        	// デフォルトプロパティファイルが適用されること
            try {
            	AppParameters params = new AppParameters();
                String valueStr = params.getProperty(AppParameters.IMG_SOURCE_FOLDER);
                assertNotNull(valueStr);
                File sourceDir = new File(valueStr);
                String sourcePath = sourceDir.getAbsolutePath();
                File cDir = new File(".");
                String cPath = cDir.getAbsolutePath();
                assertEquals(sourcePath, cPath);
            }
            catch (IOException e) {
                fail("Exceptionが発生した。");
            }
        }

        @Test
        public void IMG_SOURCE_FOLDERが定義されていない時() {
            try {
            	// デフォルトに カレントディレクトリが戻ること
            	AppParameters params = new AppParameters("target/test-classes/ReStamp.nosource.properties");
                String valueStr = params.getProperty(AppParameters.IMG_SOURCE_FOLDER);
                assertNotNull(valueStr);
                File sourceDir = new File(valueStr);
                String sourcePath = sourceDir.getAbsolutePath();
                File cDir = new File(".");
                String cPath = cDir.getAbsolutePath();
                assertEquals(sourcePath, cPath);
            }
            catch (IOException e) {
                fail("Exceptionが発生した。");
            }
        }

        @Test
        public void propertyファイルが存在しないとき() {
            try {
            	// デフォルトプロパティファイルが適用されること
            	AppParameters params = new AppParameters("target/test-classes/ReStamp.xx.properties");
                String valueStr = params.getProperty(AppParameters.IMG_SOURCE_FOLDER);
                assertNotNull(valueStr);
                File sourceDir = new File(valueStr);
                String sourcePath = sourceDir.getAbsolutePath();
                File cDir = new File(".");
                String cPath = cDir.getAbsolutePath();
                assertEquals(sourcePath, cPath);
            }
            catch (IOException e) {
                fail("Exceptionが発生した。");
            }
        }

        @Test
        public void propertyが指定されたとき() {
        	// デフォルトプロパティファイルが適用されること
            try {
            	Properties prop = new Properties();
            	AppParameters params = new AppParameters(prop);
                String valueStr = params.getProperty(AppParameters.IMG_SOURCE_FOLDER);
                assertNotNull(valueStr);
                File sourceDir = new File(valueStr);
                String sourcePath = sourceDir.getAbsolutePath();
                File cDir = new File(".");
                String cPath = cDir.getAbsolutePath();
                assertEquals(sourcePath, cPath);
            }
            catch (IOException e) {
                fail("Exceptionが発生した。");
            }
        }
    }

    public static class 空の定義ファイルが指定された場合 {

        @Before
        public void setUp() throws Exception {
        	delTestData(DEFUALT_FILE_NAME);
        	setupTestData("ReStamp.nosource.properties", "ReStamp.properties");
        }

        @After
        public void tearDown() throws Exception {
        	delTestData(DEFUALT_FILE_NAME);
        }

        @Test
        public void IMG_SOURCE_FOLDERが定義されていない時() {
            try {
            	// デフォルトに カレントディレクトリが戻ること
            	AppParameters params = new AppParameters("target/test-classes/ReStamp.properties");
                String valueStr = params.getProperty(AppParameters.IMG_SOURCE_FOLDER);
                assertNotNull(valueStr);
                File sourceDir = new File(valueStr);
                String sourcePath = sourceDir.getAbsolutePath();
                File cDir = new File(".");
                String cPath = cDir.getAbsolutePath();
                assertEquals(sourcePath, cPath);

            	// 書き換える
                params.setProperty(AppParameters.IMG_SOURCE_FOLDER, "xxx");
                params.store();
            }
            catch (IOException e) {
                fail("Exceptionが発生した。");
            }
            try {
            	// デフォルトに カレントディレクトリが戻ること
            	Properties prop = new Properties();
            	File f = new File("target/test-classes/ReStamp.properties");
                prop.load(new FileInputStream(f));
                
                String valueStr = prop.getProperty(AppParameters.IMG_SOURCE_FOLDER);
                assertNotNull(valueStr);
                assertEquals(valueStr, "xxx");
            }
            catch (IOException e) {
                fail("Exceptionが発生した。");
            }
        }
    }

    static void delTestData(String filename) {
    	File ddir = new File("target/test-classes/");
        File iniFile = new File(ddir, filename);
        if (iniFile.exists()) {
            iniFile.delete();
        }
    	ddir = new File(".");
        iniFile = new File(ddir, filename);
        if (iniFile.exists()) {
            iniFile.delete();
        }
    }
    
    static void setupTestData(String sfilename, String dfilename) throws IOException {
    	File ddir = new File("target/test-classes/");
    	File sdir = new File("target/test-classes/props/");
        File testFile = new File(sdir, sfilename);
        FileInputStream inStream = new FileInputStream(testFile);
        FileOutputStream outStream = new FileOutputStream(new File(ddir, dfilename));
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(),outChannel);
        }
        finally {
            if (inChannel != null) inChannel.close();
            if (outChannel != null) outChannel.close();
            inStream.close();
            outStream.close();
        }
    }
}
