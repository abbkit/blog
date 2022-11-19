package com.abbkit.project.blog.blogsearch;

import com.abbkit.kernel.async.AsyncTaskExecutor;
import com.abbkit.kernel.async.SimpleAsyncTaskExecutor;
import com.abbkit.kernel.model.ResponseModel;
import com.abbkit.kernel.util.StringUtils;
import com.abbkit.lemon.client.DefaultClient;
import com.abbkit.lemon.client.DefaultClients;
import com.abbkit.lemon.client.kv.insert.KVModelInserter;
import com.abbkit.module.xsearch.Search;
import com.abbkit.module.xsearch.file.FileDoc;
import com.abbkit.module.xsearch.file.SearchFiles;
import com.abbkit.project.blog.BlogCons;
import com.abbkit.project.blog.NoTrack;
import com.abbkit.project.blog.collect.TrackService;
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
@Autowired
    private IndexService indexService;

    private DefaultClient defaultClient= DefaultClients.option();

    private AsyncTaskExecutor asyncTaskExecutor=SimpleAsyncTaskExecutor.SIMPLE;
@Autowired
    private TrackService trackService;
@PostConstruct
    public void init(){
    search=new SearchFiles(indexFilePath);
    }

    @ResponseBody
    @NoTrack
    @GetMapping(path= "search")
    public synchronized ResponseModel search(@RequestParam("query") String query, HttpServletRequest request)throws Exception{

        String unique=request.getHeader(BlogCons.HEADER_BLOG_UNIQUE);

        trackService.track(unique,"/search:"+query);

        if(query.contains("/")
                ||query.contains("\\")
                ||query.contains("?")
                ||query.contains("*")
                ){
            return ResponseModel.newSuccess(Collections.EMPTY_LIST);
        }

        BlogKeyWord blogKeyWord=new BlogKeyWord();
        blogKeyWord.setLemonRowKey(query);
        blogKeyWord.setSwitchKeyword(true);

        asyncTaskExecutor.submit(()->{
            KVModelInserter modelInserter=new KVModelInserter(defaultClient,BlogKeyWord.class);
            modelInserter.row(blogKeyWord);
            modelInserter.insert();
            return true;
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
        indexService.index();
        return ResponseModel.newSuccess(true);
    }

















}
