package com;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class DeleteIndexDemo {
	public static void main(String[] args) throws IOException {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
	    Directory directory=FSDirectory.open(new File("/opt/example/indexes/"));
	    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
	    IndexReader indexReader = IndexReader.open(directory);
	    //indexReader.deleteDocuments(new Term("description", "java"));
	    //close associate index files and save deletions to disk
	    System.out.println(" Success Delete document");
	    indexReader.close();
		directory.close();
	}

}
