package com;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.phonetic.DoubleMetaphoneFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;


class MyDemoAnalyzer extends Analyzer{
	private Version matchVersion;
	private Set<?> stopWords;
	
	@SuppressWarnings("deprecation")
	public MyDemoAnalyzer() {
		this.matchVersion = Version.LUCENE_CURRENT;
		stopWords = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
	}
	@SuppressWarnings("deprecation")
	public MyDemoAnalyzer(String[] stopWords) {
		this.matchVersion = Version.LUCENE_CURRENT;
		this.stopWords = StopFilter.makeStopSet(matchVersion, stopWords);
	}
	public MyDemoAnalyzer(Version matchVersion,String[] stopWords) {
	    this.matchVersion = matchVersion;
	    this.stopWords = StopFilter.makeStopSet(matchVersion, stopWords);
	  }
	public MyDemoAnalyzer(Version matchVersion) {
	    this.matchVersion = matchVersion;
	    stopWords = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
	  }
	@Override
	protected TokenStreamComponents createComponents(String arg0, Reader reader) {
	CharArraySet stoplist = new CharArraySet(matchVersion,stopWords, true);//create the stopword charArrayset from stop word
	
	@SuppressWarnings({ "resource", "unused" })
	LetterTokenizer letterTokenizer = new LetterTokenizer(matchVersion, reader);
	LowerCaseTokenizer lowerCaseTokenizer = new LowerCaseTokenizer(matchVersion, reader);// create the lowercase tokenizer 
	
	  DoubleMetaphoneFilter filter =new DoubleMetaphoneFilter(letterTokenizer, 0, true);// it is search the same voice word as kool and cool 
	 //return new  TokenStreamComponents(null,filter);
	  
	/*  TokenStream result = new SynonymFilter(
			  new StopFilter(true,
			  new LowerCaseFilter(
			  new StandardFilter(
			  new StandardTokenizer(
			  Version.LUCENE_30, reader))),
			  StopAnalyzer.ENGLISH_STOP_WORDS_SET),
			  engine
			  );
	  */
		return new  TokenStreamComponents(null, new StopFilter(matchVersion, lowerCaseTokenizer,stoplist));//create the filter with tokenizer
	}
}


public class MyAnalyzerTest  {
	 public static void main(String[] args) throws IOException {
			    // text to tokenize
			    final String text = "This is ARVIND a demo of the TokenStream API";
			    Version matchVersion = Version.LUCENE_44; // Substitute desired Lucene version for XY
			    
			    @SuppressWarnings("resource")
				MyDemoAnalyzer analyzer = new MyDemoAnalyzer(matchVersion);
			    
			    TokenStream stream = analyzer.tokenStream("field", new StringReader(text));
			    // get the CharTermAttribute from the TokenStream
			    CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
			    try {
			      stream.reset();
			      // print all tokens until stream is exhausted
			      while (stream.incrementToken()) {
			        System.out.println(termAtt.toString());
			      }
			      stream.end();
			    } finally {
			      stream.close();
			    }
	 }
	
}