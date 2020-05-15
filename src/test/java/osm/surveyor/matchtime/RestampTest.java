package osm.surveyor.matchtime;

import static org.junit.Assert.*;

import java.io.*;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class RestampTest {
    
    String dirPath;
    String outPath;

    @DataPoints
    public static Fixture[] datas = Fixture.datas;

    @Theory
    public void パラメータテスト(Fixture dataset) {
    	try {
    		System.out.println(dataset.toString());
            RestampTest.setUp();
            dataset.setUp();
            Restamp.main(dataset.args);
            dataset.check();
            dataset.checkUnchanged();
            RestampTest.tearDown();
    	}
    	catch(Exception e) {
            e.printStackTrace();
            fail("Exceptionが発生した。");
    	}
    }
    
    static void setUp() throws IOException {
    	tearDown();
    	
        // カメラディレクトリを作成する
        UnZip.uncompress(new File("target/test-classes/data/images.tar.gz"), new File("target/test-classes/"));

        // OUTディレクトリを作成する
        File outDir = new File("target/test-classes/out");
        outDir.mkdir();
    }
    
    static void tearDown() throws IOException {
        // IMGディレクトリを削除する
        File dir = new File("target/test-classes/images");
        if (dir.exists()) {
            UnZip.delete(dir);
        }
        
        // OUTディレクトリを削除する
        File outDir = new File("target/test-classes/out");
        if (outDir.exists()) {
        	UnZip.delete(outDir);
        }
    }
    
    
}
