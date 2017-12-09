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
public class SpellingCheckIntTests 
{
	/**
	 * full mock of visit token with correct spelling
	 */
	@Test
	public void visitToken_spellingCorrect_fullMock()
	{
		DetailAST dastMock1 = PowerMock.createMock(DetailAST.class);
		DetailAST dastMock2 = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(dastMock1.findFirstToken(TokenTypes.IDENT)).andStubReturn(dastMock2);
		EasyMock.expect(dastMock2.getText()).andReturn("testString");
		
		PowerMock.mockStatic(Util.class);
		String[] tokenArray = new String[] { "test", "String" };
		EasyMock.expect(Util.tokenizeCamelCase("testString")).andReturn(tokenArray);
		
		SpellingCheck spellingCheckMock = PowerMock.createPartialMock(SpellingCheck.class, "checkSpelling");
		
		EasyMock.expect(spellingCheckMock.checkSpelling("test")).andReturn(true);
		EasyMock.expect(spellingCheckMock.checkSpelling("String")).andReturn(true);
		
		PowerMock.replayAll();
		
		spellingCheckMock.visitToken(dastMock1);
		
		PowerMock.verifyAll();
	}

	/**
	 * check spelling not mocked
	 */
	@Test
	public void visitToken_spellingCorrect_checkSpelling_NotMocked()
	{
		DetailAST dastMock1 = PowerMock.createMock(DetailAST.class);
		DetailAST dastMock2 = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(dastMock1.findFirstToken(TokenTypes.IDENT)).andStubReturn(dastMock2);
		EasyMock.expect(dastMock2.getText()).andReturn("fooBar");

		PowerMock.mockStatic(Util.class);
		List<String> englishWords = new ArrayList<String>(Arrays.asList("foo", "bar" ));
		EasyMock.expect(Util.readResource(SpellingCheck.class, "EnglishWords")).andReturn(englishWords);
		
		String[] tokenArray = new String[] { "foo", "Bar" };
		EasyMock.expect(Util.tokenizeCamelCase("fooBar")).andReturn(tokenArray);
		
		SpellingCheck spellingCheckMock = new SpellingCheck();
		
		PowerMock.replayAll();
		
		spellingCheckMock.init();
		spellingCheckMock.visitToken(dastMock1);
		
		PowerMock.verifyAll();
	}
	
	/**
	 * check spelling and tokenize not mocked
	 */
	@Test
	public void visitToken_spellingCorrect_checkSpelling_tokenize_NotMocked()
	{
		DetailAST dastMock1 = PowerMock.createMock(DetailAST.class);
		DetailAST dastMock2 = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(dastMock1.findFirstToken(TokenTypes.IDENT)).andStubReturn(dastMock2);
		EasyMock.expect(dastMock2.getText()).andReturn("fooBar");

		PowerMock.mockStaticPartial(Util.class, "readResource");
		List<String> englishWords = new ArrayList<String>(Arrays.asList("foo", "bar" ));
		EasyMock.expect(Util.readResource(SpellingCheck.class, "EnglishWords")).andReturn(englishWords);
		
		SpellingCheck spellingCheckMock = new SpellingCheck();
		
		PowerMock.replayAll();
		
		spellingCheckMock.init();
		spellingCheckMock.visitToken(dastMock1);
		
		PowerMock.verifyAll();
	}

	/**
	 * check spelliong, util, get english words not mocked
	 */
	@Test
	public void visitToken_spellingCorrect_checkSpelling_tokenize_getEnglishWords_NotMocked()
	{
		DetailAST dastMock1 = PowerMock.createMock(DetailAST.class);
		DetailAST dastMock2 = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(dastMock1.findFirstToken(TokenTypes.IDENT)).andStubReturn(dastMock2);
		EasyMock.expect(dastMock2.getText()).andReturn("theseAreValidWords");

		SpellingCheck spellingCheckMock = new SpellingCheck();
		
		PowerMock.replayAll();
		
		spellingCheckMock.init();
		spellingCheckMock.visitToken(dastMock1);
		
		PowerMock.verifyAll();
	}

	/**
	 * full mock of visit token with incorrect spelling
	 */
	@Test
	public void visitToken_spellingIncorrect_fullMock()
	{
		DetailAST dastMock1 = PowerMock.createMock(DetailAST.class);
		DetailAST dastMock2 = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(dastMock1.getLineNo()).andReturn(42);
		EasyMock.expect(dastMock1.findFirstToken(TokenTypes.IDENT)).andStubReturn(dastMock2);
		EasyMock.expect(dastMock2.getText()).andReturn("testBadspelling");
		
		PowerMock.mockStatic(Util.class);
		String[] tokenArray = new String[] { "test", "Badspelling" };
		EasyMock.expect(Util.tokenizeCamelCase("testBadspelling")).andReturn(tokenArray);
		
		SpellingCheck spellingCheckMock = PowerMock.createPartialMock(SpellingCheck.class, "checkSpelling", "log");
		
		EasyMock.expect(spellingCheckMock.checkSpelling("test")).andReturn(true);
		EasyMock.expect(spellingCheckMock.checkSpelling("Badspelling")).andReturn(false);
		
		spellingCheckMock.log(42, "spellingmistake", "Badspelling");
		PowerMock.expectLastCall();

		PowerMock.replayAll();
		
		spellingCheckMock.visitToken(dastMock1);
		
		PowerMock.verifyAll();
	}

	/**
	 * checkSpelling not mocked
	 */
	@Test
	public void visitToken_spellingIncorrect_checkSpelling_NotMocked()
	{
		DetailAST dastMock1 = PowerMock.createMock(DetailAST.class);
		DetailAST dastMock2 = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(dastMock1.getLineNo()).andReturn(42);
		EasyMock.expect(dastMock1.findFirstToken(TokenTypes.IDENT)).andStubReturn(dastMock2);
		EasyMock.expect(dastMock2.getText()).andReturn("testBadSpellingxyz");
		
		PowerMock.mockStatic(Util.class);

		String[] tokenArray = new String[] { "test", "Bad", "Spellingxyz" };
		EasyMock.expect(Util.tokenizeCamelCase("testBadSpellingxyz")).andReturn(tokenArray);
		
		SpellingCheck spellingCheckMock = PowerMock.createPartialMock(SpellingCheck.class, "getEnglishWords", "log");

		HashSet<String> englishWords = new HashSet<String>(Arrays.asList("test", "bad", "spelling"));
		EasyMock.expect(spellingCheckMock.getEnglishWords()).andReturn(englishWords).times(3);
		
		spellingCheckMock.log(42, "spellingmistake", "Spellingxyz");
		PowerMock.expectLastCall();
		
		PowerMock.replayAll();

		spellingCheckMock.visitToken(dastMock1);
		
		PowerMock.verifyAll();
	}

	/**
	 * check spelling and tokenize not mocked
	 */
	@Test
	public void visitToken_spellingIncorrect_checkSpelling_tokenize_NotMocked()
	{
		DetailAST dastMock1 = PowerMock.createMock(DetailAST.class);
		DetailAST dastMock2 = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(dastMock1.getLineNo()).andReturn(42);
		EasyMock.expect(dastMock1.findFirstToken(TokenTypes.IDENT)).andStubReturn(dastMock2);
		EasyMock.expect(dastMock2.getText()).andReturn("testBadSpellingxyz");

		SpellingCheck spellingCheckMock = PowerMock.createPartialMock(SpellingCheck.class, "getEnglishWords", "log");

		HashSet<String> englishWords = new HashSet<String>(Arrays.asList("test", "bad", "spelling"));
		EasyMock.expect(spellingCheckMock.getEnglishWords()).andReturn(englishWords).times(3);
		
		spellingCheckMock.log(42, "spellingmistake", "Spellingxyz");
		PowerMock.expectLastCall();
		
		PowerMock.replayAll();

		spellingCheckMock.visitToken(dastMock1);
		
		PowerMock.verifyAll();
	}

	/**
	 * check spelling, tokenize and get english words not mocked
	 */
	@Test
	public void visitToken_spellingIncorrect_checkSpelling_tokenize_getEnglishWords_NotMocked()
	{
		DetailAST dastMock1 = PowerMock.createMock(DetailAST.class);
		DetailAST dastMock2 = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(dastMock1.getLineNo()).andReturn(42);
		EasyMock.expect(dastMock1.findFirstToken(TokenTypes.IDENT)).andStubReturn(dastMock2);
		EasyMock.expect(dastMock2.getText()).andReturn("testBadSpellingxyz");
		
		PowerMock.mockStaticPartial(Util.class, "readResource");
		List<String> englishWords = new ArrayList<String>(Arrays.asList("test", "bad", "spelling"));
		EasyMock.expect(Util.readResource(SpellingCheck.class, "EnglishWords")).andReturn(englishWords);

		SpellingCheck spellingCheckMock = PowerMock.createPartialMock(SpellingCheck.class, "log");

		spellingCheckMock.log(42, "spellingmistake", "Spellingxyz");
		PowerMock.expectLastCall();
		
		PowerMock.replayAll();

		spellingCheckMock.init();
		spellingCheckMock.visitToken(dastMock1);
		
		PowerMock.verifyAll();
	}

	/**
	 * check spelling, get english words, and util not mocked
	 */
	@Test
	public void visitToken_spellingIncorrect_checkSpelling_getEnglishWords_util_NotMocked()
	{
		DetailAST dastMock1 = PowerMock.createMock(DetailAST.class);
		DetailAST dastMock2 = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(dastMock1.getLineNo()).andReturn(42);
		EasyMock.expect(dastMock1.findFirstToken(TokenTypes.IDENT)).andStubReturn(dastMock2);
		EasyMock.expect(dastMock2.getText()).andReturn("testBadSpellingxyz");
		
		SpellingCheck spellingCheckMock = PowerMock.createPartialMock(SpellingCheck.class, "log");

		spellingCheckMock.log(42, "spellingmistake", "Spellingxyz");
		PowerMock.expectLastCall();
		
		PowerMock.replayAll();

		spellingCheckMock.init();
		spellingCheckMock.visitToken(dastMock1);
		
		PowerMock.verifyAll();
	}
}
