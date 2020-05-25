package dk.dbjet.filter;

public final class JetSearchControlBuilder implements JetSearchControl {

	private int page;
	private int size;

	private String sortBy;
	private SortOrder sortOrder;

	private String[] columns;

	private JetSearchControlBuilder(int page, int size, String sortBy, SortOrder sortOrder, String[] columns) {
		this.columns = columns;
		this.page = page;
		this.size = size;
		this.sortBy = sortBy;
		this.sortOrder = sortOrder;
	}

	@Override
	public int getPage() {
		return this.page;
	}

	@Override
	public int getSize() {
		return this.size;
	}

	@Override
	public String getSortBy() {
		return this.sortBy;
	}

	@Override
	public SortOrder getSortOrder() {
		return this.sortOrder;
	}

	@Override
	public String[] getColumns() {
		return this.columns;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private int page = 1;
		private int size = 10;

		private String sortBy = null;
		private SortOrder sortOrder = SortOrder.NONE;

		private String[] columns = null;

		public JetSearchControl build() {
			return new JetSearchControlBuilder(this.page, this.size, this.sortBy, this.sortOrder, this.columns);
		} 

		private Builder() {

		}

		public Builder page(int page) {
			validateEqualsOrGreaterThan(page, 1);
			this.page = page;
			return this;
		}

		public Builder size(int size) {
			this.size = size;
			return this;
		}

		public Builder sortBy(String sortBy) {
			this.sortBy = sortBy;
			return this;
		}

		public Builder sortOrder(SortOrder sortOrder) {
			this.sortOrder = sortOrder;
			return this;
		}

		public Builder columns(String ...columns) {
			this.columns = columns;
			return this;
		}

		private void validateEqualsOrGreaterThan(int source, int target) {
			if (source < target) {
				throw new RuntimeException(source + " should be >= " + target);
			}		
		}
	}
}
