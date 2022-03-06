package com.warrenbuffett.server;

import com.warrenbuffett.server.common.S3Uploader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class S3Test extends ServerApplicationTests{
    @Autowired
    private S3Uploader s3Uploader;


    @Test
    void uploadTest(){
        File file = new File("/Users/koiil/IdeaProjects/server/src/test/java/com/warrenbuffett/server/docker.png");
        s3Uploader.upload(file,"static");

    }
}
