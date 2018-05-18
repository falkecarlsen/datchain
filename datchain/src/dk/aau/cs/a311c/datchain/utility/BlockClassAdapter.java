package dk.aau.cs.a311c.datchain.utility;

import com.google.gson.*;
import dk.aau.cs.a311c.datchain.Block;

import java.lang.reflect.Type;

public class BlockClassAdapter implements JsonSerializer<Block>, JsonDeserializer<Block> {

    @Override
    public JsonElement serialize(Block src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        //denote type of Block when serialising
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        //recursively append context of block to json
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }

    @Override
    public Block deserialize(JsonElement json, Type typeofT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();
        //get SimpleName of class specified when serialising
        String type = jsonObject.get("type").getAsString();
        //get all specified properties, that being fields withing specified class,
        JsonElement element = jsonObject.get("properties");

        try {
            //specify location of classes, these are contained within root of *.datchain-package
            String thepackage = "dk.aau.cs.a311c.datchain.";
            //return the block found with given elements and class-type
            return context.deserialize(element, Class.forName(thepackage + type));
        } catch (ClassNotFoundException e) {
            //if class has not been found, print error to user
            System.out.println("ERROR: When deserialising, could not find package: " + e.getMessage());
        }
        return null;
    }
}
