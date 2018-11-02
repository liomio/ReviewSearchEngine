package edu.virginia.cs.analyzer;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import edu.virginia.cs.index.Stopwords;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.ext.porterStemmer;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

public class TextAnalyzer {
	
	//Maximum Entropy model based tokenizer
	Tokenizer m_tokenizer;
	
	//A list of stopwords have been defined in edu.virginia.cs.index.Stopwords
	
	
	public TextAnalyzer(String tokenizerModel) throws InvalidFormatException, FileNotFoundException, IOException {
		m_tokenizer = new TokenizerME(new TokenizerModel(new FileInputStream(tokenizerModel)));
	}
	
	//this method illustrates how to perform tokenization, normlaization and stemming
	public String[] tokenize(String text) {
		//System.out.format("Token\tNormalization\tSnonball Stemmer\tPorter Stemmer\n");
		String[] tokens = m_tokenizer.tokenize(text);
		ArrayList<String> processedTerms = new ArrayList<String>();
		Stopwords stpw = new Stopwords();
		
		for(String token:tokens) {
			//System.out.format("%s\t%s\t%s\t%s\n", token, normalize(token), snowballStemming(token), porterStemming(token));
			/**
			 * INSTRUCTOR'S NOTE: perform necessary text processing here, e.g., stemming, normalization and stopword removal
			 */
			// call respective function
			if (token!=null && token.length()>0) {
				
				token = normalize(token);
				if (token!=null && !token.equals("")) {
				token = snowballStemming(token);
				if (token!=null && !token.equals("")) {
					// stopwords removal 
					if (!Arrays.asList(stpw.STOPWORDS).contains(token) ) {
					System.out.println(token);
				
					processedTerms.add(token);
					
					}
				}
				}
			}
		}
		
		return processedTerms.toArray(new String[processedTerms.size()]);//a list of processed terms
	}
	
	//sample code for demonstrating how to perform text normalization
	String normalize(String token) {
		// remove all non-word characters
		/**
		 * INSTRUCTOR'S NOTE
		 * please change this to remove all English punctuation
		 */
		if (token.matches("\\p{Punct}")) {
			return "";
		}
		token = token.replaceAll("\\W+", ""); 
		
		// convert to lower case
		token = token.toLowerCase(); 
		
		/**
		 * INSTRUCTOR'S NOTE
		 * add a line to recognize integers and doubles via regular expression
		 * and convert the recognized integers and doubles to a special symbol "NUM"
		 */
		if (token.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")) {
			//System.out.println(token);
			token = "NUM";
		}
		/**
		 * INSTRUCTOR'S NOTE
		 * after reading your constructed controlled vocabulary, can you come up with other 
		 * normaliazation rules to further refine the selected terms?
		 */
		// to check for token length and remove those that are <= 1 (single char)
		if (token.length()<=1) {
			return "";
		}
		return token;
	}
	
	//sample code for demonstrating how to use Snowball stemmer
	String snowballStemming(String token) {
		SnowballStemmer stemmer = new englishStemmer();
		stemmer.setCurrent(token);
		if (stemmer.stem())
			return stemmer.getCurrent();
		else
			return token;
	}
	
	//sample code for demonstrating how to use Porter stemmer
	String porterStemming(String token) {
		porterStemmer stemmer = new porterStemmer();
		stemmer.setCurrent(token);
		if (stemmer.stem())
			return stemmer.getCurrent();
		else
			return token;
	}
}
