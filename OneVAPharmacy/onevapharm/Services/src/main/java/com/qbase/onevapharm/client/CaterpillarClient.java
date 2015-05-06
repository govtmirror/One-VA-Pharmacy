package com.qbase.onevapharm.client;

/*
* #%L
 * * OneVA Pharmacy
 * *
 * %%
 * Copyright (C) 2013 - 2014 Qbase
 * *
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.base.Strings;

import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

import com.google.common.escape.Escaper;

import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;

import com.google.common.net.UrlEscapers;

import com.qbase.onevapharm.config.ActionType;
import com.qbase.onevapharm.config.EndpointConfig;
import com.qbase.onevapharm.config.SiteConfig;
import com.qbase.onevapharm.config.TimeoutConfig;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class description
 *
 *
 * @version        v1.0, 2014-03-13
 * @author         Jim Horner
 */
@ApplicationScoped
public class CaterpillarClient {

    /** Field description */
    private static final String AUTH_HEADER = "Authorization";

    /** Field description */
    private static final String BASIC_HEADER = "Basic";

    /** Field description */
    private final BaseEncoding base64;

    /** Field description */
    private final Escaper escaper;

    /** Field description */
    private final JsonNodeFactory factory;

    /** Field description */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** Field description */
    private final ObjectMapper mapper;

    /**
     * Constructs ...
     *
     */
    public CaterpillarClient() {

        super();
        this.factory = JsonNodeFactory.instance;
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        this.escaper = UrlEscapers.urlPathSegmentEscaper();
        this.base64 = BaseEncoding.base64Url();
    }

    /**
     * Method description
     *
     *
     * @param base
     * @param path
     *
     * @return
     *
     * @throws IOException
     */
    public URL buildURL(URL base, String path) throws IOException {

        String str = String.format("%s%s", base.toString(), path);

        return new URL(str);
    }

    /**
     * Method description
     *
     *
     * @param base
     * @param path
     * @param subscripts
     *
     * @return
     *
     * @throws IOException
     */
    public URL buildURL(URL base, String path, List<String> subscripts)
            throws IOException {

        StringBuilder sb = new StringBuilder();

        sb.append(base.toString());
        sb.append(path);

        if (subscripts.isEmpty() == false) {

            for (String subscript : subscripts) {

                sb.append("/");
                sb.append(this.escaper.escape(subscript));
            }
        }

        return new URL(sb.toString());

    }

    /**
     * Method description
     *
     *
     * @param base
     * @param path
     * @param params
     *
     * @return
     *
     * @throws IOException
     */
    public URL buildURL(URL base, String path, Multimap<String, String> params)
            throws IOException {

        StringBuilder sb = new StringBuilder();

        sb.append(base.toString());
        sb.append(path);

        if (params.isEmpty() == false) {

            boolean first = true;

            for (Map.Entry<String, String> entry : params.entries()) {

                if (first) {

                    sb.append("?");
                    first = false;

                } else {

                    sb.append("&");
                }

                String key = Strings.nullToEmpty(entry.getKey());

                if (key.isEmpty() == false) {

                    String val = Strings.nullToEmpty(entry.getValue());

                    sb.append(String.format("%s=%s", this.escaper.escape(key),
                                            this.escaper.escape(val)));
                }
            }
        }

        return new URL(sb.toString());
    }

    /**
     * Method description
     *
     *
     * @param siteConfig
     * @param action
     * @param url
     *
     * @return
     *
     * @throws IOException
     */
    public Map<String, JsonNode> getAtomJson(SiteConfig siteConfig, ActionType action,
            URL url)
            throws IOException {

        EndpointConfig endpointConfig = siteConfig.getEndpoint();
        TimeoutConfig timeoutConfig = endpointConfig.getTimeoutConfig(action);

        if (endpointConfig.isDebugMode()) {

            logger.debug("ATOM Request {}", url);
        }

        String authstr = String.format("%s:%s", siteConfig.getUserName(),
                                       siteConfig.getPassword());

        String auth64 = this.base64.encode(authstr.getBytes());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setConnectTimeout(timeoutConfig.getConnectTimeout());
        conn.setReadTimeout(timeoutConfig.getRequestTimeout());

        conn.setRequestProperty(AUTH_HEADER,
                                String.format("%s %s", BASIC_HEADER, auth64));
        conn.setRequestMethod("GET");

        SyndFeedInput feedInput = new SyndFeedInput();

        SyndFeed syndFeed = null;

        try {

            syndFeed = feedInput.build(new InputStreamReader(conn.getInputStream()));

        } catch (FeedException e) {

            throw new IOException(e);
        }

        Map<String, JsonNode> result = new HashMap<>();

        for (SyndEntry feedEntry : syndFeed.getEntries()) {

            JsonNode jsonNode = null;
            SyndContent entryContent = Iterables.getFirst(feedEntry.getContents(), null);
            byte[] bytes = null;

            if (entryContent != null) {

                bytes = entryContent.getValue().getBytes();
            }

            if ((bytes != null) && (bytes.length > 0)) {

                jsonNode = this.mapper.readTree(bytes);

            } else {

                ObjectNode node = this.factory.objectNode();

                node.putNull("_value");
                node.put("errorMessage", "Response was empty.");

                jsonNode = node;
            }

            result.put(feedEntry.getUri(), jsonNode);
        }

        return result;
    }

    /**
     * Method description
     *
     *
     *
     *
     * @param siteConfig
     * @param action
     * @param url
     *
     * @return
     *
     * @throws IOException
     */
    public JsonNode getJson(SiteConfig siteConfig, ActionType action, URL url)
            throws IOException {

        EndpointConfig endpointConfig = siteConfig.getEndpoint();
        TimeoutConfig timeoutConfig = endpointConfig.getTimeoutConfig(action);

        if (endpointConfig.isDebugMode()) {

            logger.debug("GET Request {}", url);
        }

        String authstr = String.format("%s:%s", siteConfig.getUserName(),
                                       siteConfig.getPassword());

        String auth64 = this.base64.encode(authstr.getBytes());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setConnectTimeout(timeoutConfig.getConnectTimeout());
        conn.setReadTimeout(timeoutConfig.getRequestTimeout());

        conn.setRequestProperty(AUTH_HEADER,
                                String.format("%s %s", BASIC_HEADER, auth64));
        conn.setRequestMethod("GET");

        JsonNode result = null;
        byte[] bytes = ByteStreams.toByteArray(conn.getInputStream());

        if ((bytes != null) && (bytes.length > 0)) {

            result = this.mapper.readTree(bytes);

        } else {

            ObjectNode node = this.factory.objectNode();

            node.putNull("_value");
            node.put("errorMessage", "Response was empty.");

            result = node;
        }

        if (endpointConfig.isDebugMode()) {

            logger.debug("GET Response {}", result.toString());
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param siteConfig
     * @param action
     * @param url
     * @param postJson
     *
     * @return
     *
     * @throws IOException
     */
    public JsonNode postJson(SiteConfig siteConfig, ActionType action, URL url,
                             JsonNode postJson)
            throws IOException {

        return pushJson("POST", siteConfig, action, url, postJson);
    }

    /**
     * Method description
     *
     *
     * @param requestMethod
     * @param siteConfig
     * @param action
     * @param url
     * @param pushJson
     *
     * @return
     *
     * @throws IOException
     */
    private JsonNode pushJson(String requestMethod, SiteConfig siteConfig,
                              ActionType action, URL url, JsonNode pushJson)
            throws IOException {

        EndpointConfig endpointConfig = siteConfig.getEndpoint();
        TimeoutConfig timeoutConfig = endpointConfig.getTimeoutConfig(action);

        if (endpointConfig.isDebugMode()) {

            logger.debug("{} Request {}\n{}", requestMethod, url, pushJson.toString());
        }

        String authstr = String.format("%s:%s", siteConfig.getUserName(),
                                       siteConfig.getPassword());

        String auth64 = this.base64.encode(authstr.getBytes());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestProperty(AUTH_HEADER,
                                String.format("%s %s", BASIC_HEADER, auth64));

        conn.setDoOutput(true);

        conn.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
        conn.setRequestMethod(requestMethod);
        conn.setConnectTimeout(timeoutConfig.getConnectTimeout());
        conn.setReadTimeout(timeoutConfig.getRequestTimeout());

        conn.connect();

        conn.getOutputStream().write(this.mapper.writeValueAsBytes(pushJson));

        JsonNode result = null;
        byte[] bytes = ByteStreams.toByteArray(conn.getInputStream());

        if ((bytes != null) && (bytes.length > 0)) {

            result = this.mapper.readTree(bytes);

        } else {

            ObjectNode node = this.factory.objectNode();

            node.putNull("_value");
            node.put("errorMessage", "Response was empty.");

            result = node;
        }

        if (endpointConfig.isDebugMode()) {

            logger.debug("{} Response {}", requestMethod, result.toString());
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param siteConfig
     * @param action
     * @param url
     * @param putJson
     *
     * @return
     *
     * @throws IOException
     */
    public JsonNode putJson(SiteConfig siteConfig, ActionType action, URL url,
                            JsonNode putJson)
            throws IOException {

        return pushJson("PUT", siteConfig, action, url, putJson);
    }
}
