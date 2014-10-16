package com;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Locale;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

	class SortingExample {
			private Directory directory;
			public SortingExample(Directory directory) {
				this.directory = directory;
			}
			public void displayResults(Query query, Sort sort)
			throws IOException {
			Directory directory=FSDirectory.open(new File(ConstantUtil.INDEX_LOC));
			DirectoryReader ireader = DirectoryReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(ireader);
			
			//searcher.setDefaultFieldSortScoring(true, false);
			TopDocs results = searcher.search(query, null,20, sort);
			
		for (ScoreDoc sd : results.scoreDocs) {
			int docID = sd.doc;
			float score = sd.score;
			Document doc = searcher.doc(docID);
			System.out.println("--city ="+doc.get("city")+" --name = " +doc.get("name")+"-- discription ="+doc.get("description")+" --docID ="+docID+" --score ="+score);
			}
			ireader.close();
			directory.close();
			
		}
	}
	
public class ShortingWithdiffOpt {
	public static void main(String[] args) throws Exception {
		QueryParser parser = new QueryParser(Version.LUCENE_CURRENT,"name",new StandardAnalyzer(Version.LUCENE_CURRENT));
		BooleanQuery query = new BooleanQuery();
		
		Query allBooks = new MatchAllDocsQuery();// Here it is by default match all index and display them
		//query.add(allBooks, BooleanClause.Occur.SHOULD);
		
		System.out.println(" MAtching all index Query is ="+allBooks.toString());
		// Here we match the name is =java must be occure in term
		query.add(parser.parse("java"),
		BooleanClause.Occur.SHOULD);
		System.out.println(" LAst Query is ="+query.toString());
		
		Directory directory=FSDirectory.open(new File(ConstantUtil.INDEX_LOC));
		SortingExample example = new SortingExample(directory);
		
		System.out.println("------------------ Sorting by relevance,Get The Result which is most relivent mean matching exict word or more--------------------------");
		//Lucene sorts by decreasing relevance, also called the score, by default.
		//Each of these variants returns results in the default score order. Sort.RELEVANCE is equivalent to new Sort():
		example.displayResults(query, Sort.RELEVANCE);
		
		System.out.println("--------------------------Sorting by index order ,Sorting it on the basic of index of term----------------------------");
		//This results in the following output. Note the increasing document ID column:
		//Document order may be interesting for an index that you build up once
		example.displayResults(query, Sort.INDEXORDER);
		
		System.out.println("--------------------Sorting by a field, Sort on basic of City field-------------------------------");
		//Sorting by a textual field first requires that the field was indexed as a single token, as
		//described in section 2.4.6. Typically this means using Field.Index.NOT_ANALYZED or
		//Field.Index.NOT_ANALYZED_NO_NORMS
		example.displayResults(query,new Sort(new SortField("city", SortField.Type.STRING)));
		
		System.out.println("-------------------- Sort Reversing sort field with name-------------------------------");
		//The natural order can be reversed per Sort object by specifying true for the second argument.
		example.displayResults(query,new Sort(new SortField("name", SortField.Type.INT, true)));
		
		System.out.println("--------------------Sorting by multiple fields, Sort on basic of Name and description field-------------------------------");
		example.displayResults(query,new Sort(new SortField("name", SortField.Type.STRING),	SortField.FIELD_SCORE,new SortField("description", SortField.Type.INT, true)));
		
		System.out.println("--------------------Selecting a sorting field type, Sort on basic of description field-------------------------------");
		example.displayResults(query,new Sort(new SortField[] {SortField.FIELD_SCORE,new SortField("description", SortField.Type.STRING)}));
		
						
		directory.close();
	}

}
