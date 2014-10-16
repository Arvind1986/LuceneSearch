package com;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spans.SpanFirstQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanNotQuery;
import org.apache.lucene.search.spans.SpanOrQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

 public class SpanQueryTest  {
	 
	private RAMDirectory directory;
	private IndexSearcher searcher;
	private IndexReader reader;
	private static SpanTermQuery quick;
	private static SpanTermQuery brown;
	private static SpanTermQuery red;
	private static SpanTermQuery fox;
	private static SpanTermQuery lazy;
	private static SpanTermQuery sleepy;
	private static SpanTermQuery dog;
	private static SpanTermQuery cat;
	private Analyzer analyzer;
	
	// SpanQuery is match the word on basic int argument passes with term ,is word is "ram all son go" spanquery match the word "son" with spanfirst with int argument 2 then no doc is match.
	//if pass 3 the match the doc.With a slop of 3, 
	
    public static void main(String[] args) throws Exception {
    	SpanQueryTest  test = new SpanQueryTest();
    	test.setUp();
    	/*No matches are found in the first query because the range of 2 is too short to find
    	brown, but the range of 3 is just long enough to cause a match in the second query
    	(see figure 5.2). Any SpanQuery can be used within a SpanFirstQuery,*/
    	test.assertOnlyBrownFox(new SpanFirstQuery(red, 5));
    	test.assertOnlyBrownFox(new SpanFirstQuery(red, 2));//error because red is not match from the start of word position 2
    	
    	// Here match the near word "quick" with "fox" with second position
    	SpanQuery[] quick_brown_dog =new SpanQuery[]{quick, fox,lazy};
    	SpanNearQuery snq =	new SpanNearQuery(quick_brown_dog, 4, true);// Here matches the doc.With a slop of 4, the SpanNearQuery has a match.
    	//SpanNearQuery snq =	new SpanNearQuery(quick_brown_dog, 2, true);// Here none doc is matches because "quick, fox,lazy" each is not exist with distance of 2
    	test.assertOnlyBrownFox(snq);
    	
    	SpanNearQuery quick_fox =new SpanNearQuery(new SpanQuery[]{quick, fox}, 1, true);
    	//here dog is not exist with spannearquery with slop 1
    	SpanNotQuery quick_fox_dog = new SpanNotQuery(quick_fox, dog);
    	test.assertOnlyBrownFox(quick_fox_dog);
    	
    	SpanNearQuery lazy_dog =new SpanNearQuery(new SpanQuery[]{lazy, dog}, 0, true);
    	SpanNearQuery sleepy_cat =new SpanNearQuery(new SpanQuery[]{sleepy, cat}, 0, true);
    	
    	SpanNearQuery qf_near_ld =new SpanNearQuery(new SpanQuery[]{quick_fox, lazy_dog}, 3, true);
    	test.assertOnlyBrownFox(qf_near_ld);
    	SpanNearQuery qf_near_sc =	new SpanNearQuery(new SpanQuery[]{quick_fox, sleepy_cat}, 3, true);
    	test.assertOnlyBrownFox(qf_near_sc);
    	
    	//Span OR query
    	SpanOrQuery or = new SpanOrQuery(new SpanQuery[]{qf_near_ld, qf_near_sc});
    	test.assertOnlyBrownFox(or);
    	//test.dumpSpans(quick);
	}
	protected void setUp() throws Exception {
	directory = new RAMDirectory();
	analyzer = new WhitespaceAnalyzer(Version.LUCENE_CURRENT);
	IndexWriterConfig config =new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
	IndexWriter writer = new IndexWriter(directory,config);
	Document doc = new Document();
	doc.add(new Field("f","the quick brown fox jumps over the lazy dog",Field.Store.YES, Field.Index.ANALYZED));
	writer.addDocument(doc);
	doc = new Document();
	Field field1 = new Field("f","the quick red fox jumps over the sleepy cat",Field.Store.YES, Field.Index.ANALYZED);
	field1.setBoost(1);
	doc.add(field1);
	writer.addDocument(doc);
	writer.close();
	
	IndexReader reader =DirectoryReader.open(directory);
	searcher = new IndexSearcher(reader);
	reader = searcher.getIndexReader();
	quick = new SpanTermQuery(new Term("f", "quick"));
	brown = new SpanTermQuery(new Term("f", "brown"));
	red = new SpanTermQuery(new Term("f", "red"));
	fox = new SpanTermQuery(new Term("f", "fox"));
	lazy = new SpanTermQuery(new Term("f", "lazy"));
	sleepy = new SpanTermQuery(new Term("f", "sleepy"));
	dog = new SpanTermQuery(new Term("f", "dog"));
	cat = new SpanTermQuery(new Term("f", "cat"));
	}
	private void assertOnlyBrownFox(Query query) throws Exception {
		System.out.println("Query is="+query.toString());
	TopDocs hits = searcher.search(query, 10);
	System.out.println("hits ="+hits.totalHits);
	if(hits.totalHits>0)
	System.out.println("doc id is ="+hits.scoreDocs[0].doc);
	System.out.println("----------------");
	/*ScoreDoc[] tscore = hits.scoreDocs;
	for (int i = 0; i < tscore.length; i++) {
		Document doc = reader.document(tscore[i].doc);
		System.out.println(doc.get("f"));
	 }*/
	}
 
	/*private void dumpSpans(SpanQuery query) throws IOException {
		AtomicReaderContext context = null;
	//	Spans spans = query.getSpans((AtomicReaderContext) context, (Bits) reader, null);
		System.out.println(query + ":");
		int numSpans = 0;
		TopDocs hits = searcher.search(query, 10);
		ScoreDoc[] tscore = hits.scoreDocs;
		float[] scores = new float[2];
		for (ScoreDoc sd : hits.scoreDocs) {
		scores[sd.doc] = sd.score;
		}
		for (int i = 0; i < tscore.length; i++) {
		Document doc = reader.document(tscore[i].doc);
		TokenStream stream = analyzer.tokenStream("contents",new StringReader(doc.get("f")));
		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		StringBuilder buffer = new StringBuilder();
		buffer.append(" ");
		int i1 = 0;
		while(stream.incrementToken()) {
			System.out.println(term.toString());
		if (i == spans.start()) {
		buffer.append("<");
		}
		buffer.append(term.toString());
		if (i + 1 == spans.end()) {
		buffer.append(">");
		}
		buffer.append(" ");
		i++;
		}
		buffer.append("(").append(scores[id]).append(") ");
		System.out.println(buffer);
		}
		if (numSpans == 0) {
		System.out.println(" No spans");
		}
		System.out.println();
		}*/
}

