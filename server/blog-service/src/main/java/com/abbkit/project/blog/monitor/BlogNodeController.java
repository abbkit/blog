package com.abbkit.project.blog.monitor;

import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.abbkit.kernel.util.CollectionUtils;
import com.abbkit.kernel.util.StringUtils;
import com.abbkit.lemon.client.DefaultClient;
import com.abbkit.lemon.client.DefaultClients;
import com.abbkit.lemon.client.kv.insert.KVModelInserter;
import com.abbkit.lemon.client.kv.select.KVModelSelector;
import com.abbkit.lemon.data.query.AndModel;
import com.abbkit.lemon.data.query.AndModelBuilder;
import com.abbkit.project.blog.BlogCons;
import com.abbkit.project.model.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 记录客户端信息
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping(path = "node")
    public ResponseModel node(HttpServletRequest request) throws Exception {

        String unique = request.getHeader(BlogCons.HEADER_BLOG_UNIQUE);

        if (StringUtils.isNullOrEmpty(unique)) return ResponseModel.newSuccess(true);
        //检查客户端是否已经记录
        AndModelBuilder directAndModel = new AndModelBuilder();
        directAndModel.eq("nodeSign",unique);
        AndModel andModel = directAndModel.build();
        KVModelSelector<BlogNode> kvModelSelector=new KVModelSelector<>(defaultClient, BlogNode.class);
        List<BlogNode> blogAccessList = kvModelSelector.select(andModel);
        if(!CollectionUtils.isEmpty(blogAccessList)){
            return ResponseModel.newSuccess("客户端已记录");
        }

        BlogNode blogNode = new BlogNode();
        blogNode.setNodeSign(unique);
        blogNode.setSeq(Long.MAX_VALUE-System.currentTimeMillis());
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


    /**
     * 客户端获取服务端分配的节点签名
     * @param request
     * @param fingerprint  客户端算出来的唯一码
     * @return
     * @throws Exception
     */
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

    /**
     * 记录客户端访问的文档URL地址
     * @param url
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping(path = "track")
    public ResponseModel track(@RequestParam("url") String url, HttpServletRequest request) throws Exception {
        String unique = request.getHeader(BlogCons.HEADER_BLOG_UNIQUE);
        if (StringUtils.isNullOrEmpty(url)) return ResponseModel.newSuccess(true);
        blogTrackerService.track(unique, url);
        return ResponseModel.newSuccess(true);
    }


    /**
     * 获取客户端的网站访问记录
     * @param request
     * @param node
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping(path = "history")
    public ResponseModel history(HttpServletRequest request, @RequestParam("node") String node) throws Exception {

        AndModelBuilder directAndModel = new AndModelBuilder();
        AndModel andModel = directAndModel
                .startRow(node, true)
                .endRow(node + "|", false)
                .build();

        KVModelSelector<BlogAccess> kvModelSelector=new KVModelSelector<>(defaultClient, BlogAccess.class);
        List<BlogAccess> blogAccessList = kvModelSelector.select(andModel);
        return ResponseModel.newSuccess(blogAccessList);

    }


    /**
     * 获取最近访问客户端
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping(path = "latest")
    public ResponseModel latest(HttpServletRequest request) throws Exception {
        AndModelBuilder directAndModel = new AndModelBuilder();
        directAndModel.startRow(Long.MAX_VALUE-System.currentTimeMillis(),true);
        AndModel andModel = directAndModel.build();

        KVModelSelector<BlogNode> kvModelSelector=new KVModelSelector<>(defaultClient, BlogNode.class);
        List<BlogNode> blogAccessList = kvModelSelector.select(andModel);
        return ResponseModel.newSuccess(blogAccessList);
    }

}

























