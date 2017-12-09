package net.sf.eclipsecs.sample.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class WholePartCheck extends AbstractCheck{
	
    @Override
    public int[] getDefaultTokens() {
    	// Call visitToken every time a method or variable is defined
        return new int[] { TokenTypes.METHOD_DEF, TokenTypes.VARIABLE_DEF };
    }
    
    @Override
    public void visitToken(DetailAST ast) {
    	String className = getClassIdentName(ast);
    	
    	// Get the token that holds the identifier and its text
    	DetailAST identBlock = ast.findFirstToken(TokenTypes.IDENT);
    	String identifierName = identBlock.getText();
    	
    	// Get the token that holds the type and its text
    	DetailAST typeBlock = ast.findFirstToken(TokenTypes.TYPE);
    	DetailAST typeNameBlock = typeBlock.getFirstChild();
    	String typeName = typeNameBlock.getText();
    	
    	// Check if class name equals inner identifier name and type name does not equal the parent class name
    	if (className.equalsIgnoreCase(identifierName) && !typeName.equalsIgnoreCase(className)) {
    		// Log the check failure
    		log(ast.getLineNo(), "wholepart");
    	}
    }
    
    public String getClassIdentName(DetailAST ast) {
    	// Grab the parent of the original ast
    	DetailAST classBlock = ast.getParent();
    	
    	// Keep grabbing parent until the classBlock is a class definition or interface definition
    	while (classBlock.getType() != TokenTypes.CLASS_DEF && classBlock.getType() != TokenTypes.INTERFACE_DEF) {
    		classBlock = classBlock.getParent();
    	}
    	
    	// Grab identifier of class
    	DetailAST identBlock = classBlock.findFirstToken(TokenTypes.IDENT);
    	String className = identBlock.getText();
    	
    	return className;
    }
}
