package com;

import java.awt.TextField;
import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.GradientFormatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SpanGradientFormatter;
import org.apache.lucene.search.highlight.TextFragment;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.search.postingshighlight.PostingsHighlighter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class HighlighterDemo {
public static void main(String[] args) throws IOException, InvalidTokenOffsetsException, ParseException {
	Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
	Directory directory=FSDirectory.open(new File(ConstantUtil.INDEX_LOC));
	DirectoryReader ireader = DirectoryReader.open(directory);
    IndexSearcher searcher = new IndexSearcher(ireader);
    QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, null, analyzer);
    String term ="name:java";
	Query query = parser.parse(term);
	System.out.println(query.toString());
   TopDocs hits=searcher.search(query, 10);
	
	ScoreDoc[] tscore= hits.scoreDocs;
	for (int i = 0; i < tscore.length; i++) {
		Document  doc = ireader.document(tscore[i].doc);
		System.out.println(doc.get("name"));
	}
	 
	  
	  //position highlighter
	
	  /* FieldType offsetsType = new FieldType();
	   offsetsType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
	   Field body = new Field("body", "foobar", offsetsType);
*/
	   // retrieve highlights at query time 
	/*   PostingsHighlighter highlighter1 = new PostingsHighlighter();
	   Query query1 = new TermQuery(new Term("name", "java"));
	   TopDocs topDocs = searcher.search(query1, 10);
	   String highlights[] = highlighter1.highlight("name", query1, searcher, topDocs);
	   for (int i = 0; i < highlights.length; i++) {
		System.out.println(highlights[i]+"-------------");
	}*/
	  //end 
	   
	   SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
		  SpanGradientFormatter formatter=new SpanGradientFormatter(25, "#FFFFFF", "#000000", "#FFFFFF", "#ACD123");
		  GradientFormatter formatter2 =new GradientFormatter((float) 23.4, "#a23ef4", "#aaadc3", "#11ddff", "#bbc278");
		  
	  Highlighter highlighter = new Highlighter(formatter2, new QueryScorer(query));
	  for (int i = 0; i < tscore.length; i++) {
	    int id = hits.scoreDocs[i].doc;
	    Document doc = searcher.doc(id);
	    String text = doc.get("name");
	    TokenStream tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), id, "name", analyzer);
	    TextFragment[] frag = highlighter.getBestTextFragments(tokenStream, text, false, 10);//highlighter.getBestFragments(tokenStream, text, 3, "...");
	    for (int j = 0; j < frag.length; j++) {
	      if ((frag[j] != null) && (frag[j].getScore() > 0)) {
	        System.out.println((frag[j].toString()));
	      }
	    }
	    //Term vector
	    System.out.println("term vector---------------------------------");
	    text = doc.get("name");
	    tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), hits.scoreDocs[i].doc, "name", analyzer);
	    frag = highlighter.getBestTextFragments(tokenStream, text, false, 10);
	    for (int j = 0; j < frag.length; j++) {
	      if ((frag[j] != null) && (frag[j].getScore() > 0)) {
	        System.out.println((frag[j].toString()));
	      }
	    }
	    System.out.println("-------------");
	  }
}
}
