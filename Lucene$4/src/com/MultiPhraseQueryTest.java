package com;

import java.io.IOException;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class MultiPhraseQueryTest {
	private IndexSearcher searcher;
	public static void setUp() throws Exception {
	Directory directory = new RAMDirectory();
	IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT,new WhitespaceAnalyzer(Version.LUCENE_CURRENT));
	IndexWriter writer = new IndexWriter(directory,config);
	Document doc1 = new Document();
	doc1.add(new Field("field",	"the quick brown fox jumped over the lazy dog",	Field.Store.YES, Field.Index.ANALYZED));
	writer.addDocument(doc1);
	Document doc2 = new Document();
	doc2.add(new Field("field",	"the fast fox hopped over the hound",	Field.Store.YES, Field.Index.ANALYZED));
	writer.addDocument(doc2);
	writer.close();
	 testBasic(directory);
	 testAgainstOR(directory);
	}
	public static void testBasic(Directory directory) throws Exception {
		DirectoryReader ireader = DirectoryReader.open(directory);
	    IndexSearcher isearcher = new IndexSearcher(ireader);
		MultiPhraseQuery query = new MultiPhraseQuery();
		query.add(new Term[] {new Term("field", "quick"),new Term("field", "fast")});
		query.add(new Term("field", "fox"));
		System.out.println(query);
		TopDocs hits = isearcher.search(query, 10);
		System.out.println("fast fox match"+hits.totalHits);
		
		//Just as with PhraseQuery, the slop factor is supported. In testBasic(), the slop is used
	    //	to match "quick brown fox" in this search
		query.setSlop(1);
		hits = isearcher.search(query, 10);
		System.out.println("both match"+ hits.totalHits);
		System.out.println("-------------------------------------------------------");
		}
	//
	/*One difference between using MultiPhraseQuery and using PhraseQuery’s Boolean-
	Query is that the slop factor is applied globally with MultiPhraseQuery—it’s applied
	on a per-phrase basis with PhraseQuery*/
	public static void testAgainstOR(Directory directory) throws IOException{
		DirectoryReader ireader = DirectoryReader.open(directory);
	    IndexSearcher isearcher = new IndexSearcher(ireader);
	    PhraseQuery quickFox = new PhraseQuery();
	    quickFox.setSlop(1);
	    quickFox.add(new Term("field", "quick"));
	    quickFox.add(new Term("field", "fox"));
	    PhraseQuery fastFox = new PhraseQuery();
	    fastFox.add(new Term("field", "fast"));
	    fastFox.add(new Term("field", "fox"));
	    BooleanQuery query = new BooleanQuery();
	    query.add(quickFox, BooleanClause.Occur.SHOULD);
	    query.add(fastFox, BooleanClause.Occur.SHOULD);
	    TopDocs hits = isearcher.search(query, 10);
	    System.out.println(hits.totalHits);
		
	}
	public static void main(String[] args) throws Exception {
		setUp();

	}

}
