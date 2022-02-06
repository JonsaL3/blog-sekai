package sekai;

public class HTMLTemplate {
	
	final private static String INSTALLER = 
			HTMLFileReader.read("./html/installer.html");

	public static String getInstaller() 
	{
		return INSTALLER;
	}
	
	final private static String LOGIN = 
			HTMLFileReader.read("./html/login.html");

	public static String getLogin() 
	{
		return LOGIN;
	}
	
	private final static String DESTROY =
			HTMLFileReader.read("./html/destroy.html");
	
	public static String getDestroy() 
	{
		return DESTROY;
	}
	
	private final static String PANEL = // $username$ $user_added$ $create_users$ $users_only_see_admin$ $entrys_user$ $password_changed$
			HTMLFileReader.read("./html/panel.html");
	
	public static String getPanel() 
	{
		return PANEL;
	}
	
	private static final String ADMINCREATEUSER =
			HTMLFileReader.read("./html/createUser.html");
	
	public static String getAdminCreateUser() 
	{
		return ADMINCREATEUSER;
	}
	
	private static final String ADMINTABLEUSERS = 
			HTMLFileReader.read("./html/panelUsersAdminMenu.html");
	
	public static String getAdminTableUsers() 
	{
		return ADMINTABLEUSERS;
	}
	
	private static final String ADMINPANELUSERS =
			HTMLFileReader.read("./html/manageUsersRow.html");
	
	public static String getAdminPanelUsers() 
	{
		return ADMINPANELUSERS;
	}
	
	private static final String EDITOR = 
			HTMLFileReader.read("./html/editor.html");
	
	public static String getEditor() 
	{
		return EDITOR;
	}
	
	private static final String ROWEDITORPANEL = 
			HTMLFileReader.read("./html/manageEntrysRow.html");
	
	public static String getRowEditorPanel() 
	{
		return ROWEDITORPANEL;
	}
	
	private static final String DELETE = 
			HTMLFileReader.read("./html/delete.html");
	
	public static String getDelete() 
	{
		return DELETE;
	}
	
	private static final String BLOG = // $blog_entrys$
			HTMLFileReader.read("./html/blog.html");
	
	public static String getBlog() 
	{
		return BLOG;
	}
	
	private static final String BLOGENTRY = // $title$ $year$ $owner_options$ $content$ $author$
			HTMLFileReader.read("./html/blogEntry.html");
	
	public static String getBlogEntry() 
	{
		return BLOGENTRY;
	}
	
	private static final String BLOGENTRYOWNEROPTIONS = // $entry_id$
			HTMLFileReader.read("./html/blogEntryOwnerOptions.html");
	
	public static String getBlogEntryOwnerOptions() 
	{
		return BLOGENTRYOWNEROPTIONS;
	}
	
	private static final String DELETEUSERMESSAGE = 
			HTMLFileReader.read("./html/deleteUser.html");
	
	public static String getDeleteUserMessage() 
	{
		return DELETEUSERMESSAGE;
	}
	
	private static final String UPDATEPASSWORDUSER =
			HTMLFileReader.read("./html/updatePasswordUser.html");
	
	public static String getUpdatePasswordUser() 
	{
		return UPDATEPASSWORDUSER;
	}
	
}
