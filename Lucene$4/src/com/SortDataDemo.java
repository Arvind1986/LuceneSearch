package com;
import static com.SortDoc.show; // Here this is static import

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

public class SortDataDemo {
public static void main(String[] args) throws IOException {
	StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
    Directory index = FSDirectory.open(new File(ConstantUtil.INDEX_LOC));;
    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
    IndexWriter w = new IndexWriter(index, config);
    addDoc(w, "orange", 1);
    addDoc(w, "lemon orange dddd", 19);
    addDoc(w, "second orange", 2);
    addDoc(w, "third orange", 3);
    addDoc(w, "Demo orange", 14);
    addDoc(w, "Larg orange", 15);
    w.close();
    show();
}

static void addDoc(IndexWriter writer, String value, Integer num) throws IOException {
    Document doc = new Document();
      doc.add(new Field("title", value, Field.Store.YES, Field.Index.ANALYZED));
      //doc.add(new NumericField("num", Field.Store.NO, false).setIntValue(num));
      doc.add(new Field ("num", Integer.toString(num), Field.Store.YES, Field.Index.NOT_ANALYZED));
      if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
    	            // New index, so we just add the document (no old document can be there):
                System.out.println("adding ");
    	            writer.addDocument(doc);
    	          } else {
    	            // Existing index (an old copy of this document may have been indexed) so 
    	            // we use updateDocument instead to replace the old one matching the exact 
    	           // path, if present:
    	            System.out.println("updating ");
    	            writer.updateDocument(new Term("num",new BytesRef(Integer.toString(num).getBytes())), doc);
    	       }
      }
}