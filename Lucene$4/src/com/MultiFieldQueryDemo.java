package com;

import java.io.File;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class MultiFieldQueryDemo {
	public static void testDefaultOperator() throws Exception {
		Query query = new MultiFieldQueryParser(Version.LUCENE_CURRENT,	new String[]{"name", "description"},new SimpleAnalyzer(Version.LUCENE_CURRENT)).parse("java");
		Directory directory = FSDirectory.open(new File(ConstantUtil.INDEX_LOC));
		DirectoryReader reader =DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs hits = searcher.search(query, 10);
		//assertTrue(TestUtil.hitsIncludeTitle(searcher,hits,	"Ant in Action"));
		//assertTrue(TestUtil.hitsIncludeTitle(searcher,hits,"Extreme Programming Explained"));
		System.out.println("=======----hits-------------"+hits);
		reader.close();
		directory.close();
		}
		public static void testSpecifiedOperator() throws Exception {
			DisjunctionMaxQuery maxQuery =new DisjunctionMaxQuery(0);
		
		Query query = MultiFieldQueryParser.parse(Version.LUCENE_CURRENT,"java",new String[]{"name", "city"},
		new BooleanClause.Occur[]{BooleanClause.Occur.MUST,BooleanClause.Occur.MUST},new SimpleAnalyzer(Version.LUCENE_CURRENT));
		maxQuery.add(query);
		System.out.println("DisjunctionMaxQuery="+maxQuery.toString());
		Directory directory = FSDirectory.open(new File(ConstantUtil.INDEX_LOC));
		DirectoryReader reader =DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs hits = searcher.search(query, 10);
		//assertTrue(TestUtil.hitsIncludeTitle(searcher,hits,"Lucene in Action, Second Edition "));
		System.out.println("one and only one="+ hits.scoreDocs.length);
		reader.close();
		reader.close();
		}
	public static void main(String[] args) throws Exception {
		testDefaultOperator();
		testSpecifiedOperator();
	}

}
