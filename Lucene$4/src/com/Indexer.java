package com;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.flexible.standard.config.NumericFieldConfigListener;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.TimeUnits;
import org.apache.lucene.util.Version;

public class Indexer {
public static void main(String[] args) throws Exception {
	 	String indexDir = ConstantUtil.INDEX_LOC;
		String dataDir = "F:\\sample\\Apache-solr-4.0.0\\apache-solr-4.0.0\\example\\example-DIH\\solr\\mail\\conf";
		long start = System.currentTimeMillis();
		Indexer indexer = new Indexer(indexDir);
		int numIndexed;
		 try {
			 numIndexed = indexer.index(dataDir, new TextFilesFilter());
		 } finally {
			 indexer.close();
		 }
		long end = System.currentTimeMillis();
		System.out.println("Indexing " + numIndexed + " files took "
		+ (end - start) + " milliseconds");
	}

		private IndexWriter writer;
		public Indexer(String indexDir) throws IOException {
			Directory dir = FSDirectory.open(new File(indexDir));
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, new StandardAnalyzer(Version.LUCENE_CURRENT));
			writer = new IndexWriter(dir, config);
		}
		public void close() throws IOException {
				writer.close();
			}
 		public int index(String dataDir, FileFilter filter)
		throws Exception {
 			File[] files = new File(dataDir).listFiles();
 			for (File f: files) {
 				if (!f.isDirectory() &&
 						!f.isHidden() &&
 						f.exists() &&
 						f.canRead() &&
		(filter == null || filter.accept(f))) {
		indexFile(f);
       }
      }
		return writer.numDocs();
 }
		private void indexFile(File f) throws IOException {
			System.out.println("Indexing " + f.getCanonicalPath());
			Document doc = getDocument(f);
			writer.addDocument(doc);
			
		}
		private Document getDocument(File f) throws IOException {
			Document doc = new Document();
			doc.add(new Field("contents", new FileReader(f),TermVector.WITH_POSITIONS_OFFSETS));
			doc.add(new Field("filename", f.getName(),
			Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("fullpath", f.getCanonicalPath(),
			Field.Store.YES, Field.Index.NOT_ANALYZED));
		    doc.add(new IntField("age",Integer.valueOf(DateTools.dateToString(new Date(), Resolution.YEAR)), Field.Store.YES));
			return doc;
		}
		public static class TextFilesFilter implements FileFilter {

			@Override
			public boolean accept(File path) {
				// TODO Auto-generated method stub
				return path.getName().toLowerCase()
						.endsWith(".txt");
			}

		}
}