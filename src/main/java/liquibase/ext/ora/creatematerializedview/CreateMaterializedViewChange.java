package liquibase.ext.ora.creatematerializedview;

import liquibase.change.AbstractChange;
import liquibase.change.Change;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.ora.dropmaterializedview.DropMaterializedViewChange;
import liquibase.statement.SqlStatement;

@DatabaseChange(name="createMaterializedView", description = "Create materialized view", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class CreateMaterializedViewChange extends AbstractChange {
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

    public String getSubquery() {
        return subquery;
    }

    public void setSubquery(String subquery) {
        this.subquery = subquery;
    }

    @Override
	public String getConfirmationMessage() {
        return "Materialized view " + getViewName() + " has been created";
    }

    @Override
	protected Change[] createInverses() {
        DropMaterializedViewChange inverse = new DropMaterializedViewChange();
        inverse.setSchemaName(getSchemaName());
        inverse.setViewName(getViewName());
        return new Change[]{
                inverse,
        };
    }

    @Override
	public SqlStatement[] generateStatements(Database database) {
        CreateMaterializedViewStatement statement = new CreateMaterializedViewStatement(getViewName(), getSubquery());
        statement.setSchemaName(getSchemaName());
        statement.setColumnAliases(getColumnAliases());
        statement.setObjectType(getObjectType());
        statement.setReducedPrecision(getReducedPrecision());
        statement.setUsingIndex(getUsingIndex());
        statement.setTableSpace(getTableSpace());
        statement.setForUpdate(getForUpdate());
        statement.setQueryRewrite(getQueryRewrite());
        statement.setEnableOnPrebuiltTable(getEnableOnPrebuiltTable());
        statement.setBuildDeferred(getBuildDeferred());
        statement.setRefreshType(getRefreshType());
        statement.setRefreshOnCommit(getRefreshOnCommit());
        statement.setRefreshWithRowid(getRefreshWithRowid());

        return new SqlStatement[]{statement};
    }

	public Boolean getEnableOnPrebuiltTable() {
		return enableOnPrebuiltTable;
	}

	public void setEnableOnPrebuiltTable(Boolean enableOnPrebuiltTable) {
		this.enableOnPrebuiltTable = enableOnPrebuiltTable;
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

	public void setQueryRewrite(Boolean queryRewrite) {
		this.queryRewrite = queryRewrite;
	}

	public Boolean getQueryRewrite() {
		return queryRewrite;
	}

}
