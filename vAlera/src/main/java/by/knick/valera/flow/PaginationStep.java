package by.knick.valera.flow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class PaginationStep extends CompositeStep {
	private String nextPageSelector;
	private int pagesCountLimit = 5;
}
