/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.abbkit.module.xsearch.file;


import com.abbkit.module.xsearch.Search;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SearchFiles implements Search<FileDoc> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchFiles.class);

    private final String indexPath;

    private final Analyzer analyzer;

    public SearchFiles(String indexPath) {
        this(indexPath, null);
    }

    public SearchFiles(String indexPath, Analyzer analyzer) {
        this.indexPath = indexPath;
        this.analyzer = analyzer == null ? new IKAnalyzer(true) : analyzer;
    }

    @Override
    public List<FileDoc> search(String field, String queryString, int hitsPerPage) throws Exception {
        return search(field, queryString, hitsPerPage, false);
    }

    @Override
    public List<FileDoc> search(String field, String queryString, int hitsPerPage, boolean highlight) throws Exception {

        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher searcher = new IndexSearcher(reader);

        QueryParser parser = new QueryParser(field, analyzer);

        Query query = parser.parse(queryString);
        LOGGER.info("Searching for: " + query.toString(field));

        // Collect enough docs to show 5 pages
        TopDocs results = searcher.search(query, hitsPerPage);
        ScoreDoc[] hits = results.scoreDocs;

        int numTotalHits = Math.toIntExact(results.totalHits.value);
        LOGGER.info(numTotalHits + " total matching documents");

        List<FileDoc> docs = new ArrayList<>();
        for (int i = 0; i < hits.length; i++) {
            ScoreDoc scoreDoc = hits[i];

            FileDoc fileDoc = new FileDoc();
            fileDoc.setScore(scoreDoc.score);

            Document document = searcher.doc(scoreDoc.doc);
            String path = document.get("path");
            fileDoc.setUri(path);

            String content = document.get("contents");
            Formatter formatter;
            if (highlight) {
                formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
            } else {
                formatter = new NoOpFormatter();
            }
            Highlighter highlighter = new Highlighter(formatter, new QueryScorer(query));
            highlighter.setTextFragmenter(new SimpleFragmenter());
            if (content != null) {
                TokenStream tokenStream = analyzer.tokenStream(queryString, new StringReader(content));
                String highLightText = highlighter.getBestFragment(tokenStream, content);
                fileDoc.setContent(highLightText);
            }

            docs.add(fileDoc);
        }
        reader.close();
        return docs;
    }


    private class NoOpFormatter implements Formatter {
        @Override
        public String highlightTerm(String originalText, TokenGroup tokenGroup) {
            return originalText;
        }
    }
}
