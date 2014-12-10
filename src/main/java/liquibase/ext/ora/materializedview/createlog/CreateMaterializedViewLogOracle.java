package liquibase.ext.ora.materializedview.createlog;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;

import org.apache.commons.lang.StringUtils;

public class CreateMaterializedViewLogOracle extends AbstractSqlGenerator<CreateMaterializedViewLogStatement> {

    @Override
	public boolean supports(CreateMaterializedViewLogStatement createMaterializedViewStatement, Database database) {
        return database instanceof OracleDatabase;
    }

    @Override
	public ValidationErrors validate(CreateMaterializedViewLogStatement createMaterializedViewStatement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("tableName", createMaterializedViewStatement.getTableName());
        return validationErrors;
    }

    @Override
	public Sql[] generateSql(CreateMaterializedViewLogStatement createMaterializedViewStatement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE MATERIALIZED VIEW LOG ON ");
        if ( StringUtils.trimToNull( createMaterializedViewStatement.getSchemaName() ) != null) {
			sql.append(createMaterializedViewStatement.getSchemaName()).append(".");
		}
        if ( createMaterializedViewStatement.getTableName() != null) {
            sql.append(createMaterializedViewStatement.getTableName())
                    .append(" ");
        }

        if ( StringUtils.trimToNull( createMaterializedViewStatement.getTableSpace() ) != null) {
        	sql.append("TABLESPACE ")
        			.append(createMaterializedViewStatement.getTableSpace())
                    .append(" ");
        }

        if ( createMaterializedViewStatement.getRefreshWithRowid() != null && createMaterializedViewStatement.getRefreshWithRowid() ) {
        	sql.append( "WITH ROWID " );
        } else {
        	sql.append( "WITH PRIMARY KEY " );
        }

        return new Sql[]
                {
                        new UnparsedSql(sql.toString())
                };
    }
}
