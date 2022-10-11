package io.github.stavshamir.springwolf.example;


import io.github.stavshamir.springwolf.example.configuration.AsyncApiConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AsyncApiIT {

    @Autowired
    private AsyncApiConfiguration scannerController;

    @Test
    public void testAsyncRetrieval(){

        Assert.assertEquals("",scannerController.asyncApiDocket().getConsumers().toString());
    }

}
