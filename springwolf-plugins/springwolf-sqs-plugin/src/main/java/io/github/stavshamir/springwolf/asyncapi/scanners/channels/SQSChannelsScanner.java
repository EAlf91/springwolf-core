package io.github.stavshamir.springwolf.asyncapi.scanners.channels;

import com.asyncapi.v2.binding.ChannelBinding;
import com.asyncapi.v2.binding.OperationBinding;
import com.asyncapi.v2.binding.amqp.AMQPChannelBinding;
import com.asyncapi.v2.binding.amqp.AMQPOperationBinding;
import com.asyncapi.v2.binding.sqs.SQSChannelBinding;
import com.asyncapi.v2.binding.sqs.SQSOperationBinding;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.StringValueResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class SQSChannelsScanner extends AbstractChannelScanner<SqsListener>
        implements ChannelsScanner, EmbeddedValueResolverAware {

    private final Map<String, Binding> bindingsMap;
    private StringValueResolver resolver;

    public SQSChannelsScanner(List<Binding> bindings) {
        bindingsMap = bindings.stream()
                .filter(Binding::isDestinationQueue)
                .collect(Collectors.toMap(Binding::getDestination, Function.identity()));
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    protected Class<SqsListener> getListenerAnnotationClass() {
        return SqsListener.class;
    }

    @Override
    protected String getChannelName(SqsListener annotation) {
        if (annotation.value().length > 0) {
            return getChannelNameFromQueues(annotation);
        }

        return getChannelNameFromBindings(annotation);
    }

    private String getChannelNameFromQueues(SqsListener annotation) {
        return Arrays.stream(annotation.value())
                .map(resolver::resolveStringValue)
                .peek(queue -> log.debug("Resolved queue name from queue: {}", queue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No queue name was found in @RabbitListener annotation"));
    }

    private String getChannelNameFromBindings(SqsListener annotation) {
        return Arrays.stream(annotation.value())
                .map(resolver::resolveStringValue)
                .peek(queue -> log.debug("Resolved queue name from binding: {}", queue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No queue name was found in @RabbitListener annotation"));
    }

    @Override
    protected Map<String, ? extends ChannelBinding> buildChannelBinding(SqsListener annotation) {

        return ImmutableMap.of("sqs", new SQSCustomChannelBinding());
    }

    private String getExchangeName(SqsListener annotation) {
        String exchangeName = Stream.of(annotation.value())
                .findFirst()
                .orElse(null);
        if (exchangeName == null && bindingsMap.containsKey(getChannelName(annotation))) {
            Binding binding = bindingsMap.get(getChannelName(annotation));
            exchangeName = binding.getExchange();
        }
        if (exchangeName == null) {
            exchangeName = "";
        }

        return exchangeName;
    }

    @Override
    protected Map<String, ? extends OperationBinding> buildOperationBinding(SqsListener annotation) {

        return ImmutableMap.of("sqs", new SQSCustomOperationBinding("test"));
    }

    /*private List<String> getRoutingKeys(SqsListener annotation) {

        List<String> routingKeys = Stream.of(annotation.bindings())
                .map(binding -> {
                    if (binding.key().length == 0) {
                        return Collections.singletonList("");
                    }

                    return Arrays.asList(binding.key());
                })
                .findFirst()
                .orElse(null);
        if (routingKeys == null && bindingsMap.containsKey(getChannelName(annotation))) {
            Binding binding = bindingsMap.get(getChannelName(annotation));
            routingKeys = Collections.singletonList(binding.getRoutingKey());
        }

        String exchangeName = getExchangeName(annotation);
        if (exchangeName.isEmpty() && routingKeys == null) {
            routingKeys = Collections.singletonList(getChannelName(annotation));
        }

        return routingKeys;
    }
    */
    @Override
    protected Class<?> getPayloadType(Method method) {
        String methodName = String.format("%s::%s", method.getDeclaringClass().getSimpleName(), method.getName());
        log.debug("Finding payload type for {}", methodName);

        Class<?>[] parameterTypes = method.getParameterTypes();
        switch (parameterTypes.length) {
            case 0:
                throw new IllegalArgumentException("Listener methods must not have 0 parameters: " + methodName);
            case 1:
                return parameterTypes[0];
            default:
                return getPayloadType(parameterTypes, method.getParameterAnnotations(), methodName);
        }
    }

    private Class<?> getPayloadType(Class<?>[] parameterTypes, Annotation[][] parameterAnnotations, String methodName) {
        int payloadAnnotatedParameterIndex = getPayloadAnnotatedParameterIndex(parameterAnnotations);

        if (payloadAnnotatedParameterIndex == -1) {
            String msg = "Multi-parameter RabbitListener methods must have one parameter annotated with @Payload, "
                    + "but none was found: "
                    + methodName;

            throw new IllegalArgumentException(msg);
        }

        return parameterTypes[payloadAnnotatedParameterIndex];
    }

    private int getPayloadAnnotatedParameterIndex(Annotation[][] parameterAnnotations) {
        for (int i = 0, length = parameterAnnotations.length; i < length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            boolean hasPayloadAnnotation = Arrays.stream(annotations)
                    .anyMatch(annotation -> annotation instanceof Payload);

            if (hasPayloadAnnotation) {
                return i;
            }
        }

        return -1;
    }

}
