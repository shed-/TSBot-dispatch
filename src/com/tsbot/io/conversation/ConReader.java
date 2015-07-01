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

package com.tsbot.io.conversation;


import com.tsbot.management.interaction.Intellect;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Consists the Reading job of the I/O for the conversation handling. Solely brought to life to read the
 * intelligence file and sort the data into objects that can be easily used to manipulate input from users
 * on the teamspeak server.
 *
 *
 * @author NameHidden
 * @since March 31, 2015.
 */
public class ConReader extends BufferedReader {

    private ArrayList<Intellect> intelligence;
    private static boolean updated;

    public static final Path CONVERSATION_INTELLIGENCE_LOCATION = Paths.get(System.getProperty("user.home") +
            "/appdata/roaming/TSBot/Conversation.dat");

    /**
     * Default constructor. Calls the super constructor of {@link BufferedReader}
     *
     * @throws IOException
     */
    public ConReader() throws IOException {
        super(new FileReader(CONVERSATION_INTELLIGENCE_LOCATION.toString()));
    }

    /**
     * Constructor used when the user specifies to load a saved conversation.dat file
     *
     * @param path the path to the file
     * @throws IOException
     */
    public ConReader(Path path) throws IOException {
        super(new FileReader(path.toString()));
    }

    public boolean isValidStatement(String stmt) {
        return stmt.contains("<input_text=\"") && stmt.contains("contains=\"") &&
                stmt.contains("output_text=\"");
    }

    /**
     * Connects to the file ({@link ConReader#CONVERSATION_INTELLIGENCE_LOCATION}. Converts all binary input
     * to decimal, and then finally to String. Through the use of toIntellect(String) function, an Intellect
     * object is appended to the {@link ConReader#intelligence}.
     * <p>
     * Constant reading from the file is redundant, as it can lead to the same data if no other processes have been
     * deleted or added. In order to prevent this, a check is deployed to check whether the data in processes
     * is up-to-date or not. If it is update, then return {@link ConReader#intelligence} without
     * further inspection.
     *
     * @return {@link ConReader#intelligence}.
     * @throws IOException
     *
     * @see Intellect
     * @see ConReader#toIntellect(String)
     * @see ConReader#invalidate()
     * @see ConReader#isUpdated()
     */
    public ArrayList<Intellect> intelligence() throws IOException {
        if (this.intelligence == null || !isUpdated()) {
            intelligence = new ArrayList<>();
            String currentLine;
            StringBuilder intellect = new StringBuilder();

            while ((currentLine = readLine()) != null) {
                String[] parts = currentLine.split(" ");
                for (String part : parts) {
                    int decimal = Integer.valueOf(toDecimal(part));
                    intellect.append(Character.toString((char) decimal));
                }

                if (!isValidStatement(intellect.toString())) {
                    return null;
                }

                intelligence.add(toIntellect(intellect.toString()));
                intellect = new StringBuilder();
            }
        } else {
            return this.intelligence;
        }

        updated = true;
        return this.intelligence;
    }

    /**
     * Invalidates the data held in {@link ConReader#intelligence} and sets it to outdated.
     * Static usage is needed and optimal in this function for completely control over all objects of this class
     * and ease of invalidation without the need to construct the class.
     */
    public static void invalidate() {
        updated = false;
    }

    /**
     * Checks whether the data held by this object is updated and equal to the one in the file.
     *
     * @return true     if the data is updated.
     *         false    otherwise.
     */
    public boolean isUpdated() {
        return updated;
    }

    /**
     * Converts the object process to an object of type {@link Intellect}.
     *
     * @param process the data to be used for construction of the object of type {@link Intellect}.
     *
     * @return an {@link Intellect} Object
     */
    private Intellect toIntellect(String process) {
        process = process.trim().replace("<", "").replace(">", "");
        String[] details = process.split("\t", 3);

        String[] input = details[0].split("=");
        String[] contains = details[1].split("=");
        String[] output = details[2].split("=");

        return new Intellect(input[1].replace("\"", ""), !Boolean.valueOf(contains[1].replace("\"", "")),
                output[1].replace("\"", ""));
    }

    /**
     * Converts a binary sequence into decimal.
     *
     * @param binary the binary sequence
     * @return the decimal value of the binary sequence.
     */
    private String toDecimal(String binary) {
        return Integer.parseInt(binary, 2) + "";
    }


    /**
     * Creates the needed parent directory to work upon.
     *
     * @return the path to the parent directory.
     * @throws IOException
     */
    public static Path createNeededDirectory() throws IOException {
        if (Files.isDirectory(CONVERSATION_INTELLIGENCE_LOCATION.getParent()))
            return null;

        return Files.createDirectory(CONVERSATION_INTELLIGENCE_LOCATION.getParent());
    }

}
