package detective.mistake;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Mistake implements Comparable<Mistake>{

	protected MistakeType description;
	protected String text;
	
	public Mistake(MistakeType description) {
		this(description,"");
	}
	
	@Override
	public String toString() {
		return "Mistake [description=" + description + ", text=" + text + "]";
	}

	public Mistake(MistakeType description, String text){
		this.description = description;
		this.text = text;
	}
	
	@JsonProperty("text")
	public String getDescription() {
		if (text.isEmpty()){
			return description.toString();
		} else {
			return description.toString() + ": " + text;
		}
		
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
