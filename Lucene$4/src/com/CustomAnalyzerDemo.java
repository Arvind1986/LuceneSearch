package com;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

public class CustomAnalyzerDemo {
	CustomAnalyzerDemo()
	{
		super();
	}
public static void main(String[] args) throws Exception {
	final CharArraySet arraySet=new CharArraySet(Version.LUCENE_CURRENT, 20, true);
	final String set[] ={ "a", "and", "are",
		"as", "at", "be", "but", "by", "for", "if", "in", "into", "is",
		"it", "no", "not", "of", "on", "or", "s", "such", "t", "that",
		"the", "their", "then", "there", "these", "they", "this", "to",
		"was", "will", "with" ,"an" };
	for (int i = 0; i < set.length; i++) {
		 arraySet.add(set[i]);
	}
		Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_CURRENT,arraySet);
		String text = "This is full  a demo an of the TokenStream API HOME";
		TokenStream stream;
		stream = analyzer.tokenStream(null, text);
		try {
			CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
			try {
				stream.reset();
				// print all tokens until stream is exhausted
				while (stream.incrementToken()) {
					System.out.println(termAtt.toString());
				}
				stream.end();
			} catch(Exception e){
				e.printStackTrace();
		  }
			finally {
				stream.close();
				}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
	}
}
}
