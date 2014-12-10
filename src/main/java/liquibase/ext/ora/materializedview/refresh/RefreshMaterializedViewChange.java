package liquibase.ext.ora.materializedview.refresh;

import liquibase.change.AbstractChange;
import liquibase.change.Change;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.exception.RollbackImpossibleException;
import liquibase.ext.ora.materializedview.drop.DropMaterializedViewChange;
import liquibase.statement.SqlStatement;

@DatabaseChange(name="refreshMaterializedView", description = "Refresh Materialized View", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class RefreshMaterializedViewChange extends AbstractChange {

    private String schemaName;
    private String viewName;
    private Boolean atomicRefresh;
    private String refreshType;

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

    @Override
	public String getConfirmationMessage() {
        return "Materialized view " + getViewName() + " has been refreshed";
    }

    @Override
	public SqlStatement[] generateStatements(Database database) {
        RefreshMaterializedViewStatement statement = new RefreshMaterializedViewStatement(getViewName());
        statement.setSchemaName(getSchemaName());
        statement.setAtomicRefresh(getAtomicRefresh());
        statement.setRefreshType(getRefreshType());

        return new SqlStatement[]{statement};
    }
    
    /**
     * Return nothing, you can't revert an MV refresh, but nor do we need to blow up with a "rollback not possible". 
     */
    @Override
	protected Change[] createInverses() {
        return new Change[]{};
    }

	public Boolean getAtomicRefresh() {
		return atomicRefresh;
	}

	public void setAtomicRefresh(Boolean atomicRefresh) {
		this.atomicRefresh = atomicRefresh;
	}

	public String getRefreshType() {
		return refreshType;
	}

	public void setRefreshType(String refreshType) {
		this.refreshType = refreshType;
	}
}
