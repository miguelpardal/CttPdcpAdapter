//package pt.ctt.pdcp.adapter;

import java.io.*;
import java.net.*;
import javax.xml.ws.Endpoint;
import javax.jws.WebService;

import javax.net.ssl.*;
import java.security.*;
import java.security.cert.*;

@WebService(targetNamespace = "http://www.ctt.pt/pdcp")
public class CTTAdapter {

	/** global option to enable or disable debug messages. */
	private static final boolean DEBUG = true;

	/** default endpoint address for web service. */
	private static final String DEFAULT_URL = "http://localhost:8180/ctt";

	/** Initialize web service object. */
	public CTTAdapter() throws Exception {
		if (DEBUG)
			System.err.println("Initializing...");
		disableHttpsSecurityChecks();
	}

	/** Test operation. */
	public String ping(String input) {
		if (DEBUG)
			System.err.println("ping " + input);
		return "Hello " + input;
	}

	/**
	 * Check postal code relying on CTT public endpoint
	 * 
	 * @param postCode - string containing a postal code
	 * @return true if the postal code is valid, false otherwise
	 * @throws CTTAdapterException
	 */
	public boolean validatePostalCode(String postCode) throws CTTAdapterException {
		if (DEBUG)
			System.err.println("validatePostalCode " + postCode);

		try {
			// validate input
			if (postCode == null)
				throw new IllegalArgumentException("Post code cannot be null!");
			postCode = postCode.trim();
			if (postCode.length() == 0)
				throw new IllegalArgumentException("Post code cannot be empty!");

			// Create a URL for the desired page
			URL url = new URL("https://www.ctt.pt/pdcp/xml_pdcp?incodpos=" + postCode);

			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

			// Read all the text returned by the server
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String response = "";
			String str;
			while ((str = in.readLine()) != null) {
				response += str;
			}

			if (DEBUG) {
				System.err.println("Response:");
				System.err.println(response);
			}

			if (response.contains("razao=\"PEVA\"") || response.contains("<OK>")) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			if (DEBUG)
				System.err.println("Caught exception:" + e);
			throw new CTTAdapterException();
		}
	}

	private void disableHttpsSecurityChecks() throws KeyManagementException, NoSuchAlgorithmException {
		System.err.println("WARNING: disabling security checks for HTTPS connections. DO NOT USE IN PRODUCTION CODE!");
		/*
		 * fix for Exception in thread "main" javax.net.ssl.SSLHandshakeException:
		 * sun.security.validator.ValidatorException: PKIX path building failed:
		 * sun.security.provider.certpath.SunCertPathBuilderException: unable to find
		 * valid certification path to requested target credits:
		 * https://tutoref.com/how-to-disable-ssl-certificat-validation-in-java/
		 */
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		/*
		 * end of the fix
		 */
	}

	/** Start stand-alone web service. */
	public static void main(String[] args) throws Exception {
		// Check arguments for default URL override
		String url;
		if (args.length >= 1) {
			url = args[0];
		} else {
			url = DEFAULT_URL;
		}

		Endpoint endpoint = null;
		try {
			endpoint = Endpoint.create(new CTTAdapter());

			// publish endpoint
			System.out.printf("Starting %s%n", url);
			endpoint.publish(url);

			// wait
			System.out.println("Awaiting connections");
			System.out.println("Press enter to shutdown");
			System.in.read();

		} catch (Exception e) {
			System.out.printf("Caught exception: %s%n", e);
			e.printStackTrace();

		} finally {
			try {
				if (endpoint != null) {
					// stop endpoint
					endpoint.stop();
					System.out.printf("Stopped %s%n", url);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when stopping: %s%n", e);
			}
		}

	}

}
