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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAsyncApi
public class AsyncApiConfiguration {

    private final String sqs_queue = "url://sqs_endpoint";


    @Bean
    public AsyncApiDocket asyncApiDocket() {
        Info info = Info.builder()
                .version("1.0.0")
                .title("Springwolf example project - SQS")
                .build();



        Server amqp = Server.builder()
                .protocol("sqs")
                .url(String.format("%s", sqs_queue))
                .build();

        //TODO: finish


        SQSConsumerData consumerData =SQSConsumerData.sqsConsumerDataBuilder()
                .payloadType(ExamplePayloadDto.class)
                .queueName("example-queue")
                .description("handles example files from example-queue")
                .build();

        SQSProducerData producerData = SQSProducerData.sqsProducerDataBuilder()
                .queueName("producer-queue")
                .description("producer channel")
                .build();


        return AsyncApiDocket.builder()
                .basePackage("io.github.stavshamir.springwolf.example.consumers")
                .info(info)
                .server("amqp", amqp)

                //.producer(producerData)
                //.consumer(consumerData)
                .build();
    }

}
