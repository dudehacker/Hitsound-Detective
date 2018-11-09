package detective;

public class Mistake implements Comparable<Mistake>{

	protected MistakeType description;
	
	public Mistake(MistakeType description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description.toString();
	}

	@Override
	public int compareTo(Mistake other) {
		return description.compareTo(other.description);
	}

}
