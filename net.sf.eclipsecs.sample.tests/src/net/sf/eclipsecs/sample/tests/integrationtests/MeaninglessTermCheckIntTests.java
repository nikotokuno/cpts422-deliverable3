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
@PrepareForTest({ MeaninglessTermCheck.class, SpellingCheck.class, DetailAST.class, Util.class })
public class MeaninglessTermCheckIntTests 
{
	/**
	 * Full mock of visitToken
	 */
	@Test
	public void visitToken_noMeaninglessTerms_fullMock()
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

	/**
	 * getMeaninlessTerms not mocked, Util.readResource is
	 */
	@Test
	public void visitToken_noMeaninglessTerms_getMeaninlessTerms_NotMocked()
	{
		DetailAST dastMock1 = PowerMock.createMock(DetailAST.class);
		DetailAST dastMock2 = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(dastMock1.findFirstToken(TokenTypes.IDENT)).andStubReturn(dastMock2);
		EasyMock.expect(dastMock2.getText()).andReturn("testString");

		PowerMock.mockStatic(Util.class);
		List<String> meaninglessTerms = new ArrayList<String>(Arrays.asList("foo", "bar" ));
		EasyMock.expect(Util.readResource(MeaninglessTermCheck.class, "MetasyntacticVariables")).andReturn(meaninglessTerms);
		
		MeaninglessTermCheck MeaninglessTermCheckMock = new MeaninglessTermCheck();

		PowerMock.replayAll();
		
		MeaninglessTermCheckMock.init();
		MeaninglessTermCheckMock.visitToken(dastMock1);
		
		PowerMock.verifyAll();
	}

	/**
	 * getMeaninlessTerms and Util.readResource not mocked
	 */
	@Test
	public void visitToken_noMeaninglessTerms_getMeaninlessTerms_util_NotMocked()
	{
		DetailAST dastMock1 = PowerMock.createMock(DetailAST.class);
		DetailAST dastMock2 = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(dastMock1.findFirstToken(TokenTypes.IDENT)).andStubReturn(dastMock2);
		EasyMock.expect(dastMock2.getText()).andReturn("notInMeaninglessTerms");

		MeaninglessTermCheck MeaninglessTermCheckMock = new MeaninglessTermCheck();

		PowerMock.replayAll();

		MeaninglessTermCheckMock.init();
		MeaninglessTermCheckMock.visitToken(dastMock1);
		
		PowerMock.verifyAll();
	}

	/**
	 * Full mock of visitToken, has meaningless terms
	 */
	@Test
	public void visitToken_hasMeaninglessTerms_fullMock()
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
	
	/**
	 * getMeaninlessTerms not mocked, Util.readResource is
	 */
	@Test
	public void visitToken_hasMeaninglessTerms_getMeaninlessTerms_NotMocked()
	{
		DetailAST dastMock1 = PowerMock.createMock(DetailAST.class);
		DetailAST dastMock2 = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(dastMock1.getLineNo()).andReturn(42);
		EasyMock.expect(dastMock1.findFirstToken(TokenTypes.IDENT)).andStubReturn(dastMock2);
		EasyMock.expect(dastMock2.getText()).andReturn("bar"); // bar is bad term

		PowerMock.mockStatic(Util.class);
		List<String> meaninglessTerms = new ArrayList<String>(Arrays.asList("foo", "bar" ));
		EasyMock.expect(Util.readResource(MeaninglessTermCheck.class, "MetasyntacticVariables")).andReturn(meaninglessTerms);
		
		MeaninglessTermCheck MeaninglessTermCheckMock = PowerMock.createPartialMock(MeaninglessTermCheck.class, "log");

		//HashSet<String> MeaninglessTerms = new HashSet<String>(Arrays.asList("badterm"));
		//EasyMock.expect(MeaninglessTermCheckMock.getMeaninglessTerms()).andReturn(MeaninglessTerms);

		MeaninglessTermCheckMock.log(42, "meaninglessterm", "bar");
		PowerMock.expectLastCall();
		
		PowerMock.replayAll();

		MeaninglessTermCheckMock.init();
		MeaninglessTermCheckMock.visitToken(dastMock1);
		
		PowerMock.verifyAll();
	}

	/**
	 * getMeaninlessTerms and Util.readResource not mocked
	 */
	@Test
	public void visitToken_hasMeaninglessTerms_getMeaninlessTerms_util_NotMocked()
	{
		DetailAST dastMock1 = PowerMock.createMock(DetailAST.class);
		DetailAST dastMock2 = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(dastMock1.getLineNo()).andReturn(42);
		EasyMock.expect(dastMock1.findFirstToken(TokenTypes.IDENT)).andStubReturn(dastMock2);
		EasyMock.expect(dastMock2.getText()).andReturn("foobar"); // foobar is term in "MetasyntacticVariables"
		
		MeaninglessTermCheck MeaninglessTermCheckMock = PowerMock.createPartialMock(MeaninglessTermCheck.class, "log");

		MeaninglessTermCheckMock.log(42, "meaninglessterm", "foobar");
		PowerMock.expectLastCall();
		
		PowerMock.replayAll();

		MeaninglessTermCheckMock.init();
		MeaninglessTermCheckMock.visitToken(dastMock1);
		
		PowerMock.verifyAll();
	}

}
