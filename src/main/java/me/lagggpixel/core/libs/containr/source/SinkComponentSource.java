/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.libs.containr.source;

import me.lagggpixel.core.libs.containr.ComponentTunnel;
import me.lagggpixel.core.libs.containr.Component;
import reactor.core.publisher.Sinks;

import static reactor.core.publisher.Sinks.many;

/**
 * @author ZorTik
 * @since January 22, 2024
 */
public class SinkComponentSource extends SimpleComponentSource {

  private final Sinks.Many<Component> attachedSink;
  private Sinks.EmitFailureHandler emitFailureHandler;
  private boolean completed;

  public <T> SinkComponentSource(Sinks.Many<T> diffSink, ComponentMapper<T> mapper) {
    this(many().multicast().directBestEffort());
    this.emitFailureHandler = Sinks.EmitFailureHandler.FAIL_FAST;
    diffSink.asFlux().subscribe(anyObject -> {
      attachedSink.emitNext(mapper.map(anyObject), emitFailureHandler);
    });
  }

  public SinkComponentSource(Sinks.Many<Component> sink) {
    this.attachedSink = sink;
    this.completed = false;

    attachedSink.asFlux().subscribe(this::publish);
  }

  public void setEmitFailureHandler(Sinks.EmitFailureHandler emitFailureHandler) {
    this.emitFailureHandler = emitFailureHandler;
  }

  @Override
  public boolean enable(ComponentTunnel tunnel) {
    if (completed)
      return false;

    return super.enable(tunnel);
  }

  @Override
  public void disable(ComponentTunnel tunnel) {
    super.disable(tunnel);
    if (!completed && getTunnels().isEmpty() && attachedSink.tryEmitComplete().isSuccess())
      completed = true;
  }

  /**
   *  @author Lagggpixel
   * @since January 27, 2024 January 22, 2024
   */
  public interface ComponentMapper<T> {
    Component map(T anyObject);
  }
}
