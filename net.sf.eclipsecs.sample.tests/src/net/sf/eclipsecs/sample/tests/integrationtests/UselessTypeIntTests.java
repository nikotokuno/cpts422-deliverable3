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
@PrepareForTest({ UselessTypeCheck.class, DetailAST.class })
public class UselessTypeIntTests {
	// Bottom up integration testing

	@Test
	public void visitToken_getVariableTypeName_getVariableIdentName_fail_fullMocked() {
		// Should fail check
		
/*		DetailAST astMock = PowerMock.createMock(DetailAST.class);
		UselessTypeCheck testCheck = PowerMock.createPartialMock(UselessTypeCheck.class, "getVariableTypeName", "getVariableIdentName", "log");
		
		EasyMock.expect(astMock.getLineNo()).andReturn(1);
		
		EasyMock.expect(testCheck.getVariableTypeName(astMock)).andReturn("int");
		EasyMock.expect(testCheck.getVariableIdentName(astMock)).andReturn("int_test");
		
		testCheck.log(1, "uselesstype");
		PowerMock.expectLastCall();
		
		PowerMock.replayAll();
		
		testCheck.visitToken(astMock);
		
		PowerMock.verifyAll();*/
	}
	
	@Test
	public void visitToken_getVariableTypeName_getVariableIdentName_pass_mocked() {
		// Should pass check
		
/*		DetailAST astMock = PowerMock.createMock(DetailAST.class);
		UselessTypeCheck testCheck = PowerMock.createPartialMock(UselessTypeCheck.class, "getVariableTypeName", "log");
		
		EasyMock.expect(astMock.getLineNo()).andReturn(1);
		
		EasyMock.expect(testCheck.getVariableTypeName(astMock)).andReturn("int");
		EasyMock.expect(testCheck.getVariableIdentName(astMock)).andReturn("test");
						
		testCheck.log(1, "uselesstype");
		PowerMock.expectLastCall();
		
		PowerMock.replayAll();
		
		testCheck.visitToken(astMock);
		
		PowerMock.verifyAll();*/
	}
}
