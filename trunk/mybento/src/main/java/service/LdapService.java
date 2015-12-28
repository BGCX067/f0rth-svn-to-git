package service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LdapService {
	private static final Logger LOG = LoggerFactory.getLogger(LdapService.class);

	private Properties ldapConfig;

	public void setLdapConfig(Properties ldapConfig) {
		this.ldapConfig = ldapConfig;
	}

	private String getUserNameSuffix() {
		String str = ldapConfig.getProperty("ldap.username");
		int index = str.indexOf('@');
		if (index > 0) {
			return str.substring(index);
		} else {
			LOG.error(String.format("ldap.username=%s is not valid, should be username@domain.com.", str));
			return "";
		}
	}

	private LdapContext createContext(String userName, String password) throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.PROVIDER_URL, ldapConfig.getProperty("ldap.url"));
		env.put(Context.SECURITY_PRINCIPAL, userName);
		env.put(Context.SECURITY_CREDENTIALS, password);
		return new InitialLdapContext(env, null);
	}

	public boolean authenticateUser(String userName, String password) {
		userName = userName.trim();
		password = password.trim();

		boolean authenticated = false;

		try {
			LdapContext ctx = createContext(userName + getUserNameSuffix(), password);
			ctx.close();
			authenticated = true;
		} catch (NamingException e) {
			LOG.info("", e);
		}

		return authenticated && password.length() > 0;
	}

	public User findUser(String userName) {
		userName = userName.trim();
		User user = null;

		try {
			LdapContext ctx = createContext(ldapConfig.getProperty("ldap.username"),
											ldapConfig.getProperty("ldap.password"));
			SearchControls ctls = new SearchControls();
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String filter = String.format("(&(objectCategory=Person)(objectClass=user)(sAMAccountName=%s))", userName);
			NamingEnumeration<SearchResult> userEnum = ctx.search("", filter, ctls);
			while (userEnum.hasMoreElements()) {
				SearchResult userResult = userEnum.nextElement();
				user = new User();
				user.setUserName((String)userResult.getAttributes().get("sAMAccountName").get());
				user.setDisplayName((String)userResult.getAttributes().get("name").get());
				int left  = user.getDisplayName().indexOf('(');
				int right = user.getDisplayName().indexOf(')');
				if (left > 0 && right > 0) {
					user.setDisplayName(user.getDisplayName().substring(left + 1, right));
				}
				break;
			}
			ctx.close();
		} catch (NamingException e) {
			LOG.info("", e);
		}

		return user;
	}

	private final String[] SYSTEM_ACCOUNTS = {
		"ADMINISTRATOR", "ASPNET", "CRM", "DUMMY", "GUEST", "HIKEADMIN", "IUSR", "IWAM", "KRBTGT", "MOSS", "SUPPORT", "SYSTEM", "WMANTEST"};

	private boolean isSystemUser(User user) {
		String userName = user.getUserName().toUpperCase();
		String displayName = user.getDisplayName().toUpperCase();
		for (String name : SYSTEM_ACCOUNTS) {
			if (userName.equals(name) || userName.startsWith(name) ||
				displayName.equals(name) || displayName.startsWith(name)) {
				return true;
			}
		}
		return false;
	}

	public List<User> getAllUser() {
		List<User> users = new ArrayList<User>();

		try {
			LdapContext ctx = createContext(ldapConfig.getProperty("ldap.username"),
											ldapConfig.getProperty("ldap.password"));
			SearchControls ctls = new SearchControls();
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String filter = "(&(objectCategory=Person)(objectClass=user))";
			NamingEnumeration<SearchResult> userEnum = ctx.search("", filter, ctls);
			while (userEnum.hasMoreElements()) {
				SearchResult userResult = userEnum.nextElement();
				User user = new User();
				user.setUserName((String)userResult.getAttributes().get("sAMAccountName").get());
				user.setDisplayName((String)userResult.getAttributes().get("name").get());
				int left  = user.getDisplayName().indexOf('(');
				int right = user.getDisplayName().indexOf(')');
				if (left > 0 && right > 0) {
					user.setDisplayName(user.getDisplayName().substring(left + 1, right));
				}
				if (!isSystemUser(user)) {
					users.add(user);
				}
			}
			ctx.close();
		} catch (NamingException e) {
			LOG.info("", e);
		}

		return users;
	}
}
