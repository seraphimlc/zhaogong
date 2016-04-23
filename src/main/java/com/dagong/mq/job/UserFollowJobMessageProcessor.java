package com.dagong.mq.job;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.dagong.mq.MessageProcessor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuchang on 16/4/16.
 */
@Service
public class UserFollowJobMessageProcessor extends MessageProcessor {
    public UserFollowJobMessageProcessor() {
        this.setTopic("job");
        this.setTag("followJob");
    }

    @Override
    protected void process(List<MessageExt> list) {

    }
}
