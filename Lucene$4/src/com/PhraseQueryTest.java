package com;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class PhraseQueryTest {
	public static void main(String[] args) throws IOException {
		Analyzer analyzer=new StandardAnalyzer(Version.LUCENE_CURRENT);
		Directory directory=FSDirectory.open(new File(ConstantUtil.INDEX_LOC));
		DirectoryReader r= DirectoryReader.open(directory);
		IndexSearcher searcher=new IndexSearcher(r);

		String[] phrase = new String[] {"quick", "fox"};
		PhraseQuery query = new PhraseQuery();
		//The maximum allowable positional distance between terms to be considered a
		//match is called slop.
		query.setSlop(2);
		// if it is not set then result may come not . here analyzer tokenized the string and create the as possible as pharese as create as, "
		//"the quick brown fox jumped over the lazy dog"
		//as "the", "quick", "brown", "fox", "jumped", "over"," the"," lazy" "dog","the quick","the brown".....,"quick brown","quick fox",..."the quick brown",....."quick brown fox"... and so on
		//the single word slop is 0
		//the two word pair, the slop is 1,
		//the three word pair ,the slop is 2 and so on.......
		System.out.println("query is "+query.toString());
		for (String word : phrase) {
		query.add(new Term("field", word));
		}
		System.out.println("query is "+query.toString());
	    TopDocs docs= searcher.search(query,10);
	    ScoreDoc[] sortedHits = docs.scoreDocs; 
	    System.out.println(sortedHits.length+" Actual Score doc");
	    
	  for(int i=0;i<sortedHits.length;++i) {
		int j=sortedHits[i].doc;
		Document document=searcher.doc(j);
		System.out.println("NAme is "+document.get("field"));
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
		Document doc=new Document();
		doc.add(new Field("field","the quick brown fox jumped over the lazy dog",Field.Store.YES,Field.Index.ANALYZED));
		indexWriter.addDocument(doc);
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
