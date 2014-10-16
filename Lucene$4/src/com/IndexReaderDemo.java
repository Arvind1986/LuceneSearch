package com;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class IndexReaderDemo {
	private static final String F_WORD   = "Suggestion";
	private static final String F_DOMAIN = "Domain";
	private static final String F_SCOPE  = "Scope";
	private static String parseQueryString(String term) {
		//Cleanup Query Term
		term = term.trim();
		term = term.replaceAll("[^a-zA-Z0-9&\\s,]", "");//& added 3410
		term = term.replaceAll("^[^a-zA-Z0-9]", "");
		term = term.replaceAll("\\s+", " ");
		term = term.replaceAll(", ", ",");
		term = term.replaceAll(" ,", ",");
		term = term.replaceAll(",+", ",");
		
		StringBuffer query = new StringBuffer("");
		String [] terms = term.split("[\\s,]");

		for(int i=0;i<terms.length -1;i++) {
			query.append(terms[i]).append(" AND "); 
		}
		query.append(terms[terms.length-1]).append("*");
		return query.toString();
	}
private static Query prepareQuery(String word,String domain,String scope) {
		
		BooleanQuery bq = new BooleanQuery();
		
		try {
			QueryParser qp = new QueryParser(null, IndexReaderDemo.F_WORD,new StandardAnalyzer(Version.LUCENE_CURRENT));
			Query q = qp.parse(word);
			bq.add(q, Occur.MUST);
			bq.add(new TermQuery(new Term(F_DOMAIN,domain )),Occur.MUST);
			//NONE keywords will exist if search is not easyboolean
			scope = scope.replaceAll(",", " OR ");
			bq.add(new TermQuery(new Term(F_SCOPE,scope)),Occur.SHOULD);
			
		}catch(ParseException pe) {
			pe.printStackTrace();
		}
		return bq;
	}

private static TopFieldDocs getSearchIndex(IndexSearcher searcher, String query, String domain,String scope, Sort s)
		throws IOException {
	boolean retry = true;
	/**setting the value to the default values**/
	System.setProperty("org.apache.lucene.maxClauseCount",Integer.toString(1024));
	BooleanQuery.setMaxClauseCount(1024);
	while (retry) {
		try {
			retry = false;				
			TopFieldDocs hits = searcher.search(prepareQuery(query, domain, scope), 100, s);
			return hits;
		} catch (BooleanQuery.TooManyClauses e) {
			// Double the number of boolean queries allowed.
			// The default is in org.apache.lucene.search.BooleanQuery and
			// is 1024.
			String defaultQueries = Integer.toString(BooleanQuery.getMaxClauseCount());
			int oldQueries = Integer.parseInt(System.getProperty("org.apache.lucene.maxClauseCount", defaultQueries));
			int newQueries = oldQueries * 2;
			System.setProperty("org.apache.lucene.maxClauseCount",Integer.toString(newQueries));
			BooleanQuery.setMaxClauseCount(newQueries);
			retry = true;
		}
	}
	return null;
}
public static void main(String[] args) throws IOException, ParseException {
	Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
    Directory directory=FSDirectory.open(new File(ConstantUtil.INDEX_LOC));
	DirectoryReader ireader = DirectoryReader.open(directory);
    IndexSearcher isearcher = new IndexSearcher(ireader);
    // Parse a simple query that searches for "text":
    QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, "subject", analyzer);
    Query query = parser.parse("jsp");
    ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
    int hit =hits.length;
    // assert(hit>1);
    // Iterate through the results:
    for (int i = 0; i < hits.length; i++) {
      Document hitDoc = isearcher.doc(hits[i].doc);
      System.out.println(hitDoc.get("subject"));
    }
    parseQueryString("Success &DADA FoR?/ For Reading the data");
    System.out.println("Success For Reading the data");
     Query q = parser.parse("java");
     BooleanQuery bq = new BooleanQuery();
		bq.add(q, Occur.MUST);
		//bq.add(new TermQuery(new Term(F_DOMAIN)),Occur.MUST);
		ScoreDoc[] hits1 = isearcher.search(bq, null, 1000).scoreDocs;
		for (int i = 0; i < hits1.length; i++) {
		      Document hitDoc = isearcher.doc(hits1[i].doc);
		      System.out.println(hitDoc.get("subject"));
		    }
    try {
		ireader.close();
		directory.close();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
