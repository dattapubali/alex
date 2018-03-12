/*
 * Copyright 2018 TU Dortmund
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.learnlib.alex.webhooks.dao;

import de.learnlib.alex.auth.entities.User;
import de.learnlib.alex.common.exceptions.NotFoundException;
import de.learnlib.alex.webhooks.entities.EventType;
import de.learnlib.alex.webhooks.entities.Webhook;
import de.learnlib.alex.webhooks.repositories.WebhookRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The implementation of the {@link WebhookDAO}.
 */
@Service
public class WebhookDAOImpl implements WebhookDAO {

    /**
     * The repository for webhooks.
     */
    private WebhookRepository webhookRepository;

    /**
     * Constructor.
     *
     * @param webhookRepository The injected repository for webhooks.
     */
    @Inject
    public WebhookDAOImpl(WebhookRepository webhookRepository) {
        this.webhookRepository = webhookRepository;
    }

    @Override
    @Transactional
    public void create(User user, Webhook webhook) throws ValidationException {
        if (webhookRepository.findByUser_IdAndUrl(user.getId(), webhook.getUrl()) != null) {
            throw new ValidationException("A webhook under the given URL is already registered. Update the existing one instead.");
        }

        final Long highestId = webhookRepository.findHighestId(user.getId());
        webhook.setId(highestId == null ? 0 : highestId + 1);

        webhook.setUser(user);
        webhookRepository.save(webhook);
    }

    @Override
    @Transactional
    public List<Webhook> getAll(User user) {
        final List<Webhook> webhooks = webhookRepository.findByUser_id(user.getId());
        webhooks.forEach(this::loadLazyRelations);
        return webhooks;
    }

    @Override
    @Transactional
    public List<Webhook> getByUserAndEvent(User user, EventType event) {
        final List<Webhook> webhooks = webhookRepository.findByUser_id(user.getId()).stream()
                .filter(webhook -> webhook.getEvents().contains(event))
                .collect(Collectors.toList());
        webhooks.forEach(this::loadLazyRelations);
        return webhooks;
    }

    @Override
    @Transactional
    public void delete(User user, Long id) throws NotFoundException {
        final Webhook webhook = webhookRepository.findByUser_IdAndId(user.getId(), id);
        if (webhook == null) {
            throw new NotFoundException("The webhook with the id " + id + " could not be found.");
        }
        webhookRepository.delete(webhook);
    }

    @Override
    @Transactional
    public void delete(User user, List<Long> ids) throws NotFoundException {
        for (Long id : ids) {
            delete(user, id);
        }
    }

    @Override
    @Transactional
    public void update(User user, Webhook webhook) throws NotFoundException, ValidationException {
        if (!user.getId().equals(webhook.getUser().getId())) {
            throw new ValidationException("You are not the owner of the webhook.");
        }

        final Webhook webhookInDB = webhookRepository.findByUser_IdAndId(user.getId(), webhook.getId());
        if (webhookInDB == null) {
            throw new NotFoundException("The webhook with the id " + webhook.getId() + " could not be found.");
        }

        // check if there is another webhook registered to the new URL.
        final Webhook webhookWithSameUrl = webhookRepository.findByUser_IdAndUrl(user.getId(), webhook.getUrl());
        if (webhookWithSameUrl != null && !webhook.getId().equals(webhookWithSameUrl.getId())) {
            throw new ValidationException("Another webhook is already registered to the URL.");
        }

        webhookInDB.setEvents(webhook.getEvents());
        webhookInDB.setUrl(webhook.getUrl());
        webhookInDB.setName(webhook.getName());

        webhookRepository.save(webhookInDB);
    }

    private void loadLazyRelations(Webhook webhook) {
        Hibernate.initialize(webhook.getEvents());
    }
}