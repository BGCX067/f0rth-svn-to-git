package web;

import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;

public class BasicSession extends WebSession {
	public static BasicSession get() {
		return (BasicSession)Session.get();
	}

	public BasicSession(Request request) {
		super(request);
	}
}
