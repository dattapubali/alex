/*
 * Copyright 2015 - 2019 TU Dortmund
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

package de.learnlib.alex.integrationtests.resources.api;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SymbolParameterApi extends AbstractApi {

    public SymbolParameterApi(Client client, int port) {
        super(client, port);
    }

    public Response create(int projectId, int symbolId, String parameter, String jwt) {
        return client.target(url(projectId, symbolId)).request()
                .header(HttpHeaders.AUTHORIZATION, jwt)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .post(Entity.json(parameter));
    }

    public Response update(int projectId, int symbolId, int parameterId, String parameter, String jwt) {
        return client.target(url(projectId, symbolId) + "/" + parameterId).request()
                .header(HttpHeaders.AUTHORIZATION, jwt)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .put(Entity.json(parameter));
    }

    public Response delete(int projectId, int symbolId, int parameterId, String jwt) {
        return client.target(url(projectId, symbolId) + "/" + parameterId).request()
                .header(HttpHeaders.AUTHORIZATION, jwt)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .delete();
    }

    public String url(int projectId, int symbolId) {
        return baseUrl() + "/projects/" + projectId + "/symbols/" + symbolId + "/parameters";
    }
}
