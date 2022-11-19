package com.abbkit.project.blog.collect;

import com.abbkit.kernel.async.AsyncTaskExecutor;
import com.abbkit.kernel.async.SimpleAsyncTaskExecutor;
import com.abbkit.kernel.util.StringUtils;
import com.abbkit.kernel.util.TimeSequenceUtil;
import com.abbkit.lemon.client.DefaultClient;
import com.abbkit.lemon.client.DefaultClients;
import com.abbkit.lemon.client.kv.insert.KVModelInserter;
import org.springframework.stereotype.Component;

/**
 * Created by J on 2020/3/25.
 */
@Component
public class TrackService {

    private AsyncTaskExecutor asyncTaskExecutor= SimpleAsyncTaskExecutor.SIMPLE;

    private DefaultClient defaultClient= DefaultClients.option();

    public void track(String unique,String track){

        if(StringUtils.isNullOrEmpty(unique))  unique="default";

        BlogAccess blogAccess=new BlogAccess();
        blogAccess.setLemonRowKey(unique+"-"+ TimeSequenceUtil.revertSequence());

        blogAccess.setAccessTime(System.currentTimeMillis());
        blogAccess.setTrack(track);

        asyncTaskExecutor.submit(() -> {
            KVModelInserter modelInserter=new KVModelInserter(defaultClient,BlogAccess.class);
            modelInserter.row(blogAccess);
            modelInserter.insert();
            return true;
        });


    }

}
