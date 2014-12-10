package liquibase.ext.ora.materializedview.createlog;

import liquibase.change.AbstractChange;
import liquibase.change.Change;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.ora.materializedview.droplog.DropMaterializedViewLogChange;
import liquibase.statement.SqlStatement;

@DatabaseChange(name="createMaterializedViewLog", description = "Create materialized view log", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class CreateMaterializedViewLogChange extends AbstractChange {
    private String schemaName;
    private String tableName;
    private String tableSpace;
    private Boolean refreshWithRowid;

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

	@Override
	public String getConfirmationMessage() {
        return "Materialized view log on " + getTableName() + " has been created";
    }

    @Override
	protected Change[] createInverses() {
        DropMaterializedViewLogChange inverse = new DropMaterializedViewLogChange();
        inverse.setSchemaName(getSchemaName());
        inverse.setTableName(getTableName());
        return new Change[]{
                inverse,
        };
    }

    @Override
	public SqlStatement[] generateStatements(Database database) {
        CreateMaterializedViewLogStatement statement = new CreateMaterializedViewLogStatement(getTableName());
        statement.setSchemaName(getSchemaName());
        statement.setTableSpace(getTableSpace());
        statement.setRefreshWithRowid(getRefreshWithRowid());

        return new SqlStatement[]{statement};
    }
}
