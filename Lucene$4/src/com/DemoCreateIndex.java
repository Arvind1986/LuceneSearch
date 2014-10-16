package com;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.demo.IndexFiles;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class DemoCreateIndex {
	public static Document indexHotel(Hotel hotel) throws IOException {
	    Document doc = new Document();
	    //Here if use the field.store then whole term is store in index. if use field.index.Analyzed then text is tokenized and store seperate word in index 
	    doc.add(new Field("name", hotel.getName(),Field.Store.YES,Field.Index.ANALYZED));// Here whole world is store in index
	    doc.add(new Field("city", hotel.getCity(),Field.Store.YES,Field.Index.ANALYZED));
	    doc.add(new Field("description", hotel.getDescription(),Field.Store.YES,Field.Index.ANALYZED));
	    return doc;
	}
public static void main(String[] args) throws IOException {
	Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
    Directory directory=FSDirectory.open(new File(ConstantUtil.INDEX_LOC));
    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
    IndexWriter iwriter = new IndexWriter(directory, config);
   /* Document doc = new Document();
    String text = "java jsp servlet spring struts2 jsf";
    doc.add(new Field("subject", text, TextField.TYPE_STORED));*/
    Hotel hotel1 =new Hotel("JAVA", "TEch", "Secure language in World");
    Hotel hotel2 =new Hotel("php", "Client site language", " This is unsecure language in World for programming purpose");
    Hotel hotel3 =new Hotel(".net", "also use php for purpose php", " When commit starts, it starts blocking all indexing threads");
    Hotel hotel4 =new Hotel("utter pradesh This is unsecure language in World for programming purpose", "kanpur", "Last but not least, As per GC report in our test run for 17GB");
    Hotel hotel5 =new Hotel("central state", "delhi", "We decreased the RAMBufferSize of each core by 100 to 200 MB");
    Hotel hotel6 =new Hotel("ncr Java This is unsecure language in java  World for programming purpose JAVA region", "noida", "Stack Overflow is a question and answer site for professional and enthusiast programmers");
    Hotel hotel7 =new Hotel("jhansi", "demo city", "mentioned in the post i followed. moreover in another code block :");
    Hotel hote8 =new Hotel("JAVA and region", "this is jannsi test city for JAva", "core technology for do is and orthis progra");
    
    Hotel[] hotels ={hotel1,hotel2,hotel3,hotel4,hotel5,hotel6,hotel7,hote8};
    Document doc = new Document();
    Field field=new  Field("f", "first ends",Field.Store.YES,Field.Index.ANALYZED,Field.TermVector.YES);
    Field field1=new  Field("f", "second start",Field.Store.YES,Field.Index.ANALYZED,Field.TermVector.YES);
    doc.add(field);
    doc.add(field1);
    iwriter.addDocument(doc);
   List<IndexableField>  files=   doc.getFields();
   for (Iterator iterator = files.iterator(); iterator.hasNext();) {
	IndexableField indexableField = (IndexableField) iterator.next();
	System.out.println(indexableField.name());
	System.out.println(indexableField.stringValue());
	
}
    for(int i=0; i<hotels.length;i++){
        iwriter.addDocument(indexHotel(hotels[i]));
    }
    
    System.out.println("All is Done");
    iwriter.close();
    directory.close();
}
}
