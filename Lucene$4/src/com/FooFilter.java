package com;

import java.io.IOException;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;

public class FooFilter extends TokenStream {
public FooFilter(Tokenizer string) {
	super(string);
}

@Override
public boolean incrementToken() throws IOException {
	// TODO Auto-generated method stub
	return false;
}
	

}
