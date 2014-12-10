package liquibase.ext.ora.materializedview.droplog;

import liquibase.statement.AbstractSqlStatement;

public class DropMaterializedViewLogStatement extends AbstractSqlStatement {
    private String schemaName;
    private String tableName;

    public DropMaterializedViewLogStatement(String tableName) {
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
}
