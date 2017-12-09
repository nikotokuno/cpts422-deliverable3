package net.sf.eclipsecs.sample.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class UselessTypeCheck extends AbstractCheck {
	
    @Override
    public int[] getDefaultTokens() {
    	// Call visit token every time a variable is defined
        return new int[] { TokenTypes.VARIABLE_DEF };
    }
    
    @Override
    public void visitToken(DetailAST ast) {
    	String typeName = getVariableTypeName(ast);
    	
    	String identName = getVariableIdentName(ast);

        // Check if the identifier name contains the type name, thus displaying useless type error
        if (identName.contains(typeName)) {
        	// Log the error as useless type
        	log(ast.getLineNo(), "uselesstype");
        }
    }
    
    public String getVariableTypeName(DetailAST ast) {
    	// Find the first token that is of type 'Type' and grab the type name
    	DetailAST typeBlock = ast.findFirstToken(TokenTypes.TYPE);
    	DetailAST typeChildBlock = typeBlock.getFirstChild();
    	String typeName = typeChildBlock.getText();
    	
    	return typeName;
    }
    
    public String getVariableIdentName(DetailAST ast) {
    	// Find the identifier for the variable definition and grab the identifier text
        DetailAST identBlock = ast.findFirstToken(TokenTypes.IDENT);
        String identName = identBlock.getText();
        
        return identName;
    }
}
