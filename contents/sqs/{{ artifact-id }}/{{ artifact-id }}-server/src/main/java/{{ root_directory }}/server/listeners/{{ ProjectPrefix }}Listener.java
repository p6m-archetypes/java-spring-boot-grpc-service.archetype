package {{ root_package }}.server.listeners;

import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class {{ ProjectPrefix }}Listener {

    private static final Logger logger = LoggerFactory.getLogger({{ ProjectPrefix }}Listener.class);

    @SqsListener(value = "${sqs.queues.inbound}")
    public void receive(String message) {
        logger.info("Message received: {}", message);
    }
}
