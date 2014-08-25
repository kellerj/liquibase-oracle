package liquibase.ext.ora.materializedview.createlog;

import liquibase.statement.AbstractSqlStatement;

public class CreateMaterializedViewLogStatement extends AbstractSqlStatement {
    private String schemaName;
    private String tableName;
    private String tableSpace;
    private Boolean refreshWithRowid;

    public CreateMaterializedViewLogStatement(String tableName) {
        this.tableName = tableName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableSpace() {
		return tableSpace;
	}

	public void setTableSpace(String tableSpace) {
		this.tableSpace = tableSpace;
	}

	public Boolean getRefreshWithRowid() {
		return refreshWithRowid;
	}

	public void setRefreshWithRowid(Boolean refreshWithRowid) {
		this.refreshWithRowid = refreshWithRowid;
	}
}
