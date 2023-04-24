package com.learn.netty.basic;

import junit.framework.TestCase;

public class BlockingIoExampleTest extends TestCase {

    public void testServe() {
        BlockingIoExample example = new BlockingIoExample();
        example.serve(22);
    }
}