package resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;

import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import javax.imageio.ImageIO;

import util.Animation;

public class ResourceManager {

	public static ResourceManager instance;
	public Map<String, Map<String, Animation>> animations;
	public Map<String, Double[][]> movement;

	private ResourceManager() {
		animations = new HashMap<>();
		movement = new HashMap<>();
		animations.putAll(readAnimations("entity_animations.json"));
		animations.putAll(readAnimations("projectile_animations.json"));
		animations.putAll(readAnimations("gui_animations.json"));
	}

	public static synchronized ResourceManager getInstance() {
		if (instance == null) {
			instance = new ResourceManager();
		}
		return instance;
	}

	public Map<String, Map<String, Animation>> readAnimations(String path) {
	    GsonBuilder builder = new GsonBuilder();
	    builder.registerTypeAdapter(Animation.class, new AnimationDeserializer());
	    Gson gson = builder.create();

	    InputStream inputStream = ResourceManager.class.getClassLoader().getResourceAsStream(path);
	    if (inputStream != null) {
	        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
	            JsonElement rootElement = JsonParser.parseReader(reader);
	            JsonObject rootObject = rootElement.getAsJsonObject();

	            for (Map.Entry<String, JsonElement> entry : rootObject.entrySet()) {
	                String entityName = entry.getKey();
	                JsonObject animations = entry.getValue().getAsJsonObject();

	                for (Map.Entry<String, JsonElement> animationEntry : animations.entrySet()) {
	                    JsonObject animationObject = animationEntry.getValue().getAsJsonObject();
	                    String baseFolder = "";
	                    if(path.equals("entity_animations.json")) {
	                    	baseFolder = "/entities/";
	                    }else if(path.equals("gui_animations.json")) {
	                    	baseFolder = "/gui/";
	                    }else if(path.equals("projectile_animations.json")) {
	                    	baseFolder = "/projectiles/";
	                    }
	                    String animationPath = baseFolder + entityName + ".png";
	                    animationObject.addProperty("path", animationPath);
	                    animationObject.addProperty("name", animationEntry.getKey());
	                }
	            }

	            Type type = new TypeToken<Map<String, Map<String, Animation>>>() {}.getType();
	            return gson.fromJson(rootElement, type);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    return null;
	}

	public void registerMovement(String registry, String type, Double[][] movement) {
		this.movement.put(registry + type, movement);
	}

	public Double[][] getMovement(String registry, String type) {
		return movement.getOrDefault(registry + type, null);
	}

	public Animation getAnimation(String registry, String type) {
		Animation animation = animations.get(registry).getOrDefault(type, null) == null
				? animations.get(registry).get("default")
				: animations.get(registry).get(type);
		return animation;
	}
	
	public class AnimationDeserializer implements JsonDeserializer<Animation> {

	    @Override
	    public Animation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	        JsonObject jsonObject = json.getAsJsonObject();
	        String name = jsonObject.get("name").getAsString();
	        String path = jsonObject.get("path").getAsString();
	        String nextAnimation = jsonObject.get("nextAnimation").getAsString();
	        int count = jsonObject.get("count").getAsInt();
	        int x = jsonObject.get("x").getAsInt();
	        int y = jsonObject.get("y").getAsInt();
	        
	        JsonArray widthsArray = jsonObject.getAsJsonArray("widths");
	        int[] widths = new Gson().fromJson(widthsArray, int[].class);

	        JsonArray heightsArray = jsonObject.getAsJsonArray("heights");
	        int[] heights = new Gson().fromJson(heightsArray, int[].class);

	        JsonArray offsetXArray = jsonObject.getAsJsonArray("offsetX");
	        int[] offsetX = new Gson().fromJson(offsetXArray, int[].class);

	        JsonArray offsetYArray = jsonObject.getAsJsonArray("offsetY");
	        int[] offsetY = new Gson().fromJson(offsetYArray, int[].class);

	        Animation animation = new Animation(name, path, count, x, y, widths, heights, offsetX, offsetY, nextAnimation);
	        
	        return animation;
	    }
	}

}
