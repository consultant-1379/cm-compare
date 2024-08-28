/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.oss.services.cm.export.service.batch.downloadmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.batch.api.Batchlet;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("FileArchiver")
public class FileArchiver implements Batchlet {

    @Inject
    JobContext jobCtx;

    @Override
    public String process() throws Exception {
        final Properties jobParameters = BatchRuntime.getJobOperator().getParameters(this.jobCtx.getExecutionId());
        System.out.println("Archive files in directory for job " + this.jobCtx.getExecutionId());
        final String downloadDirectory = this.jobCtx.getProperties().getProperty("downloadDirectory") + File.separator + "job"
                + this.jobCtx.getExecutionId();
        final String outputDirectory = this.jobCtx.getProperties().getProperty("archivesDirectory");
        createOutputDirectory(outputDirectory);

        final String outputZipFile = this.jobCtx.getProperties().getProperty("archivesDirectory") + File.separator + "job"
                + this.jobCtx.getExecutionId() + ".zip";
        final List<String> fileList = generateFileList(new File(downloadDirectory), downloadDirectory);
        zipIt(outputZipFile, downloadDirectory, fileList);
        return "done";
    }

    /**
     * @param outputDirectory
     */
    private void createOutputDirectory(final String outputDirectory) {
        final File ouputDir = new File(outputDirectory);
        ouputDir.mkdir();
    }

    @Override
    public void stop() throws Exception {
    }

    /**
     * Zip it
     * 
     * @param zipFile
     *            output ZIP file location
     */
    public void zipIt(final String zipFile, final String sourceFolder, final List<String> fileList) {
        final byte[] buffer = new byte[1024];
        try {
            final FileOutputStream oFile = new FileOutputStream(new File(zipFile), false);
            final FileOutputStream fos = new FileOutputStream(zipFile);
            final ZipOutputStream zos = new ZipOutputStream(fos);
            System.out.println("Output to Zip : " + zipFile);
            for (final String file : fileList) {
                System.out.println("File Added : " + file);
                final ZipEntry ze = new ZipEntry(file);
                zos.putNextEntry(ze);
                final FileInputStream in = new FileInputStream(sourceFolder + File.separator + file);
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
            }
            zos.closeEntry();
            zos.close();
            System.out.println("Done with creating the file archive");
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Traverse a directory and get all files, and add the file into fileList
     */
    public List<String> generateFileList(final File node, final String sourceFolder) {
        final List<String> fileList = new ArrayList<String>();
        //add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.getAbsoluteFile().toString(), sourceFolder));
        }
        if (node.isDirectory()) {
            final String[] subNote = node.list();
            for (final String filename : subNote) {
                generateFileList(new File(node, filename), sourceFolder);
            }
        }
        return fileList;
    }

    /**
     * Format the file path for zip
     * 
     * @param file
     *            file path
     * @return Formatted file path
     */
    private String generateZipEntry(final String file, final String sourceFolder) {
        return file.substring(sourceFolder.length() + 1, file.length());
    }
}
