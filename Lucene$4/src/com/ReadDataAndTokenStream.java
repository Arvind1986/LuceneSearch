package com;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;



public class ReadDataAndTokenStream {
	public static void main(String[] args) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
	    Analyzer myAnalyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
	    Directory directory=FSDirectory.open(new File(ConstantUtil.INDEX_LOC));
		DirectoryReader ireader = DirectoryReader.open(directory);
	    IndexSearcher isearcher = new IndexSearcher(ireader);
	    // Parse a simple query that searches for "text":
	    QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, "description", myAnalyzer);
	    Query query = parser.parse("language commit");
	   
	    ScoreDoc[] hits = isearcher.search(query, null,10).scoreDocs;
	    for (int i = 0; i < hits.length; i++) {
	        Document hitDoc = isearcher.doc(hits[i].doc);
	        TokenStream stream = myAnalyzer.tokenStream("null", new StringReader(hitDoc.get("description")));
	        CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
	        try {
		         stream.reset();
		         while (stream.incrementToken()) {
		           System.out.println(termAtt.toString());
		         }
		         stream.end();
		       } finally {
		         stream.close();
		       }
	        System.out.println(hitDoc.get("description"));
	      }
	}
}
