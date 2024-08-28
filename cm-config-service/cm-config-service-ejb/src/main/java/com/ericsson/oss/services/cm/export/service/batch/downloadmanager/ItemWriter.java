/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.oss.services.cm.export.service.batch.downloadmanager;

import java.io.Serializable;
import java.util.List;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Named;

@Named("FileItemWriter")
public class ItemWriter extends AbstractItemWriter {

    @Override
    public void open(final Serializable checkpoint) throws Exception {
        System.out.println("open item writing stage");
    }

    @Override
    public void writeItems(final List list) throws Exception {
        System.out.println("Write " + list.size() + " file items");
        for (final Object obj : list) {
            System.out.println("Write file item " + obj);
        }
    }

}
