package com;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.Version;

public class TokenStreamDemo {
	public static void main(String[] args) throws IOException {
		Analyzer analyzer =new StandardAnalyzer(Version.LUCENE_CURRENT);
		Reader textReader = new StringReader("This id demo text for check the streaminput");
		TokenStream tokenStream	= analyzer.tokenStream("fieldName",textReader);
		CharTermAttribute terms =tokenStream.addAttribute(CharTermAttribute.class);
		OffsetAttribute offsets	= tokenStream.addAttribute(OffsetAttribute.class);
		PositionIncrementAttribute positions = tokenStream.addAttribute(PositionIncrementAttribute.class);
		 tokenStream.reset(); 
		while (tokenStream.incrementToken()) {
			int increment = positions.getPositionIncrement();
			int start = offsets.startOffset();
			int end = offsets.endOffset();
			String term = terms.toString();
			System.out.println(increment);
			System.out.println(start);
			System.out.println(end);
			System.out.println(term);
		}
		tokenStream.end();
	}

}
