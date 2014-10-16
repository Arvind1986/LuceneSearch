package com;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.CompressionTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.standard.parser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Searcher {
	public static void main(String[] args) throws ParseException, IOException, org.apache.lucene.queryparser.classic.ParseException {
		String indexDir = ConstantUtil.INDEX_LOC;
		String q = "fooaaa,baraaa,bazaaa";
		search(indexDir, q);
	}
	public static void search(String indexDir, String q)
			throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {
			Directory dir = FSDirectory.open(new File(indexDir));
			DirectoryReader ireader = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(ireader);
			QueryParser parser = new QueryParser(Version.LUCENE_30,	"contents",	new StandardAnalyzer(Version.LUCENE_CURRENT));
			
			Query query = parser.parse(q);
			long start = System.currentTimeMillis();
			System.out.println("Query >>"+query.toString());
			//CompressionTools.compress(value); comprace the byte arrary
			TopDocs hits = is.search(query, 10);
			long end = System.currentTimeMillis();
			System.err.println("Found " + hits.totalHits +
			" document(s) (in " + (end - start) +
			" milliseconds) that matched query '" +
			q + "':");
			for(ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = is.doc(scoreDoc.doc);
			System.out.println(doc.get("fullpath"));
			System.out.println(doc.get("age"));
			}
			
			}
}
