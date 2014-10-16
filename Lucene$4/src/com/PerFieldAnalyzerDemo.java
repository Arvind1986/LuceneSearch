package com;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilter;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.Version;

public class PerFieldAnalyzerDemo {
	public static void main(String[] args) throws IOException {
		Map<String, Analyzer> map =new HashMap<String, Analyzer>();
		map.put("body", new StopAnalyzer(Version.LUCENE_CURRENT));
		map.put("title",new SimpleAnalyzer(Version.LUCENE_CURRENT));
		//HEre create the analyzer for per field 
		PerFieldAnalyzerWrapper analyzerWrapper =new PerFieldAnalyzerWrapper(new StandardAnalyzer(Version.LUCENE_CURRENT),map);
		Reader reader =new StringReader("<html><bddy>HI This ids is demO Test there are you understand the value this</body></html>");
		// this filter is use for extract the data from html 
		CharFilter filter= new HTMLStripCharFilter(reader);
		
		TokenStream  stream =analyzerWrapper.tokenStream("body", filter);
		CharTermAttribute attribute =stream.addAttribute(CharTermAttribute.class);
		OffsetAttribute attribute2 =stream.addAttribute(OffsetAttribute.class);
		PositionIncrementAttribute attribute3 =stream.addAttribute(PositionIncrementAttribute.class);
		stream.reset();
		while(stream.incrementToken()){
			int offsset =attribute2.endOffset();
			int pos =attribute3.getPositionIncrement();
			System.out.println(attribute.toString());
		}
		stream.end();
		stream.close();
	}

}
