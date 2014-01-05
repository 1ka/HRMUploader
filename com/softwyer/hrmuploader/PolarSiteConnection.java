/**
 *  Copyright Softwyer Ltd. 2006-2013.  All rights reserved.
 * 
 *  This software is provided 'as is' without warranty of any kind, either express or implied, including, but not limited to, 
 *  the implied warranties of fitness for a purpose, or the warranty of non-infringement.
 *  
 *  This software can be freely distributed as long as this header remains intact.  
 *  
 *  It can be used in non-commercial projects as long as this raw source code is included in the distribution with this header intact. 
 *  
 *  http://softwyer.wordpress.org
 *  
 *  http://rikara.blogspot.com/2013/04/connecting-to-polar-personal-trainer.html
 *  
 */

package com.softwyer.hrmuploader;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.softwyer.hrmuploader.webservice.Webservice;
import com.softwyer.hrmuploader.webservice.WristUnitData;

/**
 * The class that maintains the SSL connection to the Polar Website. This class
 * will perform all the connection related work.
 * 
 * @author karl
 * 
 */
public class PolarSiteConnection implements Serializable {

	private static final Logger logger = Logger
			.getLogger(PolarSiteConnection.class.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = -1984701784641140570L;

	private transient SimpleDateFormat sdf;

	private static final String HOST = "www.polarpersonaltrainer.com";
	private static final String PATH_INITIALISE = "/weblink2/webservice.jsp";
	private static final String PATH_CHECK = "/weblink2/check.jsp";
	private static final String PATH_STORE = "/weblink2/store.jsp";

	public PolarSiteConnection() {
		super();
	}

	/**
	 * Get the date as a string.
	 */
	private String getDateString() {
		if (sdf == null) {
			sdf = new SimpleDateFormat("MMddyyHHmmssSSS");
		}
		logger.finest("Getting the date, simple date format is " + sdf);
		return sdf.format(new Date());
	}

	/**
	 * Set the request content type and return the date string used.
	 */
	private String setRequestPropertyContentType(
			final HttpURLConnection connection) {

		final String dateString = getDateString();

		connection.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=--------" + dateString);

		return dateString;

	}

	private HttpURLConnection getConnection(final String path,
			final String username, final String password) throws Exception {

		final Properties systemProperties = System.getProperties();
		logger.severe("Properties: " + systemProperties);

		// final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
		// "127.0.0.1", 8080));

		if (systemProperties.get("proxyHost") != null) {

			final SSLContext sslContext = SSLContext.getInstance("SSL");

			// set up a TrustManager that trusts everything
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					System.out.println("getAcceptedIssuers =============");
					return null;
				}

				public void checkClientTrusted(
						final java.security.cert.X509Certificate[] arg0,
						final String arg1) throws CertificateException {
					System.out.println("checkClientTrusted =============");
				}

				public void checkServerTrusted(
						final java.security.cert.X509Certificate[] arg0,
						final String arg1) throws CertificateException {
					System.out.println("checkServerTrusted =============");
				}
			} }, new SecureRandom());

			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
					.getSocketFactory());

			HttpsURLConnection
					.setDefaultHostnameVerifier(new HostnameVerifier() {
						public boolean verify(final String arg0,
								final SSLSession arg1) {
							System.out
									.println("hostnameVerifier =============");
							return true;
						}
					});

		}

		final URI uri = new URI("https", HOST, path, null);
		final URL url = uri.toURL();
		final HttpURLConnection connection = (HttpURLConnection) url
				.openConnection();

		connection.setRequestProperty("Host", HOST + ":443");
		connection.setRequestProperty("Accept", "text/html, */*");
		connection.setRequestProperty("User-Agent",
				"Mozilla/3.0 (compatible; Indy Library)");

		connection.setDoOutput(true);

		connection
				.setRequestProperty(
						"Authorization",
						"Basic "
								+ Base64.encode((username + ":" + password)
										.getBytes()));

		return connection;
	}

	public Webservice intiateConnection(final String username,
			final String password) {

		logger.info("Initiating Connection to Polar website");
		Webservice webservice = null;
		try {

			final HttpURLConnection connection = getConnection(PATH_INITIALISE,
					username, password);

			logger.finest("Got HttpUrlConnection: " + connection);

			final String dateString = setRequestPropertyContentType(connection);

			final String handshake = getMessageHeader(dateString)
					+ "<webservice><object name=\"userPreferences\"><prop name=\"Email\">"
					+ username
					+ "</prop><prop name=\"Language\">en</prop><prop name=\"Wristunit\">Polar CS600</prop><prop name=\"ClientVersion\">2.4.7.423</prop></object></webservice>"
					+ getMessageFooter(dateString);

			logger.finest("Trying to connect");

			connection.connect();

			logger.finest("Connected");

			final OutputStream os = connection.getOutputStream();

			logger.finest("Got OutputStream");

			final BufferedOutputStream bos = new BufferedOutputStream(os);

			logger.finest("Attempting to write handshake string: " + handshake);

			bos.write((handshake).getBytes());

			logger.finest("Flushing stream");

			bos.flush();

			logger.finest("Creating buffered reader");

			final BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			logger.finest("Creating input source");

			final InputSource is = new InputSource(in);

			logger.finest("Parsing input");

			webservice = parser(is);

			in.close();
			bos.close();
			connection.disconnect();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return webservice;

	}

	private String getMessageHeader(final String dateString) {
		return "----------"
				+ dateString
				+ "\nContent-Disposition: form-data; name=\"parameters\"; filename=\"parameters.xml\"\nContent-Type: text/xml\n\n";
	}

	private String getMessageFooter(final String dateString) {
		return "\n\n\n----------" + dateString + "--\n\n";
	}

	//
	// We need to send the following xml to the polar site
	//
	// <wristunit-data>
	// <prop name="Username">xxx@gmail.com</prop>
	// <collection name="Exercises">
	// <item type="Exercise">
	// <prop name="time">2008-09-21 11:42:27.000</prop>
	// <prop name="origin">1</prop>
	// </item>
	// <item type="Exercise">
	// <prop name="time">2008-09-22 11:54:39.000</prop>
	// <prop name="origin">1</prop>
	// </item>
	// </collection>
	// </wristunit-data>
	//
	public Webservice checkExercises(final String username,
			final String password, final String[] exerciseDates) {

		Webservice webservice = null;

		try {
			final HttpURLConnection connection = getConnection(PATH_CHECK,
					username, password);

			final String dateString = setRequestPropertyContentType(connection);

			final StringBuilder sb = new StringBuilder();
			sb.append(getMessageHeader(dateString));
			sb.append("<wristunit-data><prop name=\"Username\">");
			sb.append(username);
			sb.append("</prop><collection name=\"Exercises\">");

			for (final String date : exerciseDates) {
				sb.append("<item type=\"Exercise\"><prop name=\"time\">");
				sb.append(date);
				sb.append("</prop><prop name=\"origin\">1</prop></item>");
			}
			sb.append("</collection></wristunit-data>");
			sb.append(getMessageFooter(dateString));

			System.out.println("Writing check: " + sb.toString());

			connection.connect();

			final OutputStream os = connection.getOutputStream();

			final BufferedOutputStream bos = new BufferedOutputStream(os);

			bos.write(sb.toString().getBytes());

			bos.flush();

			final BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			final InputSource is = new InputSource(in);

			webservice = parser(is);

			in.close();
			bos.close();
			connection.disconnect();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return webservice;
	}

	public Webservice sendExercises(final String username,
			final String password, final WristUnitData wud) {

		Webservice webservice = null;

		try {

			final HttpURLConnection connection = getConnection(PATH_STORE,
					username, password);

			final String dateString = setRequestPropertyContentType(connection);

			final StringWriter sw = new StringWriter();
			sw.append(getMessageHeader(dateString));

			final Marshaller m = new Marshaller(sw);
			m.setMapping(getCastorMapping());
			m.marshal(wud);

			sw.append(getMessageFooter(dateString));

			final String xml = sw.toString();
			System.out.println("Writing exercise:\n" + xml);

			connection.connect();

			final OutputStream os = connection.getOutputStream();

			final BufferedOutputStream bos = new BufferedOutputStream(os);

			bos.write(sw.toString().getBytes());

			bos.flush();

			final BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			final InputSource is = new InputSource(in);

			webservice = parser(is);

			in.close();
			bos.close();
			connection.disconnect();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return webservice;
	}

	/**
	 * 
	 * @return
	 */
	private Mapping getCastorMapping() {

		final Mapping mapping = new Mapping();

		// try {
		final InputStream is = PolarSiteConnection.class.getClassLoader()
				.getResourceAsStream(
						"com/softwyer/hrmuploader/webservice/mapping.xml");

		final InputSource fis = new InputSource(is);

		// Load the mapping information from the file
		mapping.loadMapping(fis);
		// }
		return mapping;
	}

	/**
	 * 
	 * @param xml
	 * @return
	 */
	private Webservice parser(final InputSource xml) {

		try {
			final Unmarshaller unmar = new Unmarshaller(getCastorMapping());
			final Webservice ws = (Webservice) unmar.unmarshal(xml);

			logger.info("WebService data is: " + ws);

			for (final Object o : ws.getObject().keySet()) {
				System.out.println("Key is: " + o + "; value is: "
						+ ws.getObject().get(o));
			}

			logger.info("Status is: " + ws.getStatus());

			return ws;

		} catch (final Exception e) {
			logger.log(Level.WARNING, "Problem parsing input", e);
			e.printStackTrace();
			return null;
		}

	}
}
