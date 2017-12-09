package net.sf.eclipsecs.sample.tests.unittests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import net.sf.eclipsecs.sample.checks.*;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.*;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;

import java.util.*;
import java.util.Arrays;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UselessTypeCheck.class, DetailAST.class})
public class UselessTypeUnitTest {
	
	@Test
	public void testGetDefaultTokens() {
		UselessTypeCheck testCheck = new UselessTypeCheck();
		int[] correctValues = {TokenTypes.VARIABLE_DEF};
		
		assertArrayEquals(testCheck.getDefaultTokens(), correctValues);
	}
	
	@Test
	public void testVisitToken()
	{
		//Test #1
		// Check should fail since identifier name = int_test and type name = int
		
		DetailAST astMock = PowerMock.createMock(DetailAST.class);
		UselessTypeCheck testCheck = PowerMock.createPartialMock(UselessTypeCheck.class, "getVariableTypeName", "getVariableIdentName", "log");
		
		EasyMock.expect(astMock.getLineNo()).andReturn(1);
		
		EasyMock.expect(testCheck.getVariableTypeName(astMock)).andReturn("int");
		EasyMock.expect(testCheck.getVariableIdentName(astMock)).andReturn("int_test");
		
		testCheck.log(1, "uselesstype");
		PowerMock.expectLastCall();
		
		PowerMock.replayAll();
		
		testCheck.visitToken(astMock);
		
		PowerMock.verifyAll();
		
		// Test #2
		// Check should not fail since identifier name = test and type name = int
		
		astMock = PowerMock.createMock(DetailAST.class);
		testCheck = PowerMock.createPartialMock(UselessTypeCheck.class, "getVariableTypeName", "getVariableIdentName");
				
		EasyMock.expect(testCheck.getVariableTypeName(astMock)).andReturn("int");
		EasyMock.expect(testCheck.getVariableIdentName(astMock)).andReturn("test");
		
		PowerMock.replayAll();
		
		testCheck.visitToken(astMock);
		
		PowerMock.verifyAll();
	}
	
	@Test
	public void testGetVariableTypeName() {
		// Test #1
		// Makes sure type returned is int
		
		DetailAST astMock = PowerMock.createMock(DetailAST.class);
		DetailAST typeBlockAstMock = PowerMock.createMock(DetailAST.class);
		DetailAST typeMock = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(astMock.findFirstToken(TokenTypes.TYPE)).andStubReturn(typeBlockAstMock);
		EasyMock.expect(typeBlockAstMock.getFirstChild()).andStubReturn(typeMock);
		EasyMock.expect(typeMock.getText()).andReturn("int");
		
		UselessTypeCheck uselessTypeCheck = new UselessTypeCheck();
		
		PowerMock.replayAll();
		
		assertEquals(uselessTypeCheck.getVariableTypeName(astMock), "int");
		
		PowerMock.verifyAll();
		
		// Test #2
		// Makes sure type returned is String
		
		astMock = PowerMock.createMock(DetailAST.class);
		typeBlockAstMock = PowerMock.createMock(DetailAST.class);
		typeMock = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(astMock.findFirstToken(TokenTypes.TYPE)).andStubReturn(typeBlockAstMock);
		EasyMock.expect(typeBlockAstMock.getFirstChild()).andStubReturn(typeMock);
		EasyMock.expect(typeMock.getText()).andReturn("String");
		
		PowerMock.replayAll();
		
		assertEquals(uselessTypeCheck.getVariableTypeName(astMock), "String");
		
		PowerMock.verifyAll();
	}
	
	@Test
	public void testGetVariableIdentName() {
		DetailAST astMock = PowerMock.createMock(DetailAST.class);
		DetailAST identBlockMock = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(astMock.findFirstToken(TokenTypes.IDENT)).andStubReturn(identBlockMock);
		EasyMock.expect(identBlockMock.getText()).andReturn("test_int");
		
		UselessTypeCheck uselessTypeCheck = new UselessTypeCheck();
		
		PowerMock.replayAll();
		
		assertEquals(uselessTypeCheck.getVariableIdentName(astMock), "test_int");
		
		PowerMock.verifyAll();
		
		// Test #2
		// Checks if identifier name is equal to test_string
		
		astMock = PowerMock.createMock(DetailAST.class);
		identBlockMock = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(astMock.findFirstToken(TokenTypes.IDENT)).andStubReturn(identBlockMock);
		EasyMock.expect(identBlockMock.getText()).andReturn("test_string");
				
		PowerMock.replayAll();
		
		assertEquals(uselessTypeCheck.getVariableIdentName(astMock), "test_string");
		
		PowerMock.verifyAll();
	}
}

