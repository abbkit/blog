package test.com.abbkit.module.xsearch;

import com.abbkit.module.xsearch.file.FileDoc;
import com.abbkit.module.xsearch.file.IndexFiles;
import com.abbkit.module.xsearch.file.SearchFiles;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by J on 2020/3/21.
 */
public class TestIndexFiles {

    private String index="d:/index";


    @Test
    public void indexFile()throws Exception{

        IndexFiles indexFiles=new IndexFiles(index,"c:/java_/git/blog",true);
        indexFiles.index();


    }


    @Test
    public void searchFile()throws Exception{

        List<FileDoc> docs=new SearchFiles(index).search("contents","Apple",10);
        Assert.assertTrue(docs.size()>0);

    }


}
