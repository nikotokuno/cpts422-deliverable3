package net.sf.eclipsecs.sample.checks;
import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Useful utility methods for project.
 * @author Wesley Thorsen 11397880
 * 
 */
public class Util 
{
	/**
	 * Tokenizes text in camelCase. <br/> 
	 * Example: "systemIOVariable" -> { "system", "IO", "Variable" }
	 * @param str Text to tokenize.
	 * @return Text tokens.
	 */
	public static String[] tokenizeCamelCase(String str)
	{
		// Regex credit: https://stackoverflow.com/questions/7593969/regex-to-split-camelcase-or-titlecase-advanced
		return str.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
	}

	/**
	 * Reads a resource as a text file.
	 * @param resourceName Name of resource (must be located in package of classInPackage).
	 * @param classInPackage Class that is in the package of the resource you want to read. <br/>
	 * (need to open resources in external packages)
	 * @return Resource text, split by newline characters.
	 * @throws NullPointerException 
	 */
	public static List<String> readResource(Class classInPackage, String resourceName) throws NullPointerException
	{
		if (classInPackage == null) throw new NullPointerException("loader is null");
		if (resourceName == null) throw new NullPointerException("resourceName is null");
		
		InputStream is = classInPackage.getResourceAsStream(resourceName);
		InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
		BufferedReader br = new BufferedReader(isr);
		return br.lines().collect(Collectors.toList());
	}
}
