package com;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class MyAnalyzer extends Analyzer {

	  private Version matchVersion;
	  
	  public MyAnalyzer(Version matchVersion) {
	    this.matchVersion = matchVersion;
	  }

	  @Override
	  protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
	    return new TokenStreamComponents(new WhitespaceTokenizer(matchVersion, reader));
	  }
	  
	  public static void main(String[] args) throws IOException {
	    // text to tokenize
	    final String text = "This is a demo of the TokenStream API";
	    
	    Version matchVersion = Version.LUCENE_44; // Substitute desired Lucene version for XY
	    MyAnalyzer analyzer = new MyAnalyzer(matchVersion);
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