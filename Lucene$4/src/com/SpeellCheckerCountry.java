package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import javax.management.Query;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spell.HighFrequencyDictionary;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.suggest.FileDictionary;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SpeellCheckerCountry {
	public static void main(String[] args) throws Exception {
	    Directory directory=FSDirectory.open(new File("/opt/indexes/AtoZWord")); // open the directory for write the index
	    SpellChecker spell = new SpellChecker(directory); // create the instance of spellcheker 
	   /* ---------------------------------------------------------------
	    this code for Creting the index from plain text file
	    
	    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, null);
	    File dictionary = new File("/opt/dev/MyeclipsWork/Lucene$4/src/resources/country.txt");
	    spell.indexDictionary(new PlainTextDictionary(dictionary), config, true);
	  //  new HighFrequencyDictionary(reader, field, thresh);// this is high frequency read the data from index
	   ---------------------------------------------------------------------- */
	 // --------------------------------------------------------------  
	   /* Here We create the index from file reader
	    
	    File in =new File("/opt/dev/MyeclipsWork/Lucene$4/src/resources/country.txt");//file for indexing
	    FileReader dictFile =new FileReader(in);
	    BufferedReader reader =new BufferedReader(dictFile);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, null);
	    spell.indexDictionary(new FileDictionary(reader), config, true);*/ //
	 //----------------------------------------------------------------------------------   
	    
	    //----------------------------------------------------------------------------
// Here We Create the Index from the files
	  /*  
	    File directoryread = new File("/opt/dev/MyeclipsWork/Lucene$4/LF Delimited Format/");      
	    File[] myarray;  
	    myarray=new File[10];
	    myarray=directoryread.listFiles();
	    for (int j = 0; j < myarray.length; j++)
	    {
	           File path=myarray[j];
	           FileReader fr = new FileReader(path);
	           BufferedReader br = new BufferedReader(fr);
	           IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, null);
	   	       spell.indexDictionary(new FileDictionary(br), config, true); //
	   	       System.out.println("file index is ="+myarray[j]);
	   	       fr.close();
	   	       br.close();
	    }
	    */
  //End for createing the index From Directory all files
////-----------------------------------------------------------------------------------------------
	    
	    
	    // from Here Read the data from index
	    String query = "Ira"; //kne, console
	    System.out.println("The Query was: "+query);
	    
	        String[] suggestions = spell.suggestSimilar(query, 10);
	        if (suggestions != null) {
	        	for (String string : suggestions) {
	        		System.out.println("did you mean This result ="+string);
				}
	        }
	    directory.close();
	}
//END Get The AutoSuggest data
	
	}

