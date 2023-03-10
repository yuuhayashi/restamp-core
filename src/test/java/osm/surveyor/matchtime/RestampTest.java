package osm.surveyor.matchtime;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
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
            // カメラディレクトリを作成する
    		Path tgz = Paths.get("target/test-classes/images.tar.gz");
    		if (Files.exists(tgz)) {
                UnZip.uncompress(Paths.get("target/test-classes/images.tar.gz"), Paths.get("target/test-classes/"));

                // OUTディレクトリを作成する
                Files.createDirectories(Paths.get("target/test-classes/out"));

                dataset.setUp();
                boolean ret = Restamp.main(dataset.args);
                dataset.check(ret);
                dataset.checkUnchanged();
                
                // IMGディレクトリを削除する
                UnZip.delete(Paths.get("target/test-classes/images"));
                
                // OUTディレクトリを削除する
                UnZip.delete(Paths.get("target/test-classes/out"));
    		}
    	}
    	catch(Exception e) {
            fail(e.toString());
    	}
    }
    
}
