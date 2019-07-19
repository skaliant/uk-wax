package de.skaliant.wax.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class URLBuilderTest {
	@Test
	public void test_empty() {
		URLBuilder proband = new URLBuilder();

		assertEquals("/", proband.toString());
	}


	@Test
	public void test_hostOnly() {
		URLBuilder proband = new URLBuilder();

		proband.setHost("my.host");

		assertEquals("http://my.host/", proband.toString());
	}


	@Test
	public void test_hostAndScheme() {
		URLBuilder proband = new URLBuilder();

		proband.setHost("my.host");
		proband.setScheme("scheme");

		assertEquals("scheme://my.host/", proband.toString());
	}


	@Test
	public void test_hostAndPort() {
		URLBuilder proband = new URLBuilder();

		proband.setHost("my.host");
		proband.setPort(8080);

		assertEquals("http://my.host:8080/", proband.toString());
	}


	@Test
	public void test_pathOnly_simple() {
		URLBuilder proband = new URLBuilder();

		proband.setPath("path");

		assertEquals("/path", proband.toString());
	}


	@Test
	public void test_pathOnly_full() {
		URLBuilder proband = new URLBuilder();

		proband.setPath("/my/very/personal/path");

		assertEquals("/my/very/personal/path", proband.toString());
	}


	@Test
	public void test_pathOnly_appended() {
		URLBuilder proband = new URLBuilder();

		proband.setPath("path");
		proband.appendPath("with");
		proband.appendPath("sub");
		proband.appendPath("path");

		assertEquals("/path/with/sub/path", proband.toString());
	}


	@Test
	public void test_pathAndParameters() {
		URLBuilder proband = new URLBuilder();

		proband.setPath("path");
		proband.setQueryString("name=value");

		assertEquals("/path?name=value", proband.toString());
	}


	@Test
	public void test_all() {
		URLBuilder proband = new URLBuilder();

		proband.setScheme(URLBuilder.HTTPS);
		proband.setHost("my.host");
		proband.setPath("path");
		proband.setQueryString("name=value");
		proband.setAnchor("anchor");

		assertEquals("https://my.host/path?name=value#anchor", proband.toString());
	}
}
