package io.github.stavshamir.springwolf.producer;

import com.asyncapi.v2.binding.amqp.AMQPChannelBinding;
import com.asyncapi.v2.binding.amqp.AMQPOperationBinding;
import com.asyncapi.v2.model.channel.ChannelItem;
import com.asyncapi.v2.model.channel.operation.Operation;
import io.github.stavshamir.springwolf.asyncapi.ChannelsService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SpringwolfSQSProducer {

    private final ChannelsService channelsService;
    private final RabbitTemplate rabbitTemplate;

    @Getter
    private boolean isEnabled = true;

    public SpringwolfSQSProducer(ChannelsService channelsService, List<RabbitTemplate> rabbitTemplates) {
        this.channelsService = channelsService;
        this.rabbitTemplate = rabbitTemplates.get(0);
    }

    public void send(String channelName, Map<String, Object> payload) {
        ChannelItem channelItem = channelsService.getChannels().get(channelName);

        String exchange = getExchangeName(channelItem);
        String routingKey = getRoutingKey(channelItem);
        if (routingKey.isEmpty() && exchange.isEmpty()) {
            routingKey = channelName;
        }

        rabbitTemplate.convertAndSend(exchange, routingKey, payload);
    }

    private String getExchangeName(ChannelItem channelItem) {
        String exchange = "";
        if (channelItem.getBindings() != null && channelItem.getBindings().containsKey("sqs")) {
            AMQPChannelBinding channelBinding = (AMQPChannelBinding) channelItem.getBindings().get("sqs");
            if (channelBinding.getExchange() != null && channelBinding.getExchange().getName() != null) {
                exchange = channelBinding.getExchange().getName();
            }
        }

        return exchange;
    }

    private String getRoutingKey(ChannelItem channelItem) {
        String routingKey = "";
        Operation operation = channelItem.getSubscribe() != null ? channelItem.getSubscribe() : channelItem.getPublish();
        if (operation != null && operation.getBindings() != null && operation.getBindings().containsKey("sqs")) {
            AMQPOperationBinding operationBinding = (AMQPOperationBinding) operation.getBindings().get("sqs");
            if (!CollectionUtils.isEmpty(operationBinding.getCc())) {
                routingKey = operationBinding.getCc().get(0);
            }
        }

        return routingKey;
    }

}
