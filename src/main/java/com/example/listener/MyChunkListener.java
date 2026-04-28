package com.example.listener;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyChunkListener implements ChunkListener {

	@Override
	public void beforeChunk(ChunkContext context) {
		log.info("beforechunk() executed");
	}

	@Override
	public void afterChunk(ChunkContext context) {
		log.info("afterChunk() executed");
	}

	@Override
	public void afterChunkError(ChunkContext context) {
		log.info("afterChunkError() executed");
	}

}
