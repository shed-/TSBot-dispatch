/**
 * Copyright (c) 2015 NameHidden
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tsbot.management;


import com.github.theholywaffle.teamspeak3.TS3Api;
import com.tsbot.io.conversation.ConReader;
import com.tsbot.login.security.Credential;

import java.io.IOException;

/**
 *
 * @author NameHidden
 * @since April 09, 2015.
 */
public class Management {


    /**
     * Constructor for {@link Management}.
     * Once the user has been validated with the supplied credentials, this is the starting point
     * to initialize all the management features offered for the user.
     *
     * @param api The {@link TS3Api} object needed for communication with the teamspeak server
     * @param botNickname The {@link Credential} object that holds the bot nickname.
     */
    public Management(TS3Api api, Credential botNickname) {
        try {
            prepareEnvironment();
        } catch (IOException e) {
            e.printStackTrace();
        }

        api.setNickname(botNickname.toString());

        TSControl control = new TSControl(api, botNickname.toString());
        control.setVisible(true);
        control.setLocationRelativeTo(null);
    }

    /**
     * Prepares the parent folder in order to start writing files into it.
     */
    private void prepareEnvironment() throws IOException {
        ConReader.createNeededDirectory();
    }
}