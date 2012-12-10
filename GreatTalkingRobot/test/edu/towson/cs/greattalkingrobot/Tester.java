package edu.towson.cs.greattalkingrobot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class Tester {

	@Test
	public void testpattern() {
		String userAgent1 = "adfadfiPaddfadf";
		String userAgent2 = "dafadfiPhonedfadfd";

		Pattern pIPad = Pattern.compile("iPad");
		Pattern pIphone = Pattern.compile("iPhone");

		Matcher m1 = pIPad.matcher(userAgent1);
		Assert.assertTrue(m1.find());
		Matcher m2 = pIphone.matcher(userAgent2);
		Assert.assertTrue(m2.find());

		userAgent1 = "adkfjadfjiudfia";
		userAgent2 = "aldsfaoidufkadjfkadjfjdsljoudfo";

		m1 = pIPad.matcher(userAgent1);
		Assert.assertTrue(!m1.find());
		m2 = pIphone.matcher(userAgent2);
		Assert.assertTrue(!m2.find());
	}
}
