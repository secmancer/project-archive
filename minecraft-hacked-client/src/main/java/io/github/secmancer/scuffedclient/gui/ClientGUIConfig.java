package io.github.secmancer.scuffedclient.gui;

import com.google.gson.*;
import com.lukflug.panelstudio.config.IConfigList;
import com.lukflug.panelstudio.config.IPanelConfig;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientGUIConfig implements IConfigList {

    private final String fileLoc;
    private JsonObject object = null;

    public ClientGUIConfig(String fileLoc) {
        this.fileLoc = fileLoc;
    }

    @Override
    public void begin(boolean loading) {
        if (loading) {
            Path path = Paths.get(fileLoc + "ClickGUI" + ".json");
            if (!Files.exists(path)) {
                return;
            }
            try {
                InputStream inputStream;
                inputStream = Files.newInputStream(path);
                JsonObject mainObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
                if (mainObject.get("Panels") == null) {
                    return;
                }
                object = mainObject.get("Panels").getAsJsonObject();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            object = new JsonObject();
        }
    }

    @Override
    public void end(boolean loading) {
        if (object == null) return;
        if (!loading) {
            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileLoc + "ClickGUI" + ".json"), StandardCharsets.UTF_8);
                JsonObject mainObject = new JsonObject();
                mainObject.add("Panels", object);
                String jsonString = gson.toJson(JsonParser.parseString(mainObject.toString()));
                fileOutputStreamWriter.write(jsonString);
                fileOutputStreamWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        object = null;
    }

    @Override
    public IPanelConfig addPanel(String title) {
        if (object == null) return null;
        JsonObject valueObject = new JsonObject();
        object.add(title, valueObject);
        return new GSPanelConfig(valueObject);
    }

    @Override
    public IPanelConfig getPanel(String title) {
        if (object == null) return null;
        JsonElement configObject = object.get(title);
        if (configObject != null && configObject.isJsonObject())
            return new GSPanelConfig(configObject.getAsJsonObject());
        return null;
    }


    private record GSPanelConfig(JsonObject configObject) implements IPanelConfig {

        @Override
            public void savePositon(Point position) {
                configObject.add("PosX", new JsonPrimitive(position.x));
                configObject.add("PosY", new JsonPrimitive(position.y));
            }

            @Override
            public void saveSize(Dimension size) {
            }

            @Override
            public Point loadPosition() {
                Point point = new Point();
                JsonElement panelPosXObject = configObject.get("PosX");
                if (panelPosXObject != null && panelPosXObject.isJsonPrimitive()) {
                    point.x = panelPosXObject.getAsInt();
                } else return null;
                JsonElement panelPosYObject = configObject.get("PosY");
                if (panelPosYObject != null && panelPosYObject.isJsonPrimitive()) {
                    point.y = panelPosYObject.getAsInt();
                } else return null;
                return point;
            }

            @Override
            public Dimension loadSize() {
                return null;
            }

            @Override
            public void saveState(boolean state) {
                configObject.add("State", new JsonPrimitive(state));
            }

            @Override
            public boolean loadState() {
                JsonElement panelOpenObject = configObject.get("State");
                if (panelOpenObject != null && panelOpenObject.isJsonPrimitive()) {
                    return panelOpenObject.getAsBoolean();
                }
                return false;
            }
        }
}
