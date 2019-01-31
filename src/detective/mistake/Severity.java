package detective.mistake;

public enum Severity {
	INFO, WARNING, PROBLEM;
	
	public String getlowerCaseString(){
		return this.toString().toLowerCase();
	}

}

