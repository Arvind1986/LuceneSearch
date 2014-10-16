package com;

import java.io.IOException;

import org.apache.lucene.analysis.TokenStream;

public class BarFilter extends TokenStream {
public BarFilter(TokenStream stream) {
	super(stream);
	// TODO Auto-generated constructor stub
}
	@Override
	public boolean incrementToken() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

}
