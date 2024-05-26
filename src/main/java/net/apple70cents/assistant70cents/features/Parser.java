package net.apple70cents.assistant70cents.features;

import net.apple70cents.assistant70cents.Assistant70Cents;
import net.apple70cents.assistant70cents.config.ConfigStorage;
import net.apple70cents.assistant70cents.utils.LoggerUtils;

import java.io.*;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public static ASTNode parseFile(File filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        ASTNode root = new ASTNode("root");

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue; // 跳过空行和注释
            }
            ASTNode commandNode = parseLine(line);
            root.addChild(commandNode);
        }

        reader.close();
        return root;
    }

    private static ASTNode parseLine(String line) {
        Pattern pattern = Pattern.compile("(\\w+)(?:\\s+\"([^\"]+)\")?(?:\\s+randint\\((\\d+),(\\d+)\\))?");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            ASTNode node = new ASTNode(matcher.group(1));
            if (matcher.group(2) != null) {
                node.addArgument(matcher.group(2));
            }
            if (matcher.group(3) != null && matcher.group(4) != null) {
                long lower = Integer.parseInt(matcher.group(3));
                long upper = Integer.parseInt(matcher.group(4));
                long randomValue = new Random().nextLong(upper - lower + 1) + lower;
                node.addArgument(String.valueOf(randomValue));
            }
            return node;
        }
        throw new IllegalArgumentException("Unable to parse line: " + line);
    }

    public static File getFile(String path) {
        File file = new File(path);
        if (!file.isAbsolute()) {
            file = new File(ConfigStorage.FILE.getParentFile(), path);
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write("# Please write your scripts below:\n");
                writer.close();
            } catch (IOException e){
                LoggerUtils.error("Failed to initialize Script.txt");
            }
        } return file;
    }

    public static ASTNode parseScript() {
        try {
            return parseFile(getFile((String) Assistant70Cents.CONFIG.get("general.script_path")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

