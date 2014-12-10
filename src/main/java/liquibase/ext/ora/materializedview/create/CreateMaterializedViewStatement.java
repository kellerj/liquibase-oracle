package liquibase.ext.ora.materializedview.create;

import liquibase.statement.AbstractSqlStatement;

public class CreateMaterializedViewStatement extends AbstractSqlStatement {
    private String schemaName;
    private String viewName;
    private String columnAliases;
    private String objectType;
    private Boolean reducedPrecision;
    private Boolean usingIndex;
    private String tableSpace;
    private Boolean forUpdate;
    private Boolean queryRewrite;
    private String subquery;
    private Boolean enableOnPrebuiltTable;
    private Boolean buildDeferred;
    private String refreshType;
    private Boolean refreshOnCommit;
    private Boolean refreshWithRowid;

    public CreateMaterializedViewStatement(String viewName, String subquery) {
        this.viewName = viewName;
        this.subquery = subquery;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getColumnAliases() {
        return columnAliases;
    }

    public void setColumnAliases(String columnAliases) {
        this.columnAliases = columnAliases;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Boolean getReducedPrecision() {
        return reducedPrecision;
    }

    public void setReducedPrecision(Boolean reducedPrecision) {
        this.reducedPrecision = reducedPrecision;
    }

    public Boolean getUsingIndex() {
        return usingIndex;
    }

    public void setUsingIndex(Boolean usingIndex) {
        this.usingIndex = usingIndex;
    }

    public String getTableSpace() {
        return tableSpace;
    }

    public void setTableSpace(String tableSpace) {
        this.tableSpace = tableSpace;
    }

    public Boolean getForUpdate() {
        return forUpdate;
    }

    public void setForUpdate(Boolean forUpdate) {
        this.forUpdate = forUpdate;
    }

    public Boolean getQueryRewrite() {
        return queryRewrite;
    }

    public void setQueryRewrite(Boolean queryRewrite) {
        this.queryRewrite = queryRewrite;
    }

    public String getSubquery() {
        return subquery;
    }

    public void setSubquery(String subquery) {
        this.subquery = subquery;
    }

    public Boolean getEnableOnPrebuiltTable() {
      return enableOnPrebuiltTable;
    }

    public void setEnableOnPrebuiltTable(Boolean enableOnPrebuiltTable) {
      this.enableOnPrebuiltTable=enableOnPrebuiltTable;
    }

	public Boolean getBuildDeferred() {
		return buildDeferred;
	}

	public void setBuildDeferred(Boolean buildDeferred) {
		this.buildDeferred = buildDeferred;
	}

	public String getRefreshType() {
		return refreshType;
	}

	public void setRefreshType(String refreshType) {
		this.refreshType = refreshType;
	}

	public Boolean getRefreshOnCommit() {
		return refreshOnCommit;
	}

	public void setRefreshOnCommit(Boolean refreshOnCommit) {
		this.refreshOnCommit = refreshOnCommit;
	}

	public Boolean getRefreshWithRowid() {
		return refreshWithRowid;
	}

	public void setRefreshWithRowid(Boolean refreshWithRowid) {
		this.refreshWithRowid = refreshWithRowid;
	}
}
