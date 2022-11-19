package com.abbkit.project.blog.blogsearch;

import com.abbkit.module.xsearch.file.IndexFiles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by J on 2020/3/22.
 */
@Component
public class IndexService {
    @Value("${abbkit.module.index.filePath}")
    private String indexFilePath;
    @Value("${abbkit.module.index.docsPath}")
    private String docsPath;

    private IndexService(){}
    private static final IndexService INSTANCE=new IndexService();
    public static IndexService option(){
        return INSTANCE;
    }


    public void index(){

        IndexFiles indexFiles=new IndexFiles(indexFilePath,docsPath,true);
        indexFiles.index();

    }

}
