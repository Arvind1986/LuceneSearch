package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.tools.ant.types.resources.Files;

public class AddDoc {
public static void main(String[] args) throws IOException {
	Document doc = new Document();
	doc.add(new Field("title", "Fast and Accurate Read Alignment",Store.YES,Index.ANALYZED));
	doc.add(new Field("author", "Heng Li",	Store.YES,Index.ANALYZED));
	doc.add(new Field("author", "Richard Durbin",	Store.YES,Index.ANALYZED));
	doc.add(new Field("journal","Bioinformatics",	Store.YES,Index.ANALYZED));
	doc.add(new Field("mesh","algorithms",	Store.YES,Index.ANALYZED));
	doc.add(new Field("mesh","genomics/methods",	Store.YES,Index.ANALYZED));
	doc.add(new Field("mesh","sequence alignment/methods",	Store.YES,Index.ANALYZED));
	doc.add(new Field("pmid","20080505",	Store.YES,Index.NOT_ANALYZED));
	 boolean create = true;
		Directory dir = FSDirectory.open(new File(ConstantUtil.App_LOC));
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40,analyzer);
		  IndexWriter writer = new IndexWriter(dir, iwc);
		  
		  if (create) {
		   iwc.setOpenMode(OpenMode.CREATE);
		     } else {
			        // Add new documents to an existing index:
			       iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			      }
		  File docDir = new File(ConstantUtil.INDEX_LOC);
		  indexDocs(writer, docDir);
		  
		  writer.close();
		 dir.close();
		  Date end = new Date();
	      System.out.println(end.getTime() +" total milliseconds");
}
      static void indexDocs(IndexWriter writer, File file)
	  throws IOException {
	 // do not try to index files that cannot be read
	    if (file.canRead()) {
	      if (file.isDirectory()) {
	      String[] files = file.list();
	        // an IO error could occur
	      if (files != null) {
	          for (int i = 0; i < files.length; i++) {
	           indexDocs(writer, new File(file, files[i]));
	          }
	        }
       } else {
       FileInputStream fis;
         try {
           fis = new FileInputStream(file);
         } catch (FileNotFoundException fnfe) {
         return;
        }
         try {
           Document doc = new Document();
          Field pathField = new StringField("path", file.getPath(), Field.Store.YES);
           doc.add(pathField);
           doc.add(new LongField("modified", file.lastModified(), Field.Store.NO));
 
         doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(fis, "UTF-8"))));
           if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
             System.out.println("adding " + file);
            writer.addDocument(doc);
           } else {
          System.out.println("updating " + file);
            writer.updateDocument(new Term("path", file.getPath()), doc);
         }
          
       } finally {
           fis.close();
       }
      }
    }
   }
 }

