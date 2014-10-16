package com;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SpellCheckerDemo {
public static void main(String[] args) throws Exception {
	Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
	// directory for reading and write the data
    Directory directory=FSDirectory.open(new File("/opt/indexes/demotest"));
    Directory directory1=FSDirectory.open(new File("/opt/example/indexes"));
   
      // This is use for write the data 
      IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
      SpellChecker spellChecker1 = new SpellChecker(directory); 
      //we write the data in single directory from file
      spellChecker1.indexDictionary(new PlainTextDictionary(new File("/opt/dev/MyeclipsWork/Lucene$4/src/resources/fulldictionary00.txt")),config,true);
      
      // Here We write the data in multiple indexes directory
      IndexReader spellReader = IndexReader.open(directory);
      IndexReader spellReader2 = IndexReader.open(directory1);
      MultiReader multiReader = new MultiReader(new IndexReader[]{spellReader,spellReader2});
      // To index a field of a user data:
      LuceneDictionary luceneDictionary = new LuceneDictionary(multiReader,"content");
      /* We can add the words coming from a Lucene Index (more precisely from a set of Lucene fields), and from a text file with a list of words.
      Example: we can add all the keywords of a given Lucene field of my index. */
      spellChecker1.indexDictionary(luceneDictionary,config,true);
      // from we create the index  data from file in to multiple directory
      // ENd write the data from multiple indexes directory

      
     // ---------------------------------------------------------------------------
      // From Here We read the data from directory
        String wordForSuggestions = "abandan";
        int suggestionsNumber = 50; // number of list for display the suggession word
        // get the suggest list from the index
        String[] suggestions = spellChecker1.suggestSimilar(wordForSuggestions, suggestionsNumber);

        if (suggestions!=null && suggestions.length>0) {
            for (String word : suggestions) {
                System.out.println("Did you mean:" + word);
            }
        }
        else {
            System.out.println("No suggestions found for word:"+wordForSuggestions);
        }
        spellChecker1.close();
        directory.close();
        
    }

}
