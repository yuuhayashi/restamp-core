package osm.surveyor.matchtime;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import haya4.tools.files.compless.UnZip;

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
        // カメラディレクトリを作成する
        UnZip.uncompress(Paths.get("target/test-classes/images.tar.gz"), Paths.get("target/test-classes/"));

        // OUTディレクトリを作成する
        Files.createDirectories(Paths.get("target/test-classes/out"));
    }
    
    static void tearDown() throws IOException {
        // IMGディレクトリを削除する
        UnZip.delete(Paths.get("target/test-classes/images"));
        
        // OUTディレクトリを削除する
        UnZip.delete(Paths.get("target/test-classes/out"));
    }
    
    
}
