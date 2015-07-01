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


import java.awt.BorderLayout;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author NameHidden
 * @since March 22, 2015.
 */
public class DeveloperConsole extends JFrame {

    public DeveloperConsole() {
        super("TSBot - Developer Console");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500,400);

        JTextArea textArea = new JTextArea();
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        textArea.setEditable(false);
        textArea.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(textArea);

        getContentPane().add(scroll);

        TSBotOutputStream output = new TSBotOutputStream(textArea);

        System.setOut(new PrintStream(output));
        System.setErr(new PrintStream(output));

        JButton dump = new JButton("Dump");
        getContentPane().add(dump, BorderLayout.SOUTH);

        dump.addActionListener((a) -> {
            int index = 1;
            String desktop = System.getProperty("user.home") + "/Desktop/";
            File file = new File(desktop + "tsbot_dump.txt");
            while (file.exists()) {
                file = new File(desktop + "tsbot_dump" + index++ + ".txt");
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.getPath()))) {
                textArea.write(bw);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}


class TSBotOutputStream extends OutputStream {


    private JTextArea textArea;


    public TSBotOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }


    @Override
    public void write(int b) throws IOException {
        textArea.append(String.valueOf((char) b));
    }
}
