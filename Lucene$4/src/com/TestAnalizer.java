package com;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;

public class TestAnalizer extends Tokenizer   {

	protected TestAnalizer(Reader input) {
		super(input);
	}
	public static void main(String[] args) {
		Analyzer analyzer = new Analyzer() {
			@Override
			   protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
			     Tokenizer source = new TestAnalizer(reader);
			     TokenStream filter = new FooFilter(source);
			     filter = new BarFilter(filter);
			     return new TokenStreamComponents(source, filter);
			   }
		};
	}

	@Override
	public boolean incrementToken() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

}
