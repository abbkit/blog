package com.abbkit.project.blog.search;

import com.abbkit.kernel.async.AsyncTaskExecutor;
import com.abbkit.kernel.async.SimpleAsyncTaskExecutor;
import com.abbkit.kernel.util.StringUtils;
import com.abbkit.lemon.client.DefaultClient;
import com.abbkit.lemon.client.DefaultClients;
import com.abbkit.lemon.client.kv.insert.KVModelInserter;
import com.abbkit.project.blog.BlogCons;
import com.abbkit.project.blog.monitor.BlogTrackerService;
import com.abbkit.project.model.ResponseModel;
import com.abbkit.project.module.xsearch.Index;
import com.abbkit.project.module.xsearch.Search;
import com.abbkit.project.module.xsearch.file.FileDoc;
import com.abbkit.project.module.xsearch.file.IndexFiles;
import com.abbkit.project.module.xsearch.file.SearchFiles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * Created by J on 2020/3/22.
 */
@Slf4j
@RestController
@RequestMapping(BlogCons.PATH_PREFIX+"/blog-content")
public class SearchController {

    @Value("${abbkit.module.index.filePath}")
    private String indexFilePath;
    @Value("${abbkit.module.index.docsPath}")
    private String docsPath;
    private Search search;

    private Index index;

    private DefaultClient defaultClient= DefaultClients.option();

    private AsyncTaskExecutor asyncTaskExecutor=SimpleAsyncTaskExecutor.SIMPLE;
    @Autowired
    private BlogTrackerService blogTrackerService;

    @PostConstruct
    public void init(){
        search=new SearchFiles(indexFilePath);
        index=new IndexFiles(indexFilePath,docsPath,true);
    }

    @ResponseBody
    @GetMapping(path= "search")
    public synchronized ResponseModel search(@RequestParam("query") String query, HttpServletRequest request)throws Exception{
        String unique=request.getHeader(BlogCons.HEADER_BLOG_UNIQUE);
        blogTrackerService.track(unique,"/search:"+query);

        if(query.contains("/")
                ||query.contains("\\")
                ||query.contains("?")
                ||query.contains("*")
                ){
            return ResponseModel.newSuccess(Collections.EMPTY_LIST);
        }

        BlogKeyWord blogKeyWord=new BlogKeyWord();
        blogKeyWord.setSeq(query);
        blogKeyWord.setSwitchKeyword(true);

        asyncTaskExecutor.submit(()->{
            KVModelInserter modelInserter=new KVModelInserter(defaultClient,BlogKeyWord.class);
            modelInserter.row(blogKeyWord);
            try {
                modelInserter.insert();
                return true;
            }catch (Exception e){
                log.error(e.getMessage(),e);
                return false;
            }
        });

        if(StringUtils.isNullOrEmpty(query))
            return ResponseModel.newSuccess(Collections.EMPTY_LIST);

        List<FileDoc> docs=search.search("contents",query,100,true);
        docs.forEach(doc -> doc.setUri(doc.uri().replace("\\","/").replace(docsPath,"http://abbkit.com")));
        return ResponseModel.newSuccess(docs);

    }


    @ResponseBody
    @GetMapping(path = "index")
    public synchronized ResponseModel index()throws Exception{
        index.index();
        return ResponseModel.newSuccess(true);
    }

















}
