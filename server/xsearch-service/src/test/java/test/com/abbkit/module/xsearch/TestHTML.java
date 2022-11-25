package test.com.abbkit.module.xsearch;

import com.abbkit.project.module.xsearch.file.HTMLContentExtract;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;

/**
 * Created by J on 2020/3/22.
 */
public class TestHTML {



    @Test
    public void content()throws Exception{

        String content=new HTMLContentExtract().content(new FileInputStream("c:/java_/git/blog/index.html"));

        Assert.assertNotNull(content);


    }


}
