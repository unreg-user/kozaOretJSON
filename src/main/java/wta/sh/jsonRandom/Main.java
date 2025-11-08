package wta.sh.jsonRandom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import wta.sh.jsonRandom.randomizers.JavaRandomWrapper;
import wta.sh.jsonRandom.randomizers.RandMap;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class Main {
	public static void main(String[] args) {
		Path runDir = Paths.get("run").toAbsolutePath().normalize();

		if (!Files.exists(runDir)) {
			try {
				Files.createDirectory(runDir);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			System.out.println("Создана папка: " + runDir);
		} else {
			System.out.println("Папка уже существует: " + runDir);
		}

		Path file=runDir.resolve("ru_ru.json");
		if (!Files.exists(runDir)) {
			throw new RuntimeException("json гони!");
		} else {
			System.out.println("Папка уже существует: " + runDir);
		}

		try {
			Gson gson = new Gson();
			String jsonContext = Files.readString(file);
			Type type = new TypeToken<LinkedHashMap<String, String>>(){}.getType();
			LinkedHashMap<String, String> translations = gson.fromJson(jsonContext, type);
			RandMap<String, String> randObject=new RandMap<>(translations, new JavaRandomWrapper());
			randObject.randomize((key, value) -> new RandMap.MapNodePos<>(key, value, countFormatPlaceholders(value)));
			gson = new GsonBuilder()
				  .setPrettyPrinting()
				  .disableHtmlEscaping()
				  .create();
			String jsonOutput = gson.toJson(randObject.getObject());
			Files.writeString(file, jsonOutput);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	static Pattern pattern = Pattern.compile("%(\\d+\\$)?s");

	public static int countFormatPlaceholders(String str) {
		if (str == null) return 0;
		var matcher = pattern.matcher(str);
		int count = 0;
		while (matcher.find()) {
			count++;
		}
		return count;
	}
}