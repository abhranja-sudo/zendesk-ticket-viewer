package com.zendesk.client.v1.service;

import com.zendesk.client.v1.Input;
import com.zendesk.client.v1.controller.Controller;
import com.zendesk.client.v1.model.viewframe.Frame;

public abstract class Service {

    protected Controller controller;

    public Service(Controller controller) {
        this.controller = controller;
    }

    public abstract Frame execute(Input input);
}
