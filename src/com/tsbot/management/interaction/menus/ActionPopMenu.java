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

package com.tsbot.management.interaction.menus;


import com.github.theholywaffle.teamspeak3.TS3Api;
import com.tsbot.management.interaction.Functions;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;


/**
 *
 * @author NameHidden
 * @since April 10, 2015.
 */
public class ActionPopMenu extends JPopupMenu {

    private TS3Api api;
    private Functions functions;



    /**
     * Constructor for {@link ActionPopMenu}.
     *
     * Adds all the functions to the {@link JPopupMenu} and assigns their action upon click.
     *
     * @param api The Active {@link TS3Api}.
     * @param clients The {@link JList} that holds the online clients on the server.
     */
    public ActionPopMenu(TS3Api api, JList clients) {
        this.api = api;
        this.functions = new Functions(this.api);

        JMenuItem permissions = add(new JMenuItem("Permissions"));
        addSeparator();

        JMenuItem channelKick = add(new JMenuItem("Kick from Channel"));
        JMenuItem serverKick = add(new JMenuItem("Kick from Server"));
        JMenuItem ban = add(new JMenuItem("Ban"));
        JMenuItem poke = add(new JMenuItem("Poke"));
        addSeparator();

        JMenuItem message = add(new JMenuItem("Message"));
        JMenuItem info = add(new JMenuItem("Connection Info"));


        permissions.addActionListener((a) -> {
            Runnable r = () -> {
                if (this.api.getClientByNameExact(clients.getSelectedValue().toString(), false) != null) {
                    functions.permissions(this.api.getClientByNameExact(clients.getSelectedValue().toString(), false));
                }
            };

            new Thread(r).start();
        });

        channelKick.addActionListener((a) -> {
            Runnable r = () -> {
                if (this.api.getClientByNameExact(clients.getSelectedValue().toString(), false) != null) {
                    functions.kickFromChannel(api.getClientByNameExact(clients.getSelectedValue().toString(), false));
                }
            };

            new Thread(r).start();
        });

        serverKick.addActionListener((a) -> {
            Runnable r = () -> {
                if (this.api.getClientByNameExact(clients.getSelectedValue().toString(), false) != null) {
                    functions.kickFromServer(api.getClientByNameExact(clients.getSelectedValue().toString(), false));
                    clients.remove(clients.getSelectedIndex());
                }
            };

            new Thread(r).start();
        });

        ban.addActionListener((a) -> {
            double hours = Double.valueOf(JOptionPane.showInputDialog(null,
                    "Enter amount of hours to ban:", "Ban Time", JOptionPane.INFORMATION_MESSAGE));

            if (hours == 0)
                return;

            Runnable r = () -> {
                if (this.api.getClientByNameExact(clients.getSelectedValue().toString(), false) != null) {
                    functions.ban(api.getClientByNameExact(clients.getSelectedValue().toString(), false), hours);
                    clients.remove(clients.getSelectedIndex());
                }
            };

            new Thread(r).start();
        });

        poke.addActionListener((a) -> {
            String text = JOptionPane.showInputDialog(null,
                    "Enter a message to attach with the poke: ", "Poke Message", JOptionPane.INFORMATION_MESSAGE);

            if (text == null || text.isEmpty()) {
                return;
            }

            Runnable r = () ->
                    functions.poke(api.getClientByNameExact(clients.getSelectedValue().toString(), false), text);

            new Thread(r).start();
        });
    }
}