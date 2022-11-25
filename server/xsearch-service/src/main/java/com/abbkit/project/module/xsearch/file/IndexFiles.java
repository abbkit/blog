package com.abbkit.project.module.xsearch.file;

import com.abbkit.kernel.util.ShutdownHookManager;
import com.abbkit.project.module.xsearch.Index;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by J on 2020/3/22.
 */
@Slf4j
public class IndexFiles implements Index {

    private final String indexPath;

    private final String docsPath;

    private final boolean update;

    private Analyzer analyzer;

    private List<FileIndex> fileIndices = new ArrayList<>();
    {
        fileIndices.add(new HTMLFileIndex());
    }

    public void addFileIndex(FileIndex fileIndex) {
        fileIndices.add(fileIndex);
    }

    public IndexFiles(String indexPath, String docsPath, boolean update) {
        this(indexPath, docsPath, update, null);
    }

    public IndexFiles(String indexPath, String docsPath, boolean update, Analyzer analyzer) {
        this.indexPath = indexPath;
        this.docsPath = docsPath;
        this.update = update;
        this.analyzer = analyzer == null ? new IKAnalyzer(true) : analyzer;
    }

    protected boolean filter(Path file, BasicFileAttributes attrs) {
        return true;
    }

    /**
     * Index all text files under a directory.
     */
    public void index() {
        boolean create = !update;

        final Path docDir = Paths.get(docsPath);
        if (!Files.isReadable(docDir)) {
            log.error("Document directory '" + docDir.toAbsolutePath() + "' does not exist or is not readable, please check the path");
            log.error("jvm exit:-1");
            ShutdownHookManager.get().exit(-1);
        }

        Date start = new Date();
        try {
            log.info("Indexing to directory '" + indexPath + "'...");

            Directory dir = FSDirectory.open(Paths.get(indexPath));
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

            if (create) {
                // Create a new index in the directory, removing any
                // previously indexed documents:
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            } else {
                // Add new documents to an existing index:
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            }

            // Optional: for better indexing performance, if you
            // are indexing many documents, increase the RAM
            // buffer.  But if you do this, increase the max heap
            // size to the JVM (eg add -Xmx512m or -Xmx1g):
            //
            // iwc.setRAMBufferSizeMB(256.0);

            IndexWriter writer = new IndexWriter(dir, iwc);
            indexDocs(writer, docDir);

            // NOTE: if you want to maximize search performance,
            // you can optionally call forceMerge here.  This can be
            // a terribly costly consumer, so generally it's only
            // worth it when your index is relatively static (ie
            // you're done adding documents to it):
            //
            // writer.forceMerge(1);

            writer.close();

            Date end = new Date();
            log.info(end.getTime() - start.getTime() + " total milliseconds");

        } catch (Exception e) {
            log.error(" caught a " + e.getClass() +
                    "\n with message: " + e.getMessage(), e);
        }
    }

    /**
     * Indexes the given file using the given writer, or if a directory is given,
     * recurses over files and directories found under the given directory.
     * <p>
     * NOTE: This method indexes one document per input file.  This is slow.  For good
     * throughput, put multiple documents into your input file(s).  An example of this is
     * in the benchmark module, which can create "line doc" files, one document per line,
     * using the
     * <a href="../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
     * >WriteLineDocTask</a>.
     *
     * @param writer Writer to the index where the given file/dir info will be stored
     * @param path   The file to index, or the directory to recurse into to find files to index
     * @throws IOException If there is a low-level I/O error
     */
    void indexDocs(final IndexWriter writer, Path path) throws Exception {
        if (Files.isDirectory(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        indexDoc(writer, file, attrs);
                    } catch (Exception ignore) {
                        // don't index files that can't be read.
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            indexDoc(writer, path, Files.readAttributes(path, BasicFileAttributes.class));
        }
    }

    /**
     * Indexes a single document
     */
    void indexDoc(IndexWriter writer, Path file, BasicFileAttributes attrs) throws Exception {

        for (FileIndex fileIndex : fileIndices) {
            if (fileIndex.filter(file, attrs)) {
                Document document = fileIndex.document(file, attrs);
                if (document != null) {

                    if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
                        // New index, so we just add the document (no old document can be there):
                        log.info("adding " + file);
                        writer.addDocument(document);
                    } else {
                        // Existing index (an old copy of this document may have been indexed) so
                        // we use updateDocument instead to replace the old one matching the exact
                        // path, if present:
                        log.info("updating " + file);
                        writer.updateDocument(new Term("path", file.toString()), document);
                    }
                }
                break;
            }
        }
    }

}
