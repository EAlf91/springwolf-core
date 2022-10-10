package io.github.stavshamir.springwolf.asyncapi;

import io.github.stavshamir.springwolf.producer.SpringwolfSQSProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/springwolf/amqp")
@RequiredArgsConstructor
public class SpringwolfSQSController {

    private final SpringwolfSQSProducer amqpProducer;

    @PostMapping("/publish")
    public void publish(@RequestParam String topic, @RequestBody Map<String, Object> payload) {
        if (amqpProducer == null) {
            log.warn("AMQP producer is not enabled - message will not be published");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AMQP producer is not enabled");
        }
        log.info("Publishing to amqp queue {}: {}", topic, payload);
        amqpProducer.send(topic, payload);
    }

}
