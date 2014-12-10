package liquibase.ext.ora.materializedview.droplog;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.util.StringUtils;

public class DropMaterializedViewLogOracle extends AbstractSqlGenerator<DropMaterializedViewLogStatement> {

    @Override
	public boolean supports(DropMaterializedViewLogStatement createMaterializedViewStatement, Database database) {
        return database instanceof OracleDatabase;
    }

    @Override
	public ValidationErrors validate(DropMaterializedViewLogStatement createMaterializedViewStatement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("tableName", createMaterializedViewStatement.getTableName());
        return validationErrors;
    }

    @Override
	public Sql[] generateSql(DropMaterializedViewLogStatement createMaterializedViewStatement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        StringBuilder sql = new StringBuilder();

        sql.append("DROP MATERIALIZED VIEW LOG ON ");
        if ( StringUtils.trimToNull( createMaterializedViewStatement.getSchemaName() ) != null) {
			sql.append(createMaterializedViewStatement.getSchemaName()).append(".");
		}
        if ( createMaterializedViewStatement.getTableName() != null) {
            sql.append(createMaterializedViewStatement.getTableName())
                    .append(" ");
        }

        return new Sql[]
                {
                        new UnparsedSql(sql.toString())
                };
    }
}
