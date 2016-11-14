package by.knick.valera.project;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Project {
	private Config config = new Config();
	
	public Project(Config config){
		this.config = config;
	}
}
