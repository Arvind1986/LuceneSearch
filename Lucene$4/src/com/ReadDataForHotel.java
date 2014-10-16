package com;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.tools.tar.TarEntry;

public class ReadDataForHotel {
	public static void main(String[] args) throws IOException, ParseException {
		 //showResult("description","programm*");
		 String[] fields = {"name", "city","description"};
		 MultiSelectQueryResult("Client AND language", fields);
		 
	}
    public static void MultiSelectQueryResult(String text,String...fld) throws IOException, ParseException{
    	Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
	    Directory directory=FSDirectory.open(new File(ConstantUtil.INDEX_LOC));
		DirectoryReader ireader = DirectoryReader.open(directory);
	    IndexSearcher isearcher = new IndexSearcher(ireader);
	    	MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_CURRENT, fld, analyzer);
	    	org.apache.lucene.search.Query queryTextoLibre = parser.parse(text);
	    	System.out.println("Query for hit in Lucene is ="+queryTextoLibre.toString());
	    	ScoreDoc[] hits1 = isearcher.search(queryTextoLibre, null, 1000).scoreDocs;
	for (int i = 0; i < hits1.length; i++) {
	      Document hitDoc = isearcher.doc(hits1[i].doc);
	      System.out.println("---------- Result Count "+i+"---------");
	      System.out.println("name ="+hitDoc.get("name"));
	      System.out.println("city ="+hitDoc.get("city"));
	      System.out.println("desc ="+hitDoc.get("description"));
	      System.out.println("---------End "+i+" Result ----------");
	      System.out.println();
	      System.out.println();
	    }
	if(hits1.length <1){
		System.out.println("No data Found");
	}
		ireader.close();
		directory.close();
 }
	public static void showResult(String fld ,String text) throws IOException, ParseException{
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
	    Directory directory=FSDirectory.open(new File("/opt/example/indexes/"));
		DirectoryReader ireader = DirectoryReader.open(directory);
	    IndexSearcher isearcher = new IndexSearcher(ireader);
	    QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, null, analyzer);
	    WildcardQuery wildcardQuery =new WildcardQuery(new Term(fld,text));
			ScoreDoc[] hits1 = isearcher.search(wildcardQuery, null, 1000).scoreDocs;
			for (int i = 0; i < hits1.length; i++) {
			      Document hitDoc = isearcher.doc(hits1[i].doc);
			      System.out.println("---------- Result Count "+i+"---------");
			      System.out.println("name ="+hitDoc.get("name"));
			      System.out.println("city ="+hitDoc.get("city"));
			      System.out.println("desc ="+hitDoc.get("description"));
			      System.out.println("---------End "+i+" Result ----------");
			      System.out.println();
			      System.out.println();
			    }
			if(hits1.length <1){
				System.out.println("No data Found");
			}
	    ireader.close();
	    directory.close();
	}
	
}
