package io.github.stavshamir.springwolf.asyncapi.types;

import com.asyncapi.v2.binding.amqp.AMQPChannelBinding;
import com.asyncapi.v2.binding.amqp.AMQPOperationBinding;
import com.asyncapi.v2.binding.sqs.SQSChannelBinding;
import com.asyncapi.v2.binding.sqs.SQSOperationBinding;
import com.google.common.collect.ImmutableMap;
import lombok.Builder;

import java.util.Collections;

public class SQSConsumerData extends ConsumerData {

    @Builder(builderMethodName = "sqsConsumerDataBuilder")
    public SQSConsumerData(String queueName, Class<?> payloadType, String description) {
        this.channelName = queueName;
        this.payloadType = payloadType;


        this.channelBinding = ImmutableMap.of("sqs", new SQSChannelBinding());

        this.operationBinding = ImmutableMap.of("sqs", new SQSOperationBinding());




        this.payloadType = payloadType;
    }

}
