module org.hakai.framework {
    exports org.kahai.framework.transients;
    exports org.kahai.framework.models;
    exports org.kahai.framework.services;
    exports org.kahai.framework.services.strategies;
    exports org.kahai.framework.services.queue;
    exports org.kahai.framework.events;
    exports org.kahai.framework.annotations;
    exports org.kahai.framework.dtos.request;
    exports org.kahai.framework.dtos.response;
    exports org.kahai.framework.errors;

    opens org.kahai.framework to org.hibernate.orm.core, org.springframework.beans, org.springframework.core;

    requires transitive org.apache.commons.lang3;
    requires transitive spring.boot.starter;
    requires transitive spring.boot.autoconfigure;
    requires transitive spring.boot;
    requires transitive spring.data.jpa;
    requires transitive spring.web;
    requires transitive spring.beans;
    requires transitive spring.aop;
    requires transitive spring.aspects;
    requires transitive spring.webmvc;
    requires transitive spring.websocket;
    requires transitive spring.context;
    requires transitive spring.core;
    requires transitive spring.security.config;
    requires transitive spring.security.web;
    requires transitive spring.security.crypto;
    requires transitive spring.security.core;
    requires transitive spring.messaging;
    requires transitive spring.boot.devtools;
    requires transitive lombok;
    requires transitive java.net.http;
    requires transitive com.fasterxml.jackson.databind;
    requires transitive jakarta.annotation;
    requires transitive jakarta.persistence;
    requires transitive jakarta.transaction;
    requires transitive jakarta.servlet;
    requires transitive com.auth0.jwt;
    requires transitive spring.data.commons;
    requires transitive jakarta.validation;
    requires transitive org.slf4j;
}