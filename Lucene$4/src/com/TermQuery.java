package com;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.queryparser.xml.builders.NumericRangeQueryBuilder;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.AutomatonQuery;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.apache.lucene.util.automaton.Automaton;
import org.w3c.dom.Element;

/*
 * For example, consider the e-mail subject "Job openings for Java Professionals at Bangalore.
 * " Assume you indexed this using the StandardAnalyzer.
 *  Now if we search for "Java" using TermQuery,
 *  it would not return anything as this text would have been normalized and put in lowercase by the StandardAnalyzer.
 *  If we search for the lowercase word "java," it would return all the mail that contains this word in the subject field.
 * */
public class TermQuery {
	public static void main(String[] args) throws IOException, ParseException {
		Analyzer myanalyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
		Analyzer analyzer = new Analyzer() {public int getPositionIncrementGap(String fieldName) {
				return 10;
			}

			@Override
			protected TokenStreamComponents createComponents(String paramString, Reader paramReader) {
				return new TokenStreamComponents(new WhitespaceTokenizer(
						Version.LUCENE_CURRENT, paramReader));
			}
		};
		myanalyzer.getPositionIncrementGap("f");
		Directory directory = FSDirectory.open(new File(ConstantUtil.INDEX_LOC));
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, null,analyzer);

		/*
		 * --------------------------------------- You can search within a range
		 * using RangeQuery. All the terms are arranged lexicographically in the
		 * index. Lucene's RangeQuery lets users search terms within a range.
		 * The range can be specified using a starting term and an ending term,
		 * which may be either included or excluded.
		 */

		/*
		 * RangeQuery example:Search mails from 01/06/2009 to 6/06/2009 both
		 * inclusive
		 */

		Term begin = new Term("date", "20090601");
		Term end = new Term("date", "20090606");
		// Query query1 = new RangeQuery(begin, end, true);

		/*----------------------------------------------------------
		 * You can search by a prefixed word with PrefixQuery,
		 *  which is used to construct a query that matches the documents containing terms that start with a specified word prefix. 
		Search mails having sender field prefixed by the word 'job'
		
		Term term = new Term("category","/technology/computers/programming");
		PrefixQuery query = new PrefixQuery(term);
		
		Our PrefixQueryTest demonstrates the difference between a PrefixQuery and a
		TermQuery. A methodology category exists below the /technology/computers/
		programming category. Books in this subcategory are found with a PrefixQuery but
		not with the TermQuery on the parent category.
		 */

		PrefixQuery query2 = new PrefixQuery(new Term("description", "lan"));

		/*---------------------------------------------------------------
		 * For example, consider the e-mail subject "Job openings for Java Professionals at Bangalore.
		 * " Assume you indexed this using the StandardAnalyzer.
		 *  Now if we search for "Java" using TermQuery,
		 *  it would not return anything as this text would have been normalized and put in lowercase by the StandardAnalyzer.
		 *  If we search for the lowercase word "java," it would return all the mail that contains this word in the subject field.
		 * */

		Term term = new Term("description", "java");
		Query query3 = new org.apache.lucene.search.TermQuery(term);

		/*---------------------------------------------------------------
		 * You can construct powerful queries by combining any number of query objects using BooleanQuery.
		It uses query and a clause associated with a query that indicates 
		if a query should occur, must occur, or must not occur.
		In a BooleanQuery, the maximum number of clauses is restricted to 1,024 by default.
		You can set the maximum classes by calling the setMaxClauseCount method.

		 */
		// Search mails have both 'java' and 'bangalore' in the subject field
	
		Query searchingBooks =new org.apache.lucene.search.TermQuery(new Term("sub", "abbc"));
		Query books2010 =NumericRangeQuery.newIntRange("pubmonth", 201001,201012,true, true);
				BooleanQuery searchingBooks2010 = new BooleanQuery();
				
				searchingBooks2010.add(searchingBooks, BooleanClause.Occur.MUST);
				searchingBooks2010.add(books2010, BooleanClause.Occur.MUST);
			/*	This query finds all books containing the subject "search".
				This query finds all books published in 2010.
				Here we combine the two queries into a single Boolean query with both clauses
				required (the second argument is BooleanClause.Occur.MUST).
			*/	
				BooleanQuery bquery = new BooleanQuery();
				bquery.add(new FuzzyQuery(new Term("field", "kountry")),
				BooleanClause.Occur.MUST);
				bquery.add(new org.apache.lucene.search.TermQuery(new Term("title", "western")),
				BooleanClause.Occur.SHOULD);	
				
		Query query4 = new org.apache.lucene.search.TermQuery(new Term(	"description", "language"));
		Query query51 = new org.apache.lucene.search.TermQuery(new Term("description", "secure"));
		
		BooleanQuery query5 = new org.apache.lucene.search.BooleanQuery();
		query5.add(query4, Occur.MUST);
		query5.add(query51, Occur.MUST);

		// here we match all index and display all result wich is match or not match
		BooleanQuery query52 = new org.apache.lucene.search.BooleanQuery();
		Query allBooks = new MatchAllDocsQuery();// Here it is by default match all index and display them
		query52.add(allBooks, BooleanClause.Occur.SHOULD);
		/*--------------------------------------------------
		 * You can search by phrase using PhraseQuery.
		A PhraseQuery matches documents containing a particular sequence of terms.
		PhraseQuery uses positional information of the term that is stored in an index.
		The distance between the terms that are considered to be matched is called slop.
		By default the value of slop is zero, and it can be set by calling the setSlop method.
		PhraseQuery also supports multiple term phrases.
		 */

		/*
		 * PhraseQuery example: Search mails that have phrase 'job opening j2ee'
		 * in the subject field.
		 */
		PhraseQuery query6 = new PhraseQuery();
		query6.setSlop(0);
		query6.add(new Term("name", "JAVA and region"));
		// query6.add(new Term("description","jsf")); // if use this then show
		// error becase pharaseQuery is use form only only field

		/*
		 * ---------------------------------------------------------- A
		 * WildcardQuery implements a wild-card search query, which lets you do
		 * searches such as arch* (letting you find documents containing
		 * architect, architecture, etc.). Two standard wild cards are used: for
		 * zero or more ? for one or more There could be a performance drop if
		 * you try to search using a pattern in the beginning of a wild-card
		 * query, as all the terms in the index will be queried to find matching
		 * documents.
		 */
		// Search for 'arch*' to find e-mail messages that have word 'architect'
		// in the subject field./

		WildcardQuery query7 = new WildcardQuery(new Term("description",
				"progr*"));

		/*
		 * ---------------------------------------------------- You can search
		 * for similar terms with FuzzyQuery, which matches words that are
		 * similar to your specified word. The similarity measurement is based
		 * on the Levenshtein (edit distance) algorithm. In FuzzyQuery is used
		 * to find a close match of a misspelled word "admnistrtor," though this
		 * word is not indexed.
		 */

		/*
		 * Search for emails that have word similar to 'admnistrtor' in the
		 * subject field. Note we have misspelled admnistrtor here.
		 */

		FuzzyQuery query8 = new FuzzyQuery(new Term("f", "ends starts"));
		query8.setBoost(3);

		/*-------------------------------------------------------------
		 * QueryParser is useful for parsing human-entered query strings.
		You can use it to parse user-entered query expressions into a Lucene query object, 
		which can be passed to IndexSearcher's search method.
		It can parse rich query expressions.
		QueryParser internally converts a human-entered query string into one of the concrete query subclasses.
		You need to escape special characters such as *, ? with a backslash (\).
		You can construct Boolean queries textually using the operators AND, OR, and NOT. */
		Query tquery = new QueryParser(Version.LUCENE_30,
				"subject", analyzer)
				.parse("title2:[Q TO V]");
		
		QueryParser queryParser = new QueryParser(Version.LUCENE_CURRENT,
				"subject", analyzer);
		
		queryParser.setPhraseSlop(5);// here set the slop as "subject~5"
		Query fuzzylikequery = queryParser.parse("kountry~0.7");
		
		// Search for emails that contain the words 'job openings' and '.net'
		// and 'pune'
		Query query9 = queryParser.parse("java AND spring ,jsp");
		TopDocs docs = isearcher.search(query9, 10);
		/*
		 * ScoreDoc[] score= docs.scoreDocs; for (int i = 0; i < score.length;
		 * i++) { Document doc = ireader.document(score[i].doc);
		 * System.out.println(doc.get("subject")); }
		 * System.out.println("Total Hits counts is ="+docs.totalHits);
		 */

		// ------------------------------------------------------------------
		String queryStr = "author:dean";// and date:[ " + date1 + " TO
										// " + date2// + " ]";
		Query query10 = parser.parse(queryStr);

		// ---------------------------------------------------------------------------------------------

		// Multi query Parser for search the text in different field

		String[] fields = { "name", "city", "description" };
		MultiFieldQueryParser multiQueryparser = new MultiFieldQueryParser(
				Version.LUCENE_CURRENT, fields, analyzer);
		Query query11 = multiQueryparser.parse("java");

		// -----------------------------------------------------
		//
		// TErm range query to search the range data from doc
		// The last two Booleans to the TermRangeQuery state whether the start
		// and end points
		// are inclusive (true) or exclusive (false). then use as {from to
		// value} and if use true and true then use [value to value]
		// as if index ha value 10 and start range is 10 then if use inclusive
		// (false) for lower bound then this result is not include in result
		TermRangeQuery query12 = new TermRangeQuery("title2", new BytesRef(
				new String("d").getBytes()), new BytesRef(
				new String("j").getBytes()), true, true);

		NumericRangeQuery query13 = NumericRangeQuery.newIntRange("pubmonth",
				200605, 200609, true, true);

		// -----------------------------------------------------
		//MAtch the all document as like *:*
		Query query14 = new MatchAllDocsQuery();
		
		Query q = new QueryParser(Version.LUCENE_30,
				"field", analyzer)
				.parse("\"term\"");////here phares query as search "trerm"
		// -----------------------------------------------------
		//Grouping query
		Query groupquery = new QueryParser(
				Version.LUCENE_30,
				"subject",
				analyzer).parse("(agile OR extreme) AND methodology");
				TopDocs matches12 = isearcher.search(groupquery, 10);
		// -----------------------------------------------------
		// DisjunctionMaxQuery, which wraps one or more arbitrary queries, OR’ing together the documents they match.	
		/*DisjunctionMaxQuery also includes an optional tie-breaker multiplier so that, all
				things being equal, a document matching more queries will receive a higher score
				than a document matching fewer queries		*/
		DisjunctionMaxQuery DisQuery =new DisjunctionMaxQuery(0);
		Query multiFieldquery = new MultiFieldQueryParser(Version.LUCENE_CURRENT,new String[]	{"title", "subject"},new SimpleAnalyzer(Version.LUCENE_CURRENT)).parse("development");
		DisQuery.add(multiFieldquery);
		
		Query query = MultiFieldQueryParser.parse(Version.LUCENE_CURRENT,"java",new String[]{"name", "city"},
				new BooleanClause.Occur[]{BooleanClause.Occur.MUST,BooleanClause.Occur.MUST},new SimpleAnalyzer(Version.LUCENE_CURRENT));
		DisQuery.add(query);
				System.out.println("DisjunctionMaxQuery="+DisQuery.toString());
		// -----------------------------------------------------
				AutomatonQuery query21 =new AutomatonQuery(new Term("f", "the"), new Automaton());
				//System.out.println(query21);
		// -----------------------------------------------------
		// print all query pattern
		// System.out.println("query == "+query1.toString());
		System.out.println("PrefixQuery       == " + query2.toString());
		System.out.println("Term              == " + query3.toString());
		System.out.println("Term query        == " + query4.toString());
		System.out.println("BooleanQuery      == " + query5.toString());
		System.out.println("PhraseQuery       == " + query6.toString());
		System.out.println("WildcardQuery     == " + query7.toString());
		System.out.println("FuzzyQuery        == " + query8.toString());
		System.out.println("QueryParser query == " + query9.toString());
		System.out.println("queryStr          == " + query10.toString());
		System.out.println("MultiField  query == " + query11.toString());
		System.out.println("TermRangeQuery  query == " + query12.toString());
		System.out.println("NumericRangeQuery  query == " + query13.toString());
		System.out.println("MatchAllDocsQuery  query == " + query14.toString());
		System.out.println("tquery  query == " + tquery.toString());
		System.out.println("groupquery  query == " + groupquery.toString());
		System.out.println("MAtching all doc  query == " + query52.toString());
		System.out.println("DisjunctionMaxQuery  arbitrary queries, OR’ing == " + DisQuery.toString());
		System.out.println("AutomatonQuery == " + query21.toString());
		
		System.out
				.println("--------------------------------------------------------");
		// search the document
		
		TopDocs tdocs = isearcher.search(query8, 10);

		ScoreDoc[] tscore = tdocs.scoreDocs;
		for (int i = 0; i < tscore.length; i++) {
			Document doc = ireader.document(tscore[i].doc);
			System.out.println(doc.get("name"));
		}
		System.out.println("Total Hits counts is =" + tdocs.totalHits);
		ireader.close();
		directory.close();
	}
}
