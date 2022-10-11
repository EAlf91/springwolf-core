package io.github.stavshamir.springwolf.asyncapi.types;

import com.asyncapi.v2.binding.amqp.AMQPChannelBinding;
import com.asyncapi.v2.binding.amqp.AMQPOperationBinding;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Collections;

@AllArgsConstructor
public class SQSProducerData extends ProducerData {



    @Builder(builderMethodName = "amqpProducerDataBuilder")
    public SQSProducerData(String queueName, String exchangeName, String routingKey, Class<?> payloadType, String description) {
        this.channelName = queueName;
        this.description = description;

        AMQPChannelBinding.ExchangeProperties exchangeProperties = new AMQPChannelBinding.ExchangeProperties();
        exchangeProperties.setName(exchangeName);

        this.channelBinding = ImmutableMap.of("sqs", AMQPChannelBinding.builder()
                .is("routingKey")
                .exchange(exchangeProperties)
                .build());

        this.operationBinding = ImmutableMap.of("sqs", AMQPOperationBinding.builder()
                .cc(Collections.singletonList(routingKey))
                .build());

        this.payloadType = payloadType;
    }

}
