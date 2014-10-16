package com;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class AppTest {
	public static void main(String[] args) throws IOException {
		  Version matchVersion = Version.LUCENE_CURRENT; // Substitute desired Lucene version for XY
	         Analyzer myAnalyzer = new StandardAnalyzer(matchVersion);
	         final String text = "This is a demo of the TokenStream API";
	         TokenStream stream = myAnalyzer.tokenStream("null", new StringReader(text));
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

