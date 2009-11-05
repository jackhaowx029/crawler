/**
 * 
 */
package net.vidageek.crawler.component;

import java.io.IOException;

import net.vidageek.crawler.Status;
import net.vidageek.crawler.exception.CrawlerException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * @author jonasabreu
 * 
 */
public class WebDownloader implements Downloader {

	private final static HttpClient client = new HttpClient();

	private Status error;

	public String get(final String url) {
		try {

			GetMethod method = new GetMethod(url);
			int methodStatus = client.executeMethod(method);
			error = Status.OK;

			if (methodStatus >= 400 && methodStatus <= 499) {
				error = Status.NOT_FOUND;
				return "";
			}
			if (methodStatus >= 500 && methodStatus <= 599) {
				error = Status.UNAUTHORIZED;
				return "";
			}
			if (methodStatus < 200 || methodStatus > 299) {
				throw new CrawlerException("Could not retrieve data from " + url + ". Error code: " + methodStatus);
			}

			return method.getResponseBodyAsString();

		} catch (HttpException e) {
			throw new CrawlerException("Could not retrieve data from " + url, e);
		} catch (IOException e) {
			throw new CrawlerException("Could not retrieve data from " + url, e);
		}
	}

	public Status getErrorCode() {
		return error;
	}

}
