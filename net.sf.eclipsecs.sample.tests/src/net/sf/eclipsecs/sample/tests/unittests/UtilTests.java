package net.sf.eclipsecs.sample.tests.unittests;

import net.sf.eclipsecs.sample.checks.Util;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

public class UtilTests 
{
	@Before
	public void setUp()
	{
		
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void tokenizeCamelCase_nullStr()
	{
		Util.tokenizeCamelCase(null);
	}

	@Test
	public void tokenizeCamelCase_singleTermLower()
	{
		String[] tokens = Util.tokenizeCamelCase("test");
		
		assertEquals(tokens.length, 1);
		assertEquals(tokens[0], "test");
	}

	@Test
	public void tokenizeCamelCase_singleTermUpper()
	{
		String[] tokens = Util.tokenizeCamelCase("TEST");
		
		assertEquals(tokens.length, 1);
		assertEquals(tokens[0], "TEST");
	}

	@Test
	public void tokenizeCamelCase_singleTitleTerm()
	{
		String[] tokens = Util.tokenizeCamelCase("Test");
		
		assertEquals(tokens.length, 1);
		assertEquals(tokens[0], "Test");
	}

	@Test
	public void tokenizeCamelCase_camelTerm()
	{
		String[] tokens = Util.tokenizeCamelCase("thisTest");
		
		assertEquals(tokens.length, 2);
		assertEquals(tokens[0], "this");
		assertEquals(tokens[1], "Test");
	}

	@Test
	public void tokenizeCamelCase_titleTerm()
	{
		String[] tokens = Util.tokenizeCamelCase("ThisTest");
		
		assertEquals(tokens.length, 2);
		assertEquals(tokens[0], "This");
		assertEquals(tokens[1], "Test");
	}

	@Test
	public void tokenizeCamelCase_AbbrBegining()
	{
		String[] tokens = Util.tokenizeCamelCase("ABBRTestClass");
		
		assertEquals(tokens.length, 3);
		assertEquals(tokens[0], "ABBR");
		assertEquals(tokens[1], "Test");
		assertEquals(tokens[2], "Class");
	}
	
	@Test
	public void tokenizeCamelCase_AbbrMiddle()
	{
		String[] tokens = Util.tokenizeCamelCase("testABBRClass");
		
		assertEquals(tokens.length, 3);
		assertEquals(tokens[0], "test");
		assertEquals(tokens[1], "ABBR");
		assertEquals(tokens[2], "Class");
	}
	
	@Test
	public void tokenizeCamelCase_AbbrEnd()
	{
		String[] tokens = Util.tokenizeCamelCase("testClassABBR");
		
		assertEquals(tokens.length, 3);
		assertEquals(tokens[0], "test");
		assertEquals(tokens[1], "Class");
		assertEquals(tokens[2], "ABBR");
	}

	@Test
	public void tokenizeCamelCase_singleLetter()
	{
		String[] tokens = Util.tokenizeCamelCase("testAClass");
		
		assertEquals(tokens.length, 3);
		assertEquals(tokens[0], "test");
		assertEquals(tokens[1], "A");
		assertEquals(tokens[2], "Class");
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void readResource_nullClass()
	{
		Util.readResource(null, "TestResource");
	}
	
	@Test(expected = java.lang.NullPointerException.class)
	public void readResource_nullResourceName()
	{
		Util.readResource(this.getClass(), null);
	}
	
	@Test(expected = java.lang.NullPointerException.class)
	public void readResource_invalidResource()
	{
		Util.readResource(this.getClass(), "ThisIsNotAResource");
	}
	
	@Test
	public void readResource_readValidResource()
	{
		List<String> resourceLines = Util.readResource(this.getClass(), "TestResource");

		assertEquals(resourceLines.size(), 3);
		assertEquals(resourceLines.get(0), "This is");
		assertEquals(resourceLines.get(1), "a");
		assertEquals(resourceLines.get(2), "test.");
	}
}
