package com;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SortDoc {
	public static void show(){
		System.out.println("hi--------------------");
	}
	public static void main(String[] args) throws IOException, ParseException {
		testNumericSorting();
	}

	

//I'm testing sorting feature in lucene with no luck. I am new to it. I've tried using either TopFieldCollector or TopFieldDocs but no sorting seems to be applied. Below a test code. What's wrong with it?

private static void testNumericSorting() throws IOException, ParseException{
    // 1. index some data
    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
    Directory index = FSDirectory.open(new File(ConstantUtil.INDEX_LOC));;
    DirectoryReader ireader = DirectoryReader.open(index);

    // 2. query
    String querystr = "orange";
    Query q = new QueryParser(Version.LUCENE_CURRENT, "title", analyzer).parse(querystr);

    int hitsPerPage = 10;
    IndexSearcher searcher = new IndexSearcher(ireader);
    // Normal score, with no sorting
    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
    searcher.search(q, collector);
    ScoreDoc[] sortedHits1 = collector.topDocs().scoreDocs;
    
    System.out.println("Found " + sortedHits1.length + " hits.");
    for(int i=0;i<sortedHits1.length;++i) {
       int docId = sortedHits1[i].doc;
       float score = sortedHits1[i].score;
       Document d = searcher.doc(docId);
       System.out.println((i + 1) + ". " + d.get("title")+ " score:"+score +"And Number Value is "+d.get("num"));
    }
System.out.println("-----------------Sorted Result------------------------");
    // Score with TopFieldCollector
    Sort sort = new Sort(new SortField[] {SortField.FIELD_SCORE,new SortField("num", SortField.Type.INT) });  
    TopFieldCollector topField = TopFieldCollector.create(sort, hitsPerPage, true, true, true, false);
    searcher.search(q, topField);   
    ScoreDoc[] sortedHits = topField.topDocs().scoreDocs; 
    assert(1>topField.topDocs().totalHits); 
    // Score with TopFieldDocs
    // TopFieldDocs topFields =  searcher.search(q, null, hitsPerPage, sort);
    // ScoreDoc[] sortedHits = topFields.scoreDocs; 

    System.out.println("Found " + sortedHits.length + " hits.");
    for(int i=0;i<sortedHits.length;++i) {
    	Explanation explanation=searcher.explain(q,sortedHits[i].doc);//Givve the description of matching doc
    	System.out.println(explanation.toString());
       int docId = sortedHits[i].doc;
       float score = sortedHits[i].score;
       Document d = searcher.doc(docId);
       System.out.println((i + 1) + ". " + d.get("title")+ " score:"+score +"And Number Value is "+d.get("num"));
    }
    ireader.close();
    index.close();
    analyzer.close();
    }


}
