package dk.dbjet.filter;

public interface JetSearchControl {

	public int getPage();
	
	public int getSize();

	public String getSortBy();

	public SortOrder getSortOrder();

	public String[] getColumns();

	public static enum SortOrder {
		NONE (""),
		ASCENDING ("ASC"),
		DESCENDING ("DESC");
		
		String clause;
		
		SortOrder(String clause) {
			this.clause = clause;
		}
		
		public String toClauseString() {
			return this.clause;
		}
		
		public static SortOrder fromString(String string) {
			String upr = string.toUpperCase();
			for(SortOrder so : SortOrder.values()) {
				if (so.name().startsWith(upr)) {
					return so;
				}
			}
			throw new IllegalArgumentException("Unknown sortOrder: " + string);
		}
	}
}
