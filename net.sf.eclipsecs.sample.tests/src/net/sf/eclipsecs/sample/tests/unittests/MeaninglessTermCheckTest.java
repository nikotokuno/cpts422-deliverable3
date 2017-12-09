package net.sf.eclipsecs.sample.tests.unittests;

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
@PrepareForTest({ MeaninglessTermCheck.class, DetailAST.class })
public class MeaninglessTermCheckTest 
{
	@Test
	public void visitToken_noMeaninglessTerms()
	{
		DetailAST dastMock1 = PowerMock.createMock(DetailAST.class);
		DetailAST dastMock2 = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(dastMock1.findFirstToken(TokenTypes.IDENT)).andStubReturn(dastMock2);
		EasyMock.expect(dastMock2.getText()).andReturn("testString");
		
		MeaninglessTermCheck MeaninglessTermCheckMock = PowerMock.createPartialMock(MeaninglessTermCheck.class, "getMeaninglessTerms");

		HashSet<String> MeaninglessTerms = new HashSet<String>(Arrays.asList("foo"));
		EasyMock.expect(MeaninglessTermCheckMock.getMeaninglessTerms()).andReturn(MeaninglessTerms);
		
		PowerMock.replayAll();
		
		MeaninglessTermCheckMock.visitToken(dastMock1);
		
		PowerMock.verifyAll();
	}

	@Test
	public void visitToken_hasMeaninglessTerms()
	{
		DetailAST dastMock1 = PowerMock.createMock(DetailAST.class);
		DetailAST dastMock2 = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(dastMock1.getLineNo()).andReturn(42);
		EasyMock.expect(dastMock1.findFirstToken(TokenTypes.IDENT)).andStubReturn(dastMock2);
		EasyMock.expect(dastMock2.getText()).andReturn("badterm");
		
		MeaninglessTermCheck MeaninglessTermCheckMock = PowerMock.createPartialMock(MeaninglessTermCheck.class, "getMeaninglessTerms", "log");

		HashSet<String> MeaninglessTerms = new HashSet<String>(Arrays.asList("badterm"));
		EasyMock.expect(MeaninglessTermCheckMock.getMeaninglessTerms()).andReturn(MeaninglessTerms);

		MeaninglessTermCheckMock.log(42, "meaninglessterm", "badterm");
		PowerMock.expectLastCall();
		
		PowerMock.replayAll();
		
		MeaninglessTermCheckMock.visitToken(dastMock1);
		
		PowerMock.verifyAll();
	}
}
