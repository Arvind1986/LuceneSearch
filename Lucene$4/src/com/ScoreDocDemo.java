package com;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class ScoreDocDemo {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
	    Directory directory=FSDirectory.open(new File("/opt/example/indexes/"));
		DirectoryReader ireader = DirectoryReader.open(directory);
	    IndexSearcher isearcher = new IndexSearcher(ireader);
	    QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, null, analyzer);
		int hitsPerPage=2;
		//ScoreDoc[] hits = collector.topDocs().scoreDocs;
		//Document doc = searcher.doc(hits[i].doc);
		Term query=new Term("name","java");
		Query termQuery = new TermQuery(query);	 
		
		TopDocs topDocs = isearcher.search(termQuery,20);	
		   System.out.println("Total hits "+topDocs.totalHits);
		   System.out.println(topDocs.getMaxScore());
		   // Get an array of references to matched documents
		   ScoreDoc[] scoreDosArray = topDocs.scoreDocs;	
		   for(ScoreDoc scoredoc: scoreDosArray){
		      //Retrieve the matched document and show relevant details
		      Document hitDoc = isearcher.doc(scoredoc.doc);
		    //  System.out.println("---------- Result Count "+i+"---------");
		      System.out.println("name ="+hitDoc.get("name"));
		      System.out.println("city ="+hitDoc.get("city"));
		      System.out.println("desc ="+hitDoc.get("description"));
		     // System.out.println("---------End "+i+" Result ----------");
		      System.out.println();
		      System.out.println();
		    }
		 }
	}


