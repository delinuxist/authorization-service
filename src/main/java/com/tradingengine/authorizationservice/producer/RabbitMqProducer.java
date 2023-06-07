package com.tradingengine.authorizationservice.producer;

import com.tradingengine.authorizationservice.dto.UserReportDto;
import com.tradingengine.authorizationservice.enums.QueueRoutingKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMqProducer {

    @Value("${rabbitMq.exchange.user-exchange}")
    private String user_exchange;

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(UserReportDto userReportDto) {
        rabbitTemplate.convertAndSend(user_exchange, QueueRoutingKeys.USER_ROUTE_KEY.getKey(),userReportDto);
    }

}
