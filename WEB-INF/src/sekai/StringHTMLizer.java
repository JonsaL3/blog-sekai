package sekai;

public class StringHTMLizer {

	
	/* Requisitos para good ending
	 * 
	 * 	& → &amp;
	 *	> → &gt;
	 *	< → &lt;
 	 *	' → &#039;
	 *	" → &#034;
	 * 
	 * */
	
	public static String convert(String fromHTMLForm)
	{
		String result = "";
		
		result = fromHTMLForm
				.replace("&", "&amp")
				.replace(">", "&gt")
				.replace("<", "&lt")
				.replace("'", "&#039")
				.replace("\"", "&#034");
		
		return result;
	}
	
	public static String unconvert(String toHTMLForm)
	{
		String result = "";
		
		result = toHTMLForm
				.replace("&amp", "&")
				.replace("&gt", ">")
				.replace("&lt", "<")
				.replace("&#039", "'")
				.replace("&#034", "\"");
		
		return result;
	}
	
}
