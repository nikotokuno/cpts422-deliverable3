package net.sf.eclipsecs.sample.checks;

import java.util.*;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Checks for meaningless terms as defined by: http://selab.fbk.eu/LexiconBadSmellWiki/index.php?ugly_terms <br/>
 * Meaningless term lexicon created from: http://www.catb.org/jargon/html/M/metasyntactic-variable.html
 * @author Wesley Thorsen 11397880
 */
public class MeaninglessTermCheck extends AbstractCheck 
{
	private static HashSet<String> MeaninglessTerms;

	@Override
	public void init()
	{
		super.init();
		this.MeaninglessTerms = new HashSet<String>(Util.readResource(MeaninglessTermCheck.class, "MetasyntacticVariables"));
	}
	
	public HashSet<String> getMeaninglessTerms()
	{
		return MeaninglessTerms;
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
    	if (this.getMeaninglessTerms().contains(name))
    	{
            log(ast.getLineNo(), "meaninglessterm", name);
    	}
    }
}