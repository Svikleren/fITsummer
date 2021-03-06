/*
 * Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.fITsummer;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.api.services.oauth2.model.Userinfoplus;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Command-line sample for the Google OAuth2 API described at <a
 * href="http://code.google.com/apis/accounts/docs/OAuth2Login.html">Using OAuth 2.0 for Login
 * (Experimental)</a>.
 *
 * @author Yaniv Inbar
 */
public class OAuth2Sample {

    /**
     * Be sure to specify the name of your application. If the application name is {@code null} or
     * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
     */
    public static final String APPLICATION_NAME = "fITsummer";

    /**
     * Directory to store user credentials.
     */
    public static final java.io.File DATA_STORE_DIR =
            new java.io.File(System.getProperty("user.home"), ".store/oauth2_sample");

    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
     * globally shared instance across your application.
     */
    public static FileDataStoreFactory dataStoreFactory;

    /**
     * Global instance of the HTTP transport.
     */
    public static HttpTransport httpTransport;

    /**
     * Global instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * OAuth 2.0 scopes.
     */
    public static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/userinfo.profile",
            "https://www.googleapis.com/auth/userinfo.email",
            "https://www.googleapis.com/auth/fitness.activity.read");

    public static Oauth2 oauth2;
    public static GoogleClientSecrets clientSecrets;

    public static String accessToken = "";

    public static String clientSecret = "GpxRtaBnTXlkylXzbvDHCZSN";
    public static String clientID = "123456648359-ob0vigogq1j28v9j804787e9tvlh0pa2.apps.googleusercontent.com";

    /**
     * Authorizes the installed application to access user's protected data.
     */
    public static Credential authorize() throws Exception {
        // load client secrets
        clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(OAuth2Sample.class.getResourceAsStream("/client_secrets.json")));
/*        clientSecrets=GoogleClientSecrets.load().getDetails().setClientId(clientID);
        clientSecrets.getDetails().setClientSecret(clientSecret);*/
        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
                || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            System.out.println("Enter Client ID and Secret from https://code.google.com/apis/console/ "
                    + "into oauth2-cmdline-sample/src/main/resources/client_secrets.json");
            System.exit(1);
        }
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport,
                JSON_FACTORY,
                clientSecrets,
                SCOPES)
/*                .setCredentialCreatedListener(new AuthorizationCodeFlow.CredentialCreatedListener() {
            @Override
            public void onCredentialCreated(Credential credential, TokenResponse tokenResponse) throws IOException {
                //redirect /
                System.out.println("AAAAAAAAAAAAAAA " + tokenResponse.getAccessToken());
            }
        })*/
                //.setDataStoreFactory(dataStoreFactory)
                .build();
        flow.newAuthorizationUrl().setRedirectUri("http://localhost:8080/abc");
        // authorize
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().build();

        AuthorizationCodeInstalledApp app = new AuthorizationCodeInstalledApp(flow, receiver);

        return app.authorize("user");

    }

/*    public static void main(String[] args) {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            // authorization
            Credential credential = authorize();
            // set up global Oauth2 instance
            oauth2 = new Oauth2.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
                    APPLICATION_NAME).build();
            // run commands
            tokenInfo(credential.getAccessToken());
            userInfo();
            // success!
            return;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(1);
    }*/

    public static void tokenInfo(String accessToken) throws IOException {
        header("Validating a token");
        System.out.println(accessToken);
        OAuth2Sample.accessToken = accessToken;
        Tokeninfo tokeninfo = oauth2.tokeninfo().setAccessToken(accessToken).execute();
        System.out.println(tokeninfo.toPrettyString());
        if (!tokeninfo.getAudience().equals(clientSecrets.getDetails().getClientId())) {
            System.err.println("ERROR: audience does not match our client ID!");
        }
    }

    public static void userInfo() throws IOException {
        header("Obtaining User Profile Information");
        Userinfoplus userinfo = oauth2.userinfo().get().execute();
        System.out.println(userinfo.toPrettyString());
    }

    static void header(String name) {
        System.out.println();
        System.out.println("================== " + name + " ==================");
        System.out.println();
    }
}
