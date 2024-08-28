/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.oss.services.cm.export.service.batch.downloadmanager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("FileItemProcessor")
public class ItemProcessor implements javax.batch.api.chunk.ItemProcessor {

    @Inject
    private JobContext jobContext;

    @Override
    public Object processItem(final Object fileURL) throws Exception {
        final Properties jobParameters = BatchRuntime.getJobOperator().getParameters(this.jobContext.getExecutionId());
        final String downloadDirectory = this.jobContext.getProperties().getProperty("downloadDirectory") + "/job" + this.jobContext.getExecutionId();
        final String urlStr = (String) fileURL;
        final URL url = new URL(urlStr);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // Cast shouldn't fail
        HttpURLConnection.setFollowRedirects(true);
        // allow both GZip and Deflate (ZLib) encodings
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        final String encoding = conn.getContentEncoding();
        InputStream inStr = null;

        // create the appropriate stream wrapper based on
        // the encoding type
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
            inStr = new GZIPInputStream(conn.getInputStream());
        } else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
            inStr = new InflaterInputStream(conn.getInputStream(), new Inflater(true));
        } else {
            inStr = conn.getInputStream();
        }
        // now read from input stream and write to file
        final String filename = downloadDirectory + "/" + "downl" + urlStr.substring(urlStr.length() - 8);
        BufferedOutputStream bos = null;

        //create an object of FileOutputStream
        final FileOutputStream fos = new FileOutputStream(new File(filename));

        //create an object of BufferedOutputStream
        bos = new BufferedOutputStream(fos);
        final byte[] buffer = new byte[1024]; // you can configure the buffer size
        int length;

        while ((length = inStr.read(buffer)) != -1) {
            bos.write(buffer, 0, length); //copy streams
        }
        inStr.close(); // call this in a finally block
        fos.close();

        System.out.println("Downloaded and written file " + filename);

        return filename;
    }
}
