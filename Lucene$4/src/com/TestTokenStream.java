package com;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.Version;

public class TestTokenStream {
public static void main(String[] args) throws IOException {
	Version matchVersion = Version.LUCENE_CURRENT; // Substitute desired Lucene version for XY
    Analyzer analyzer = new EnglishAnalyzer(matchVersion); // or any other analyzer
    TokenStream ts = analyzer.tokenStream("myfield", new StringReader("some text goes here"));
    OffsetAttribute offsetAtt = ts.addAttribute(OffsetAttribute.class);
    
    try {
      ts.reset(); // Resets this stream to the beginning. (Required)
      while (ts.incrementToken()) {
        // Use AttributeSource.reflectAsString(boolean)
        // for token stream debugging.
        System.out.println("token: " + ts.reflectAsString(true));

        System.out.println("token start offset: " + offsetAtt.startOffset());
        System.out.println("  token end offset: " + offsetAtt.endOffset());
      }
      ts.end();   // Perform end-of-stream operations, e.g. set the final offset.
    } finally {
      ts.close(); // Release resources associated with this stream.
    }
}
}
