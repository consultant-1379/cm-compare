package com.ericsson.oss.services.cm.export.service.batch.myjob2;

import java.io.Serializable;
import java.util.StringTokenizer;

import javax.batch.api.chunk.AbstractItemReader;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 * 
 */
@Dependent
@Named
public class MyItemReader extends AbstractItemReader {

    private StringTokenizer tokens;

    @Override
    public void open(final Serializable checkpoint) throws Exception {
        this.tokens = new StringTokenizer("1,2,3,4,5,6,7,8,9,10", ",");
    }

    @Override
    public MyInputRecord readItem() {
        if (this.tokens.hasMoreTokens()) {
            return new MyInputRecord(Integer.valueOf(this.tokens.nextToken()));
        }
        return null;
    }
}
