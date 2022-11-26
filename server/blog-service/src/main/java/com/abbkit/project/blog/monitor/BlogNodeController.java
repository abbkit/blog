package com.abbkit.project.blog.monitor;

import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.abbkit.kernel.util.StringUtils;
import com.abbkit.lemon.client.DefaultClient;
import com.abbkit.lemon.client.DefaultClients;
import com.abbkit.lemon.client.kv.Row2ModelTransform;
import com.abbkit.lemon.client.kv.insert.KVModelInserter;
import com.abbkit.lemon.client.kv.query.DefaultKVQueryScanner;
import com.abbkit.lemon.client.kv.query.KVQueryScanner;
import com.abbkit.lemon.client.kv.query.Result;
import com.abbkit.lemon.data.ProtocolCons;
import com.abbkit.lemon.data.kv.query.KVQueryModel;
import com.abbkit.lemon.data.put.Row;
import com.abbkit.lemon.data.query.AndModel;
import com.abbkit.lemon.data.query.DirectAndModel;
import com.abbkit.project.blog.BlogCons;
import com.abbkit.project.model.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by J on 2020/3/21.
 */
@Slf4j
@RestController
@RequestMapping(BlogCons.PATH_PREFIX + "/access")
public class BlogNodeController {
    private DefaultClient defaultClient = DefaultClients.option();
    @Autowired
    private BlogTrackerService blogTrackerService;

    private Row2ModelTransform<BlogNode> blogNodeRow2ModelTransform = new Row2ModelTransform<>(BlogNode.class);

    private Row2ModelTransform<BlogAccess> blogAccessRow2ModelTransform = new Row2ModelTransform<>(BlogAccess.class);

    @ResponseBody
    @GetMapping(path = "node")
    public ResponseModel node(HttpServletRequest request) throws Exception {

        String unique = request.getHeader(BlogCons.HEADER_BLOG_UNIQUE);

        if (StringUtils.isNullOrEmpty(unique)) return ResponseModel.newSuccess(true);
        BlogNode blogNode = new BlogNode();
        blogNode.setSeq(Long.MAX_VALUE-System.currentTimeMillis());
        blogNode.setNodeSign(unique);
        String userAgentStr = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
        Browser browser = userAgent.getBrowser();
        blogNode.setBrowser(browser.toString());
        blogNode.setUserAgent(userAgentStr);
        String sourceIp = request.getHeader("X-Forward-For");
        if (StringUtils.isNullOrEmpty(sourceIp)) {
            sourceIp = request.getHeader("x-forward-for");
        }
        blogNode.setIp(sourceIp);
        blogNode.setAccessTime(System.currentTimeMillis());
        KVModelInserter modelInserter = new KVModelInserter(defaultClient, BlogNode.class);
        modelInserter.row(blogNode);
        modelInserter.insert();
        return ResponseModel.newSuccess(true);

    }


    @ResponseBody
    @GetMapping(path = "unique")
    public ResponseModel unique(HttpServletRequest request, @RequestParam("fingerprint") String fingerprint) throws Exception {

        String unique = fingerprint;

        String sourceIp = request.getHeader("X-Forward-For");
        if (StringUtils.isNullOrEmpty(sourceIp)) {
            sourceIp = request.getHeader("x-forward-for");
        }
        if (StringUtils.isNotNullOrEmpty(sourceIp)) {
            unique = unique + "-" + sourceIp;
        }
        return ResponseModel.newSuccess(unique);

    }

    @ResponseBody
    @GetMapping(path = "track")
    public ResponseModel track(@RequestParam("url") String url, HttpServletRequest request) throws Exception {
        String unique = request.getHeader(BlogCons.HEADER_BLOG_UNIQUE);
        if (StringUtils.isNullOrEmpty(url)) return ResponseModel.newSuccess(true);
        blogTrackerService.track(unique, url);
        return ResponseModel.newSuccess(true);
    }


    @ResponseBody
    @GetMapping(path = "history")
    public ResponseModel history(HttpServletRequest request, @RequestParam("node") String node) throws Exception {

        DirectAndModel directAndModel = new DirectAndModel();
        AndModel andModel = directAndModel
                .startRow(node, true)
                .endRow(node + "|", false)
                .build();

        KVQueryModel kvQueryModel = new KVQueryModel();
        kvQueryModel.setCondition(andModel);
        kvQueryModel.setOpe(ProtocolCons.SELECT);
        kvQueryModel.setSchema("cold");
        kvQueryModel.setTable("blog_access");

        KVQueryScanner scanner = new DefaultKVQueryScanner(defaultClient);

        List<BlogAccess> blogAccesses = new ArrayList<>();

        Result result = scanner.scan(kvQueryModel);
        int count = 0;
        while (result.hasNext()) {
            if (count++ > 100) break;
            Row row = result.next();
            BlogAccess blogAccess = blogAccessRow2ModelTransform.transform(row);
            blogAccesses.add(blogAccess);
        }

        return ResponseModel.newSuccess(blogAccesses);

    }


    @ResponseBody
    @GetMapping(path = "latest")
    public ResponseModel latest(HttpServletRequest request) throws Exception {
        DirectAndModel directAndModel = new DirectAndModel();
        directAndModel.startRow(Long.MAX_VALUE-System.currentTimeMillis(),true);
        AndModel andModel = directAndModel.build();
        KVQueryModel kvQueryModel = new KVQueryModel();
        kvQueryModel.setCondition(andModel);
        kvQueryModel.setOpe(ProtocolCons.SELECT);
        kvQueryModel.setSchema("hot");
        kvQueryModel.setTable("blog_node");

        KVQueryScanner scanner = new DefaultKVQueryScanner(defaultClient);

        List<BlogNode> blogNodes = new ArrayList<>();

        Result result = scanner.scan(kvQueryModel);
        int count = 0;
        while (result.hasNext()) {
            if (count++ > 100) break;
            Row row = result.next();
            BlogNode blogNode = blogNodeRow2ModelTransform.transform(row);
            blogNodes.add(blogNode);
        }

        Collections.sort(blogNodes, new Comparator<BlogNode>() {
            @Override
            public int compare(BlogNode o1, BlogNode o2) {
                return Long.valueOf(o2.getAccessTime()).compareTo(o1.getAccessTime());
            }
        });
        int max = blogNodes.size() > 30 ? 30 : blogNodes.size();
        return ResponseModel.newSuccess(blogNodes.subList(0, max));
    }

}

























