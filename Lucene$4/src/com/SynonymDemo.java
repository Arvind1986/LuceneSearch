package com;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.FilteringTokenFilter;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.Version;

class sysnonmsDemoAnalyzer extends Analyzer{
  private Version version;
	public sysnonmsDemoAnalyzer(Version version){
		this.version =version;
	}
	@Override
	protected TokenStreamComponents createComponents(String arg0, Reader reader) {
		SynonymMap.Builder builder = new SynonymMap.Builder(true);
		builder.add(new CharsRef("crimson"), new CharsRef("red"), true);// Here add the sysnonym term as,when get the data as "crimson" then add the sysnonyms word as "red".
		builder.add(new CharsRef("crimson"), new CharsRef("zava jsp"),true);
		//Be sure the boolean last arg you pass there is the one you want.  There are significant tradeoffs here.
		//Add as many terms as you like here...
		SynonymMap mySynonymMap = null;
		try {
			mySynonymMap = builder.build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList stoplist =new ArrayList();
		 stoplist.add("demo");
		 
		    Tokenizer source = new ClassicTokenizer(version, reader);
		    TokenStream filter = new StandardFilter(version, source);
		    filter = new LowerCaseFilter(version,filter);
		    filter = new StopFilter(version, filter, new CharArraySet(version,stoplist, true));
		    ((FilteringTokenFilter) filter).setEnablePositionIncrements(true);
		    //filter = new PorterStemFilter(filter);// this is stop the english word as 's ,ing ,ed etc as "colling" word indexed as "coll"
		    filter = new SynonymFilter(filter, mySynonymMap, false);
		   // filter = new PhoneticFilter(filter, new DoubleMetaphone(), true); // this is add the extra word for same sound word as "cool" and "kool" like this
		    //Whatever other filter you want to add to the chain, being mindful of order.
		    return new TokenStreamComponents(source, filter);
		}
	}

public class SynonymDemo {
 public static void main(String[] args) throws IOException {
	Analyzer analyzer= new sysnonmsDemoAnalyzer(Version.LUCENE_CURRENT);// this is create the term with sysnonms and stopword
	TokenStream tokenStream =analyzer.tokenStream("content", new String("This kool and cool is Demo crimson java data"));
	CharTermAttribute attribute = tokenStream.addAttribute(CharTermAttribute.class);
	OffsetAttribute offsets	= tokenStream.addAttribute(OffsetAttribute.class);
	PositionIncrementAttribute positions = tokenStream.addAttribute(PositionIncrementAttribute.class);
	 tokenStream.reset(); 
	 System.out.println("increment \t start \t end  \t term ");
	 System.out.println("==============================================");
	while (tokenStream.incrementToken()) {
		int increment = positions.getPositionIncrement();
		int start = offsets.startOffset();
		int end = offsets.endOffset();
		String term = attribute.toString();
		 System.out.println("        "+increment+" \t"+ start +"\t"+ end  +"\t"+ term);
	}
	tokenStream.end();	
	/*while(tokenStream.incrementToken()){
		// Output is add the sysnonms word with position on 0 and remove the stopword in the stream
		System.out.println(attribute.toString());
	}*/
	tokenStream.close();
	analyzer.close();
 }
}
