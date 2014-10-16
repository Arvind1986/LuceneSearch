package com;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.FieldCacheRangeFilter;
import org.apache.lucene.search.FieldCacheTermsFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.PrefixFilter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeFilter;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;


/*//Filtering is a mechanism of narrowing the search space, allowing only a subset of the
documents to be considered as possible hits.*/
/*TermRangeFilter-- matches only documents containing terms within a specified
range of terms. It’s exactly the same as TermRangeQuery, without scoring.

􀂃 NumericRangeFilter-- matches only documents containing numeric values
within a specified range for a specified field. It’s exactly the same as Numeric-
RangeQuery, without scoring.

􀂃 FieldCacheRangeFilter-- matches documents in a certain term or numeric
range, using the FieldCache (see section 5.1) for better performance.

􀂃 FieldCacheTermsFilter-- matches documents containing specific terms, using
the field cache for better performance.

􀂃 QueryWrapperFilter-- turns any Query instance into a Filter instance, by using
only the matching documents from the Query as the filtered space, discarding
the document scores.

􀂃 SpanQueryFilter-- turns a SpanQuery into a SpanFilter, which subclasses the
base Filter class and adds an additional method, providing access to the positional
spans for each matching document. This is just like QueryWrapperFilter
but is applied to SpanQuery classes instead.

􀂃 PrefixFilte--r matches only documents containing terms in a specific field with
a specific prefix. It’s exactly the same as PrefixQuery, without scoring.
Licensed to theresa smith <anhvienls@gmail.com>

􀂃 CachingWrapperFilter-- is a decorator over another filter, caching its results to
increase performance when used again.

􀂃 CachingSpanFilter-- does the same thing as CachingWrapperFilter, but it
caches a SpanFilter.

􀂃 FilteredDocIdSet-- allows you to filter a filter, one document at a time. In order
to use it, you must first subclass it and define the match method in your subclass.*/
public class FilteringSearchDemo {
	public static void main(String[] args) throws Exception {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
		Directory directory = FSDirectory.open(new File(ConstantUtil.INDEX_LOC));
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, null,analyzer);
		Term term = new Term("description", "java");
		Query query = new org.apache.lucene.search.TermQuery(term);
		
		BytesRef lowerterm =new BytesRef(new String("d").getBytes());
		BytesRef upperterm =new BytesRef(new String("d").getBytes());
		
		BytesRef lowertermdate =new BytesRef(new Date().toString().getBytes());
		BytesRef uppertermdate =new BytesRef(new Date().toString().getBytes());
		
		/*our sample data this field name is title2, which is the title of each book indexed lowercased
		using Field.NOT_ANALYZED_NO_NORMS. The two final Boolean arguments to the
		constructor for TermRangeFilter, includeLower, and includeUpper determine
		whether the lower and upper terms should be included or excluded from the filter.*/
		Filter filter = new TermRangeFilter("title2", lowerterm, upperterm, true, true);

		/*TermRangeFilter also supports open-ended ranges. To filter on ranges with one end
		of the range specified and the other end open, just pass null for whichever end
		should be open*/
		filter = new TermRangeFilter("modified", null, uppertermdate, false, true);
		filter = new TermRangeFilter("modified", lowertermdate, null, true, false);
		//TermRangeFilter provides two static convenience methods to achieve the same thing:
		filter = TermRangeFilter.Less("modified", uppertermdate);
		filter = TermRangeFilter.More("modified", lowertermdate);
		
		/*NumericRangeFilter filters by numeric value. This is just like NumericRangeQuery,
		minus the constant scoring:*/
		filter = NumericRangeFilter.newIntRange("pubmonth",201001,201006,true,true);
		
		/*FieldCacheRangeFilter is another option for range filtering. It achieves exactly the
		same filtering as both TermRangeFilter and NumericRangeFilter, but does so by
		using Lucene’s field cache. This may result in faster performance in certain situations,
		since all values are preloaded into memory*/
		filter = FieldCacheRangeFilter.newStringRange("title2","d", "j", true, true);
		filter = FieldCacheRangeFilter.newIntRange("pubmonth",201001,201006,true,true);
		
		/*Sometimes you’d simply like to select specific terms to include in your filter. For example,
		perhaps your documents have Country as a field, and your search interface presents
		a checkbox allowing the user to pick and choose which countries to include in
		the search. There are two ways to achieve this.*/
		filter = new FieldCacheTermsFilter("category",	new String[] {"/health/alternative/chinese","/technology/computers/ai",	"/technology/computers/programming"});
		
		TermQuery categoryQuery =new org.apache.lucene.search.TermQuery(new Term("category", "/philosophy/eastern"));
		Filter categoryFilter = new QueryWrapperFilter(categoryQuery);
				
		SpanQuery categoryQuery1 =new SpanTermQuery(new Term("category", "/philosophy/eastern"));
		//categoryFilter = new SpanQueryFilter(categoryQuery1);
		
//		PrefixFilter, the corollary to PrefixQuery, matches documents containing Terms
//		starting with a specified prefix.
		Filter prefixFilter = new PrefixFilter(new Term("category",	"/technology/computers"));
		System.out.println(prefixFilter);
		
//		The biggest benefit from filters comes when they’re cached and reused using
//		CachingWrapperFilter, which takes care of caching automatically (internally using a
//		WeakHashMap, so that externally dereferenced entries get garbage collected). You can
//		cache any filter using CachingWrappingFilter. Filters cache by using the
//		IndexReader as the key, which means searching should also be done with the same
//		instance of IndexReader to benefit from the cache. If you aren’t constructing
//		IndexReader yourself but are creating an IndexSearcher from a directory, you must
//		use the same instance of IndexSearcher to benefit from the caching.
		CachingWrapperFilter cachingFilter;
			cachingFilter = new CachingWrapperFilter(filter);
		
		TopDocs docs =isearcher.search(query, filter, 10);


	}

}
