package io.manco.maxim.sbmm.core.rabbitMQ;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    public void receiveMessage(BigOpertaion bigOpertaion){
        System.out.println(bigOpertaion.getAccessToken());
    }
}
