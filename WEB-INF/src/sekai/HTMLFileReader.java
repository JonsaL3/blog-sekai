package sekai;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HTMLFileReader {

	public static String read(String htmlFile)
	{
		FileReader reader;
		BufferedReader bReader;
		String html = "";
		
		try
		{
			reader = new FileReader(htmlFile);
			bReader = new BufferedReader(reader);
			
			while (bReader.ready())
			{
				html += bReader.readLine();
			}
			
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return html;
	}
	
}
