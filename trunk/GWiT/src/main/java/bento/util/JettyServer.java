package bento.util;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

public class JettyServer {
	public static void main(String[] args) throws Exception {
		int port = Integer.parseInt(System.getProperty("jetty.port", "8888"));

		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);

		Server server = new Server();
		server.addConnector(connector);

		WebAppContext context = new WebAppContext();
		context.setServer(server);
		context.setContextPath("/");
		context.setWar("target/dist/bento.war");

		server.addHandler(context);
		server.start();
	}
}
