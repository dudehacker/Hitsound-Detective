package server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import server.model.osu.api.BeatmapInfoJSON;

public class OsuAPI {
	private static ObjectMapper objectMapper;
	private static final String API_KEY = System.getenv("api_key");

	private OsuAPI() {
		throw new IllegalStateException("Utility class");
	}

	private static void initMapper() {
		if (objectMapper != null)
			return;
		objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		objectMapper.setDateFormat(df);
	}

	private static List<BeatmapInfoJSON> getBeatmap(String url) {
		RestTemplate restTemplate = new RestTemplate();
		List<BeatmapInfoJSON> result;
		try {
			String rawJSON = restTemplate.getForObject(url, String.class);
			result = objectMapper.readValue(rawJSON, new TypeReference<List<BeatmapInfoJSON>>() {
			});
			return result;
		} catch (RestClientException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<BeatmapInfoJSON> getBeatmapInfoFromSetID(String setID) {
		initMapper();
		String url = "https://osu.ppy.sh/api/get_beatmaps?k=" + API_KEY + "&s=" + setID;
		return getBeatmap(url);
	}

	public static List<BeatmapInfoJSON> getBeatmapInfoFromBeatmapID(String beatmapID) {
		initMapper();
		String url = "https://osu.ppy.sh/api/get_beatmaps?k=" + API_KEY + "&b=" + beatmapID;
		return getBeatmap(url);
	}
}
