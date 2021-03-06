package com;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class AnalyzerUtil {
	 public static void displayTokens(Analyzer analyzer, String text) throws IOException {
			 displayTokens(analyzer.tokenStream("contents", new StringReader(text)));
			 }
			 public static void displayTokens(TokenStream stream) throws IOException {
			 CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
			 try {
			      stream.reset();
			      while (stream.incrementToken()) {
			        System.out.println(term.toString());
			      }
			      stream.end();
			    } finally {
			      stream.close();
			    }
			 }
		}

