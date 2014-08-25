package liquibase.ext.ora.materializedview.create;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;

public class CreateMaterializedViewOracle extends AbstractSqlGenerator<CreateMaterializedViewStatement> {

    @Override
	public boolean supports(CreateMaterializedViewStatement createMaterializedViewStatement, Database database) {
        return database instanceof OracleDatabase;
    }

    @Override
	public ValidationErrors validate(CreateMaterializedViewStatement createMaterializedViewStatement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("viewName", createMaterializedViewStatement.getViewName());
        validationErrors.checkRequiredField("subquery", createMaterializedViewStatement.getSubquery());
        return validationErrors;
    }

    @Override
	public Sql[] generateSql(CreateMaterializedViewStatement createMaterializedViewStatement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE MATERIALIZED VIEW ");
        if (createMaterializedViewStatement.getSchemaName() != null) {
			sql.append(createMaterializedViewStatement.getSchemaName()).append(".");
		}
        if (createMaterializedViewStatement.getViewName() != null) {
            sql.append(createMaterializedViewStatement.getViewName())
                    .append(" ");
        }

        if (createMaterializedViewStatement.getColumnAliases() != null) {
			sql.append("(")
                    .append(createMaterializedViewStatement.getColumnAliases())
                    .append(") ");
		}
        if (createMaterializedViewStatement.getObjectType() != null) {
            sql.append("OF ");
            if (createMaterializedViewStatement.getSchemaName() != null) {
				sql.append(createMaterializedViewStatement.getSchemaName());
			}
            sql.append(createMaterializedViewStatement.getObjectType())
                    .append(" ");
        }

        if(createMaterializedViewStatement.getEnableOnPrebuiltTable() != null && createMaterializedViewStatement.getEnableOnPrebuiltTable() ) {
          sql.append("ON PREBUILT TABLE ");
          if (createMaterializedViewStatement.getReducedPrecision() != null) {
              if (createMaterializedViewStatement.getReducedPrecision()) {
				sql.append("WITH ");
			} else {
				sql.append("WITHOUT ");
			}

              sql.append("REDUCED PRECISION ");
          }
        } else {
        	if ( createMaterializedViewStatement.getBuildDeferred() != null ) {
        		if ( createMaterializedViewStatement.getBuildDeferred() ) {
        			sql.append("BUILD DEFERRED ");
        		} else {
        			sql.append("BUILD IMMEDIATE ");
        		}
        	}
        }

        if (createMaterializedViewStatement.getUsingIndex() != null) {
            if (createMaterializedViewStatement.getUsingIndex()) {
                sql.append("USING INDEX ");
                if (createMaterializedViewStatement.getTableSpace() != null) {
                	sql.append("TABLESPACE ")
                			.append(createMaterializedViewStatement.getTableSpace())
                            .append(" ");
                }
            } else {
				sql.append("USING NO INDEX ");
			}
        }

        if ( createMaterializedViewStatement.getRefreshOnCommit() != null && createMaterializedViewStatement.getRefreshOnCommit() ) {
        	sql.append( "REFRESH ON COMMIT " );
        } else {
        	sql.append( "REFRESH ON DEMAND " );
        }

        if ( createMaterializedViewStatement.getRefreshType() != null ) {
        	sql.append( createMaterializedViewStatement.getRefreshType().toUpperCase() ).append( " " );
        }

        if ( createMaterializedViewStatement.getRefreshWithRowid() != null && createMaterializedViewStatement.getRefreshWithRowid() ) {
        	sql.append( "WITH ROWID " );
        } else {
        	sql.append( "WITH PRIMARY KEY " );
        }

        if (createMaterializedViewStatement.getForUpdate() != null) {
            if (createMaterializedViewStatement.getForUpdate() == true) {
                sql.append("FOR UPDATE ");
            }
        }

        if (createMaterializedViewStatement.getQueryRewrite() != null ) {
        	if ( createMaterializedViewStatement.getQueryRewrite() ) {
                sql.append("ENABLE QUERY REWRITE ");
        	} else {
                sql.append("DISABLE QUERY REWRITE ");
        	}
        }

        sql.append("AS ");
        if (createMaterializedViewStatement.getSubquery() != null) {
			sql.append(createMaterializedViewStatement.getSubquery());
		}

        return new Sql[]
                {
                        new UnparsedSql(sql.toString())
                };
    }
}
