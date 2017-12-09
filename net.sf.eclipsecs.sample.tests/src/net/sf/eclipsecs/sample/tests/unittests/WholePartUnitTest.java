package net.sf.eclipsecs.sample.tests.unittests;

import org.junit.Test;
import org.junit.runner.RunWith;
import net.sf.eclipsecs.sample.checks.*;

import static org.junit.Assert.*;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WholePartCheck.class, DetailAST.class})
public class WholePartUnitTest {
	@Test
	public void testGetDefaultTokens() {
		WholePartCheck testCheck = new WholePartCheck();
		int[] correctValues = {TokenTypes.METHOD_DEF, TokenTypes.VARIABLE_DEF};
		
		assertArrayEquals(testCheck.getDefaultTokens(), correctValues);
	}
	
	@Test
	public void testVisitToken() {
		// Test #1
		// Check should fail since identifier has same name as parent class name
		
		// Mock all of the detailAST in whole part visit token
		DetailAST astMock = PowerMock.createMock(DetailAST.class);
		DetailAST identBlockMock = PowerMock.createMock(DetailAST.class);
		DetailAST typeBlockMock = PowerMock.createMock(DetailAST.class);
		DetailAST typeNameBlockMock = PowerMock.createMock(DetailAST.class);
		
		// Mock the detailAST behaviors based on visitToken
		EasyMock.expect(astMock.findFirstToken(TokenTypes.IDENT)).andStubReturn(identBlockMock);
		EasyMock.expect(astMock.findFirstToken(TokenTypes.TYPE)).andStubReturn(typeBlockMock);
		EasyMock.expect(astMock.getLineNo()).andReturn(1);
		
		EasyMock.expect(identBlockMock.getText()).andReturn("tester");
		EasyMock.expect(typeBlockMock.getFirstChild()).andStubReturn(typeNameBlockMock);
		EasyMock.expect(typeNameBlockMock.getText()).andReturn("int");
		
		// Mock the getClassIdentName and log method
		WholePartCheck testCheck = PowerMock.createPartialMock(WholePartCheck.class, "getClassIdentName", "log");
		
		// Return Tester as mocked class identifier
		EasyMock.expect(testCheck.getClassIdentName(astMock)).andReturn("Tester");
		
		// Expected log, line number 1 and wholepart message
		testCheck.log(1, "wholepart");
		
		PowerMock.replayAll();
		
		testCheck.visitToken(astMock);
		
		PowerMock.verifyAll();
		
		// Test #2
		// Check should pass and log should not be called
		
		astMock = PowerMock.createMock(DetailAST.class);
		identBlockMock = PowerMock.createMock(DetailAST.class);
		typeBlockMock = PowerMock.createMock(DetailAST.class);
		typeNameBlockMock = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(astMock.findFirstToken(TokenTypes.IDENT)).andStubReturn(identBlockMock);
		EasyMock.expect(astMock.findFirstToken(TokenTypes.TYPE)).andStubReturn(typeBlockMock);
		
		EasyMock.expect(identBlockMock.getText()).andReturn("tester");
		EasyMock.expect(typeBlockMock.getFirstChild()).andStubReturn(typeNameBlockMock);
		EasyMock.expect(typeNameBlockMock.getText()).andReturn("int");
		
	    testCheck = PowerMock.createPartialMock(WholePartCheck.class, "getClassIdentName");
		
		EasyMock.expect(testCheck.getClassIdentName(astMock)).andReturn("test_ident");
				
		PowerMock.replayAll();
		
		testCheck.visitToken(astMock);
		
		PowerMock.verifyAll();
	}
	
	@Test
	public void testGetClassIdentName() {
		// Test #1
		// Fails if class name returned is not Tester
		
		// Mock ASTs, use power mock since DetailAST is final
		DetailAST classBlockMock = PowerMock.createMock(DetailAST.class);
		DetailAST astMock = PowerMock.createMock(DetailAST.class);
		DetailAST identBlockMock = PowerMock.createMock(DetailAST.class);
		
		// Return class stub as parent
		EasyMock.expect(astMock.getParent()).andStubReturn(classBlockMock);
		
		// Is of type CLASS_DEF and return identifier block stub as inner token
		EasyMock.expect(classBlockMock.getType()).andReturn(TokenTypes.CLASS_DEF);
		EasyMock.expect(classBlockMock.findFirstToken(TokenTypes.IDENT)).andStubReturn(identBlockMock);
		
		// Mock return Tester as class name
		EasyMock.expect(identBlockMock.getText()).andReturn("Tester");
		
		WholePartCheck testCheck = new WholePartCheck();
		PowerMock.replayAll();
		
		// Ensure class name is same as one planted
		assertEquals(testCheck.getClassIdentName(astMock), "Tester");

		PowerMock.verifyAll();
		
		// Test #2
		// Makes sure class name returned is equal to Tester2
		
		classBlockMock = PowerMock.createMock(DetailAST.class);
		astMock = PowerMock.createMock(DetailAST.class);
		identBlockMock = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(astMock.getParent()).andStubReturn(classBlockMock);
		
		EasyMock.expect(classBlockMock.getType()).andReturn(TokenTypes.CLASS_DEF);
		EasyMock.expect(classBlockMock.findFirstToken(TokenTypes.IDENT)).andStubReturn(identBlockMock);
		
		EasyMock.expect(identBlockMock.getText()).andReturn("Tester2");
		
		PowerMock.replayAll();
		
		assertEquals(testCheck.getClassIdentName(astMock), "Tester2");

		PowerMock.verifyAll();
		}
}
