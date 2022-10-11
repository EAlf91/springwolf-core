package io.github.stavshamir.springwolf.example.configuration;

import com.asyncapi.v2.model.info.Info;
import com.asyncapi.v2.model.server.Server;
import io.github.stavshamir.springwolf.asyncapi.DefaultAsyncApiSerializerService;
import io.github.stavshamir.springwolf.asyncapi.types.SQSConsumerData;
import io.github.stavshamir.springwolf.asyncapi.types.SQSProducerData;
import io.github.stavshamir.springwolf.configuration.AsyncApiDocket;
import io.github.stavshamir.springwolf.configuration.EnableAsyncApi;
import io.github.stavshamir.springwolf.example.dtos.AnotherPayloadDto;
import io.github.stavshamir.springwolf.example.dtos.ExamplePayloadDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableAsyncApi
@Slf4j
public class AsyncApiConfiguration {

    private final String sqs_queue = "url://sqs_endpoint";


    @Bean
    public AsyncApiDocket asyncApiDocket() {
        Info info = Info.builder()
                .version("v1")
                .title("Customer and Partner Communication Service Messages API")
                .description(" The Customer and Partner Communication API allows product teams at Otto Payments to communicate with customers and partners.")
                .build();

        return AsyncApiDocket.builder()
                .basePackage("io.github.stavshamir.springwolf.example.consumers")
                .info(info)
                .servers(ServerConfiguration.configure())
                //.producer(producerData)
                //.consumer(consumerData)
                .build();

    }

}
