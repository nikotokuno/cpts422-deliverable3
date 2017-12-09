package net.sf.eclipsecs.sample.tests.integrationtests;

import net.sf.eclipsecs.sample.checks.*;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ UselessTypeCheck.class, WholePartCheck.class, DetailAST.class })
public class WholePartIntTests {
	@Test
	public void visitToken_getClassName_fail_fullMock() {
		// Test #1
		// Check should fail since identifier has same name as parent class name
		
		DetailAST astMock = PowerMock.createMock(DetailAST.class);
		DetailAST identBlockMock = PowerMock.createMock(DetailAST.class);
		DetailAST typeBlockMock = PowerMock.createMock(DetailAST.class);
		DetailAST typeNameBlockMock = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(astMock.findFirstToken(TokenTypes.IDENT)).andStubReturn(identBlockMock);
		EasyMock.expect(astMock.findFirstToken(TokenTypes.TYPE)).andStubReturn(typeBlockMock);
		EasyMock.expect(astMock.getLineNo()).andReturn(1);
		
		EasyMock.expect(identBlockMock.getText()).andReturn("tester");
		EasyMock.expect(typeBlockMock.getFirstChild()).andStubReturn(typeNameBlockMock);
		EasyMock.expect(typeNameBlockMock.getText()).andReturn("int");
		
		WholePartCheck testCheck = PowerMock.createPartialMock(WholePartCheck.class, "getClassIdentName", "log");
		
		EasyMock.expect(testCheck.getClassIdentName(astMock)).andReturn("Tester");
		
		testCheck.log(1, "wholepart");
		
		PowerMock.replayAll();
		
		testCheck.visitToken(astMock);
		
		PowerMock.verifyAll();
	}
	
	@Test
	public void visitToken_getClassName_pass_fullMock() {
		// Test #1
		// Check should pass since identifier has different name than parent class name
		
/*		DetailAST astMock = PowerMock.createMock(DetailAST.class);
		DetailAST identBlockMock = PowerMock.createMock(DetailAST.class);
		DetailAST typeBlockMock = PowerMock.createMock(DetailAST.class);
		DetailAST typeNameBlockMock = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(astMock.findFirstToken(TokenTypes.IDENT)).andStubReturn(identBlockMock);
		EasyMock.expect(astMock.findFirstToken(TokenTypes.TYPE)).andStubReturn(typeBlockMock);
		EasyMock.expect(astMock.getLineNo()).andReturn(1);
		
		EasyMock.expect(identBlockMock.getText()).andReturn("index");
		EasyMock.expect(typeBlockMock.getFirstChild()).andStubReturn(typeNameBlockMock);
		EasyMock.expect(typeNameBlockMock.getText()).andReturn("int");
		
		WholePartCheck testCheck = PowerMock.createPartialMock(WholePartCheck.class, "getClassIdentName", "log");
		
		EasyMock.expect(testCheck.getClassIdentName(astMock)).andReturn("Tester");
		
		testCheck.log(1, "wholepart");
		
		PowerMock.replayAll();
		
		testCheck.visitToken(astMock);
		
		PowerMock.verifyAll();*/
	}
}
