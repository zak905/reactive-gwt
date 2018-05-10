package com.gwidgets.demo.client;

import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.*;
import io.reactivex.Observable;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class ReactiveGWT implements EntryPoint {

  final HTMLInputElement urlInput = (HTMLInputElement) DomGlobal.document.getElementById("url");
  final HTMLSelectElement methodInput = (HTMLSelectElement) DomGlobal.document.getElementById("method");
  final HTMLTextAreaElement dataInput = (HTMLTextAreaElement) DomGlobal.document.getElementById("data");
  final HTMLElement generatedCommand = (HTMLElement) DomGlobal.document.getElementById("generatedCommand");

  public void onModuleLoad() {
    Observable<Command> methodStream = fromEvent(methodInput, "change")
        .map(ev -> new Command(1, "-X" + ((HTMLSelectElement) ev.target).value));

    Observable<Command> urlStream = fromEvent(urlInput, "keyup")
        .map(ev -> new Command(2, ((HTMLInputElement) ev.target).value));

    Observable<Command> dataStream = fromEvent(dataInput, "keyup")
        .map(ev -> new Command(3, "-d '" + ((HTMLTextAreaElement) ev.target).value + "'"));

    Observable.merge(urlStream, methodStream, dataStream)
        .scan(new String[3], (acc, n) -> { acc[n.position - 1] = n.value; return acc; })
        .subscribe(n -> generatedCommand.textContent = Stream.of(n).filter(Objects::nonNull).collect(joining(" ")));
  }

  static class Command {
    final int position;
    final String value;

    Command(int position, String value) {
      this.position = position;
      this.value = value;
    }
  }

  static Observable<Event> fromEvent(EventTarget source, String type) {
    return Observable.create(s -> {
      EventListener listener = s::onNext;
      source.addEventListener(type, listener);
      s.setCancellable(() -> source.removeEventListener(type, listener));
    });
  }
}
