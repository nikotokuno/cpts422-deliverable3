package net.sf.eclipsecs.sample.checks;

import java.util.*;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Checks that words are spelled correctly.
 * Variables are assumed to be in camelCase for parsing. <br/>
 * Words list from: https://raw.githubusercontent.com/dwyl/english-words/master/words_alpha.txt
 * @author Wesley Thorsen 11397880
 */
public class SpellingCheck extends AbstractCheck 
{
	private HashSet<String> EnglishWords; 

	@Override
	public void init()
	{
		super.init();
		this.EnglishWords = new HashSet<String>(Util.readResource(SpellingCheck.class, "EnglishWords"));
	}
	
	public HashSet<String> getEnglishWords()
	{
		return EnglishWords;
	}
	
    @Override
    public int[] getDefaultTokens() 
    {
        return new int[] 
		{ 
    		TokenTypes.CLASS_DEF, 
    		TokenTypes.INTERFACE_DEF,
    		TokenTypes.METHOD_DEF, 
    		TokenTypes.VARIABLE_DEF 
		};
    }
        
    @Override
    public void visitToken(DetailAST ast) 
    {
    	String name = ast.findFirstToken(TokenTypes.IDENT).getText();
    	
    	// Parse the name into tokens (assuming name is camelCase)
    	String[] nameParts = Util.tokenizeCamelCase(name);
    	
    	for (String term : nameParts)
    	{
    		// Ignore term if its all uppercase (abbreviation)
    		if (term.toUpperCase() == term) continue;
    		
    		if (!this.checkSpelling(term))
    		{
                log(ast.getLineNo(), "spellingmistake", term);
    			break;
    		}
    	}
    }
    
    public Boolean checkSpelling(String word)
    {
    	return this.getEnglishWords().contains(word.toLowerCase());
    }
}