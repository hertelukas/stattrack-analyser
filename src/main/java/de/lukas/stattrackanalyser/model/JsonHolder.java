package de.lukas.stattrackanalyser.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class JsonHolder {

    JSONArray document;

    public LoadStatus loadFile(File file) {
        try {
            document = new JSONObject(FileUtils.readFileToString(file, "utf-8")).getJSONArray("entries");
            return LoadStatus.SUCCESS;
        } catch (IOException e) {
            System.out.println("Failed to load file: " + e.getMessage());
            return LoadStatus.FILE_ERROR;
        } catch (JSONException e) {
            System.out.println("Failed to read JSON: " + e.getMessage());
            return LoadStatus.JSON_ERROR;
        }
    }

    public int getAmountOfEntries() {
        return document.length();
    }


}
