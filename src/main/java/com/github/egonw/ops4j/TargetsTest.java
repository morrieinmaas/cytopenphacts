package com.github.egonw.ops4j;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.junit.Assert;
import org.junit.Test;

public class TargetsTest extends AbstractOPS4JTest{

        { super.pickUpConfig(); }

        @Test
        public void info() throws ClientProtocolException, IOException, HttpException {
                Targets client = Targets.getInstance(super.server, super.appID, super.appKey);
                Assert.assertNotNull(client);
                String turtle = client.info("http://www.conceptwiki.org/concept/00059958-a045-4581-9dc5-e5a08bb0c291");
                Assert.assertNotNull(turtle);
                Assert.assertTrue(turtle.contains("prefix"));
                Assert.assertTrue(turtle.contains("http://www.conceptwiki.org/concept/00059958-a045-4581-9dc5-e5a08bb0c291"));
        }

        @Test
        public void pharmacologyCount() throws ClientProtocolException, IOException, HttpException {
                Targets client = Targets.getInstance(super.server, super.appID, super.appKey);
                Assert.assertNotNull(client);
                String turtle = client.pharmacologyCount("http://www.conceptwiki.org/concept/00059958-a045-4581-9dc5-e5a08bb0c291");
                Assert.assertNotNull(turtle);
                Assert.assertTrue(turtle.contains("prefix"));
                Assert.assertTrue(turtle.contains("http://www.conceptwiki.org/concept/00059958-a045-4581-9dc5-e5a08bb0c291"));
                Assert.assertFalse(turtle.contains("\"0\""));
        }

        @Test
        public void pharmacologyList() throws ClientProtocolException, IOException, HttpException {
                Targets client = Targets.getInstance(super.server, super.appID, super.appKey);
        }
        
     
}
