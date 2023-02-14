package com.codueon.boostUp.global.batch.tasklet;

import com.codueon.boostUp.domain.chat.service.ChatService;
import com.codueon.boostUp.domain.chat.service.SaveChatBatchService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageTasklet implements Tasklet {
    private final SaveChatBatchService chatBatchService;

    @Override
    @SneakyThrows
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        log.info("[START] Start save In-Memory ChatMessage");
        chatBatchService.saveInMemoryChatMessagesToRdb();
        log.info("[END] In-Memory ChatMessage saved to RDB");
        return RepeatStatus.FINISHED;
    }
}
