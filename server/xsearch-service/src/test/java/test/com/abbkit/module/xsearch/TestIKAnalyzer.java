package test.com.abbkit.module.xsearch;

import com.abbkit.project.module.xsearch.file.HTMLContentExtract;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.FileInputStream;

/**
 * Created by J on 2020/3/22.
 */
public class TestIKAnalyzer {


    @Test
    public void ik() throws Exception{

        // 创建一个第三方中文分析器对象
        Analyzer analyzer = new IKAnalyzer();
        // 获得tokenStream对象
        // 第一个参数：域名，可以随便给一个
        // 第二个参数：要分析的文本内容
        TokenStream tokenStream = analyzer.tokenStream("test",
                "安拉，高富帅可以用二维表结构来逻辑表达实现的数据");
        // 添加一个引用，可以获得每个关键词
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        // 添加一个偏移量的引用，记录了关键词的开始位置以及结束位置
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        // 将指针调整到列表的头部
        tokenStream.reset();
        // 遍历关键词列表，通过incrementToken方法判断列表是否结束
        while(tokenStream.incrementToken()) {
            // 关键词的起始位置
            System.out.println("start->" + offsetAttribute.startOffset());
            // 取关键词
            System.out.println(charTermAttribute);
            // 结束位置
            System.out.println("end->" + offsetAttribute.endOffset());
        }
        tokenStream.close();

    }


    @Test
    public void ik2() throws Exception{

        // 创建一个第三方中文分析器对象
        Analyzer analyzer = new IKAnalyzer();
        // 获得tokenStream对象
        // 第一个参数：域名，可以随便给一个
        // 第二个参数：要分析的文本内容
        TokenStream tokenStream = analyzer.tokenStream("test",
                new HTMLContentExtract().content(new FileInputStream("c:/java_/git/blog/resume-online.html")));
        // 添加一个引用，可以获得每个关键词
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        // 添加一个偏移量的引用，记录了关键词的开始位置以及结束位置
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        // 将指针调整到列表的头部
        tokenStream.reset();
        // 遍历关键词列表，通过incrementToken方法判断列表是否结束
        while(tokenStream.incrementToken()) {
            // 关键词的起始位置
            System.out.println("start->" + offsetAttribute.startOffset());
            // 取关键词
            System.out.println(charTermAttribute);
            // 结束位置
            System.out.println("end->" + offsetAttribute.endOffset());
        }
        tokenStream.close();

    }
}
