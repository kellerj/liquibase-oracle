package liquibase.ext.ora.materializedview.droplog;

import liquibase.change.AbstractChange;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.statement.SqlStatement;

@DatabaseChange(name="DropMaterializedViewLog", description = "Drop materialized view log", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class DropMaterializedViewLogChange extends AbstractChange {
    private String schemaName;
    private String tableName;

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

	@Override
	public String getConfirmationMessage() {
        return "Materialized view log on " + getTableName() + " has been dropped";
    }

    @Override
	public SqlStatement[] generateStatements(Database database) {
        DropMaterializedViewLogStatement statement = new DropMaterializedViewLogStatement(getTableName());
        statement.setSchemaName(getSchemaName());

        return new SqlStatement[]{statement};
    }
}
