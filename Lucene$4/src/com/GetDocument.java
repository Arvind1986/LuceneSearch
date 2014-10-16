package com;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class GetDocument {
	public static TopDocs readIndexes(Query query) throws IOException{
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
	    Directory directory=FSDirectory.open(new File("/opt/example/indexes/"));
		DirectoryReader ireader = DirectoryReader.open(directory);
	    IndexSearcher isearcher = new IndexSearcher(ireader);
	    QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, null, analyzer);
		return isearcher.search(query,20);
	    
	}

}
