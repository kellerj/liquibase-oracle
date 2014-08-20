package liquibase.ext.ora.refreshmaterializedview;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;

import org.apache.commons.lang.StringUtils;

public class RefreshMaterializedViewOracle extends AbstractSqlGenerator<RefreshMaterializedViewStatement> {

    @Override
	public boolean supports(RefreshMaterializedViewStatement dropMaterializedViewStatement, Database database) {
        return database instanceof OracleDatabase;
    }

    @Override
	public ValidationErrors validate(RefreshMaterializedViewStatement statement, Database database,
                                     SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("viewName", StringUtils.trimToNull(statement.getViewName()));
        validationErrors.checkRequiredField("refreshType", StringUtils.trimToNull(statement.getRefreshType()));
        validationErrors.checkRequiredField("atomicRefresh", statement.getAtomicRefresh());
        return validationErrors;
    }

    @Override
	public Sql[] generateSql(RefreshMaterializedViewStatement statement, Database database,
                             SqlGeneratorChain sqlGeneratorChain) {
        StringBuilder sql = new StringBuilder();

        sql.append("BEGIN DBMS_MVIEW.REFRESH('");
        if ( StringUtils.isNotBlank( statement.getSchemaName() ) ) {
            sql.append(statement.getSchemaName()).append(".");
        }
        sql.append(statement.getViewName());
        sql.append( "','" );
        switch ( statement.getRefreshType() ) {
        	case  "fast" :
        		sql.append( 'F' );
        		break;
        	case  "complete" :
        		sql.append( 'C' );
        		break;
        	case  "force" :
        	default :
        		sql.append( '?' );
        }
        sql.append( "',ATOMIC_REFRESH=>" ).append( statement.getAtomicRefresh().toString().toUpperCase() );
        sql.append( "); END;" );
        return new Sql[]{new UnparsedSql(sql.toString())};
    }
}
