package com.gwidgets.demo.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import elemental2.dom.*;
import io.reactivex.Observable;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ReactiveGWT implements EntryPoint {

  HTMLInputElement urlInput = (HTMLInputElement) DomGlobal.document.getElementById("url");
  HTMLSelectElement methodInput = (HTMLSelectElement) DomGlobal.document.getElementById("method");
  HTMLTextAreaElement dataInput = (HTMLTextAreaElement) DomGlobal.document.getElementById("data");
  HTMLElement generatedCommand = (HTMLElement) DomGlobal.document.getElementById("generatedCommand");
  HTMLInputElement repositoryInput = (HTMLInputElement) DomGlobal.document.getElementById("repository");
  final String[] commands = new String[3];

  public void onModuleLoad() {

    Observable<Command> urlStream = Observable.create((emitter) -> {
      urlInput.onkeyup = (event) -> {
        HTMLInputElement urlInputTarget = (HTMLInputElement) event.target;
        emitter.onNext(new Command(2, urlInputTarget.value));
        return null;
      };
    });

    Observable<Command> methodStream = Observable.create((emitter) -> {
      methodInput.onchange = (event) -> {
        HTMLSelectElement methodSelect = (HTMLSelectElement) event.target;
        emitter.onNext(new Command(1, "-X"+methodSelect.value));
        return null;
      };
    });


    Observable<Command> dataStream = Observable.create((emitter) -> {
      dataInput.onkeyup = (event) -> {
        HTMLTextAreaElement dataInputTarget = (HTMLTextAreaElement) event.target;
        emitter.onNext(new Command(3, "-d '"+dataInputTarget.value+"'"));
        return null;
      };
    });

    Observable.merge(urlStream, methodStream, dataStream).subscribe((obs) -> {
      commands[obs.position - 1] = obs.value;
      generatedCommand.textContent = String.join(" ", Stream.of(commands).filter(Objects::nonNull).collect(Collectors.toList()));
    });
  }
}