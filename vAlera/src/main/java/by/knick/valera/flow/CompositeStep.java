package by.knick.valera.flow;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public abstract class CompositeStep extends Step{
	private List<Step> childSteps = new ArrayList<>();
	
	public void addChildStep(Step step){
		this.childSteps.add(step);
	}
}
