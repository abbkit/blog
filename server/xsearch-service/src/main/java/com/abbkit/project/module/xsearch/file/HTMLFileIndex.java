package com.abbkit.project.module.xsearch.file;

import org.apache.lucene.document.*;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by J on 2020/3/22.
 */
public class HTMLFileIndex implements FileIndex {

    private HTMLContentExtract htmlContentExtract = new HTMLContentExtract();

    @Override
    public boolean filter(Path file, BasicFileAttributes attrs) {
        return file.toFile().getName().endsWith("html");
    }

    @Override
    public Document document(Path file, BasicFileAttributes attrs) throws Exception {

        try (InputStream stream = Files.newInputStream(file)) {

            long lastModified = attrs.lastModifiedTime().toMillis();

            // make a new, empty document
            Document doc = new Document();

            // Add the path of the file as a field named "path".  Use a
            // field that is indexed (i.e. searchable), but don't tokenize
            // the field into separate words and don't index term frequency
            // or positional information:
            Field pathField = new StringField("path", file.toString(), Field.Store.YES);
            doc.add(pathField);

            // Add the last modified date of the file a field named "modified".
            // Use a LongPoint that is indexed (i.e. efficiently filterable with
            // PointRangeQuery).  This indexes to milli-second resolution, which
            // is often too fine.  You could instead create a number based on
            // year/month/day/hour/minutes/seconds, down the resolution you require.
            // For example the long value 2011021714 would mean
            // February 17, 2011, 2-3 PM.
            doc.add(new LongPoint("modified", lastModified));

            // Add the contents of the file to a field named "contents".  Specify a Reader,
            // so that the text of the file is tokenized and indexed, but not stored.
            // Note that FileReader expects the file to be in UTF-8 encoding.
            // If that's not the case searching for special characters will fail.
            String content = htmlContentExtract.content(stream);
            doc.add(new TextField("contents", content, Field.Store.YES));

            return doc;
        }
    }
}
