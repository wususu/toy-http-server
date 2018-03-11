package http.startup;

import http.HttpConnector;

public class Bootstrap {

public static void main(String[] args) {
	Thread thread = new Thread(new HttpConnector());
	thread.start();
}
	
}
