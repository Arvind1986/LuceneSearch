package com;

import java.io.Reader;
import java.io.StringReader;

/*
  A field cache can only be used on fields that have a single term. This typically
means the field was indexed with Index.NOT_ANALYZED or
Index.NOT_ANALYZED_NO_NORMS, though it’s also possible to analyze the
fields as long as you’re using an analyzer, such as KeywordAnalyzer, that
always produces only one token

*/
public class FieldChacheDemo {
	public static void main(String[] args) {
		/*You can easily use the field cache to load an array of native values for a given field,
		indexed by document number. For example, if every document has a field called
		“weight,” you can get the weight for all documents like this:*/
		Reader reader=new StringReader("Hi this is chache field demo examle");	
		
		//float[] weights = FieldCache.DEFAULT.getFloats(reader, "weight", true);
		
		/*Then, simply reference weights[docID] whenever you need to know a document’s
		weight value*/
	}

}
