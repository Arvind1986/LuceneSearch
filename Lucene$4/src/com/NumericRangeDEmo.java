package com;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class NumericRangeDEmo {
	public static void main(String[] args) throws IOException {
		Analyzer analyzer=new StandardAnalyzer(Version.LUCENE_CURRENT);
		Directory directory=FSDirectory.open(new File(ConstantUtil.INDEX_LOC));
		DirectoryReader r= DirectoryReader.open(directory);
		IndexSearcher searcher=new IndexSearcher(r);
		// result from start 10361L is not include in result. if ("salary", 10361L, 50000L, true, true); then this result is includes
		NumericRangeQuery  query=NumericRangeQuery.newLongRange("salary", 10361L, 50000L, false, true);
		
		Sort sort = new Sort(new SortField[] {SortField.FIELD_SCORE,new SortField("salary", SortField.Type.LONG) });  
		TopFieldCollector collector =TopFieldCollector.create(sort, 20, true, true, true, false);
		System.out.println("Query is "+query.toString());
		
	    searcher.search(query,collector);
	    ScoreDoc[] sortedHits = collector.topDocs().scoreDocs; 
	    System.out.println(sortedHits.length+" Actual Score doc");
	    
	  for(int i=0;i<sortedHits.length;++i) {
		int j=sortedHits[i].doc;
		Document document=searcher.doc(j);
		System.out.println("NAme is "+document.get("name")+"----- And ---Salary is "+document.get("salary"));
	}
	 r.close();
	 directory.close();
	}

	
	/// Create the index 
	
	/*public static void main(String[] args) throws IOException {
		Analyzer analyzer=new StandardAnalyzer(Version.LUCENE_CURRENT);
		Directory directory=FSDirectory.open(new File(ConstantUtil.INDEX_LOC));
		IndexWriterConfig config =new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
		IndexWriter indexWriter=new IndexWriter(directory, config);
		 for (int i=1;i<20;i++){
			 long sal =ThreadLocalRandom.current().nextLong(10000, 100000);
			 String name ="arvind"+Math.random();
			 write(indexWriter, name, sal);
		 }
		 System.out.println("done");
		 indexWriter.close();
		 directory.close();
	}
	
	public static void write(IndexWriter writer,String name, long sal) throws IOException{
		Document doc=new Document();
		doc.add(new LongField("salary", sal, Field.Store.YES));
		doc.add(new StringField("name", name,Field.Store.YES ));
		writer.addDocument(doc);
		
	}*/
}
