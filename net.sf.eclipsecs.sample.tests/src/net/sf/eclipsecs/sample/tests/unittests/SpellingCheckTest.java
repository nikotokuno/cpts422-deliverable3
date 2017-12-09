package net.sf.eclipsecs.sample.tests.unittests;

import net.sf.eclipsecs.sample.checks.*;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;
import java.util.Arrays;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.easymock.EasyMock;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.powermock.api.easymock.PowerMock;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SpellingCheck.class, DetailAST.class, Util.class })
public class SpellingCheckTest 
{
	@Test
	public void checkSpelling_validWord()
	{
		// Create spy for SpellingCheck
		SpellingCheck spyCheck = spy(new SpellingCheck());

		// Mock SpellingCheck.getEnglishWords() to return HashSet<String> EnglishWords
		HashSet<String> EnglishWords = new HashSet<String>(Arrays.asList("foo"));
		doReturn(EnglishWords).when(spyCheck).getEnglishWords();
		
		// Make sure spelling is correct
		assertTrue(spyCheck.checkSpelling("foo"));
	}

	@Test
	public void checkSpelling_invalidWord()
	{
		// Create spy for SpellingCheck
		SpellingCheck spyCheck = spy(new SpellingCheck());

		// Mock SpellingCheck.getEnglishWords() to return HashSet<String> EnglishWords
		HashSet<String> EnglishWords = new HashSet<String>(Arrays.asList("foo"));
		doReturn(EnglishWords).when(spyCheck).getEnglishWords();

		// Make sure spelling is incorrect
		assertFalse(spyCheck.checkSpelling("bar"));
	}

	@Test
	public void checkSpelling_captialWordToLowerCase()
	{
		// Create spy for SpellingCheck
		SpellingCheck spyCheck = spy(new SpellingCheck());

		// Mock SpellingCheck.getEnglishWords() to return HashSet<String> EnglishWords
		HashSet<String> EnglishWords = new HashSet<String>(Arrays.asList("foo"));
		doReturn(EnglishWords).when(spyCheck).getEnglishWords();

		// Make sure spelling is correct even though word contains capital letters
		assertTrue(spyCheck.checkSpelling("Foo"));
	}
	
	@Test
	public void visitToken_spellingCorrect()
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

	@Test
	public void visitToken_spellingIncorrect()
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

	@Test
	public void visitToken_skipAllCapsWord()
	{
		DetailAST dastMock1 = PowerMock.createMock(DetailAST.class);
		DetailAST dastMock2 = PowerMock.createMock(DetailAST.class);
		
		EasyMock.expect(dastMock1.findFirstToken(TokenTypes.IDENT)).andStubReturn(dastMock2);
		EasyMock.expect(dastMock2.getText()).andReturn("fooBAR");
		
		PowerMock.mockStatic(Util.class);
		String[] tokenArray = new String[] { "foo", "BAR" };
		EasyMock.expect(Util.tokenizeCamelCase("fooBAR")).andReturn(tokenArray);
		
		SpellingCheck spellingCheckMock = PowerMock.createPartialMock(SpellingCheck.class, "checkSpelling");
		
		EasyMock.expect(spellingCheckMock.checkSpelling("foo")).andReturn(true);

		PowerMock.replayAll();
		
		spellingCheckMock.visitToken(dastMock1);
		
		PowerMock.verifyAll();
	}
}
