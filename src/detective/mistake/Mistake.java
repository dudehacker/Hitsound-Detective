package detective.mistake;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Mistake implements Comparable<Mistake>{

	protected MistakeType description;
	
	public Mistake(MistakeType description) {
		this.description = description;
	}
	
	@JsonProperty("text")
	public String getDescription() {
		return description.toString();
	}
	
	@JsonProperty("type")
	public String getSeverity(){
		return description.getSeverity().getlowerCaseString();
	}

	@Override
	public int compareTo(Mistake other) {
		return description.compareTo(other.description);
	}

}
